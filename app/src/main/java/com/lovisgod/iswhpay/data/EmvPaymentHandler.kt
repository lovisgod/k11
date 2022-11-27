package com.lovisgod.iswhpay.data

import android.content.Context
import android.graphics.Bitmap
import android.os.RemoteException
import com.horizonpay.smartpossdk.aidl.emv.CandidateAID
import com.horizonpay.smartpossdk.aidl.emv.IAidlEmvL2
import com.horizonpay.smartpossdk.aidl.printer.AidlPrinterListener
import com.horizonpay.smartpossdk.aidl.printer.IAidlPrinter
import com.horizonpay.smartpossdk.data.PrinterConst
import com.lovisgod.iswhpay.ui.uiState.PrintingState
import com.lovisgod.iswhpay.ui.uiState.ReadCardStates
import com.lovisgod.iswhpay.utils.DeviceHelper
import com.lovisgod.iswhpay.utils.EmvUtil
import com.lovisgod.iswhpay.utils.models.iccData.EmvPinData
import com.lovisgod.iswhpay.utils.models.iccData.getIccData
import com.lovisgod.iswhpay.utils.models.pay.CardReadMode
import com.lovisgod.iswhpay.utils.models.pay.CreditCard
import com.lovisgod.iswhpay.utils.models.pay.OnlineRespEntity
import com.lovisgod.iswhpay.utils.models.pay.TransactionResultCode

class EmvPaymentHandler {
    private var mEmvL2: IAidlEmvL2? = null
    private var isSupport = false
    private var payProcessor: PayProcessor? = null
    private var readCardStates: ReadCardStates?  = null
    private var printingState: PrintingState? = null
    private var printer : IAidlPrinter? = null
//    private var pinpad: IAidlPinpad? = null
//    private var isSupportPinPad = false

    fun initialize(context: Context) {
        printer = DeviceHelper.getPrinter()
        mEmvL2 = DeviceHelper.getEmvHandler()
        isSupport = mEmvL2!!.isSupport()
        payProcessor = PayProcessor()
//        pinpad = DeviceHelper.getPinpad()
//        isSupportPinPad = pinpad.let {
//            return@let it!!.isSupport()
//        }
    }

    fun pay (amount: Long, readCardStates: ReadCardStates, context: Context) {
        payProcessor = PayProcessor(context)
        this.readCardStates = readCardStates
        payProcessor?.pay(amount, processorListener)
    }

    private fun setPrintLevel(level: Int) {
        try {
            printer!!.printGray = level
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun handlePrinting(bitmap: Bitmap, printingState: PrintingState){
        this.printingState = printingState
        return try {
            return printer!!.printBmp(
                true,
                false,
                bitmap,
                0,
                object : AidlPrinterListener.Stub() {
                    @Throws(RemoteException::class)
                    override fun onError(i: Int) {
                        when (i) {
                            PrinterConst.RetCode.ERROR_PRINT_NOPAPER,
                            PrinterConst.RetCode.ERROR_DEV ,
                            PrinterConst.RetCode.ERROR_DEV_IS_BUSY,
                            PrinterConst.RetCode.ERROR_OTHER -> {
                                this@EmvPaymentHandler.printingState!!.onError(i)
                            }
                            else -> {
                                this@EmvPaymentHandler.printingState!!.onError(i)
                            }
                        }
                    }

                    @Throws(RemoteException::class)
                    override fun onPrintSuccess() {
                        println("info:::::: printing success::::::")
                        this@EmvPaymentHandler.printingState!!.onSuccess()
                    }
                })
        } catch (e: RemoteException) {
            e.printStackTrace()
            this.printingState!!.onError(PrinterConst.RetCode.ERROR_OTHER)
        }
    }

    private val processorListener: PayProcessor.PayProcessorListener = object : PayProcessor.PayProcessorListener {
        override fun onRetry(retryFlag: Int) {
            if (retryFlag == 0) {
                println("Please Insert/Tap Card")
                this@EmvPaymentHandler.readCardStates?.onInsertCard()
            } else {
                println(">>>>> on retry")
            }
        }

        override fun onCardDetected(cardReadMode: CardReadMode, creditCard: CreditCard) {
            when (cardReadMode) {
                CardReadMode.SWIPE -> {
                    val builder = StringBuilder()
                    builder.append(
                        """
                        -----------
                        Card: ${creditCard.getCardNumber()}
                        """.trimIndent()
                    )
                    builder.append(
                        """
                        
                        Expiry Date: ${creditCard.getExpireDate()}
                        """.trimIndent()
                    )
                    builder.append(
                        """
                        
                        CardholderName: ${creditCard.getHolderName()}
                        """.trimIndent()
                    )
                    builder.append(
                        """
                        
                        CardSequenceNumber: ${creditCard.getCardSequenceNumber()}
                        """.trimIndent()
                    )
                    builder.append(
                        """
                        
                        Track1: ${creditCard.getMagData().getTrack1()}
                        """.trimIndent()
                    )
                    builder.append(
                        """
                        
                        Track2: ${creditCard.getMagData().getTrack2()}
                        """.trimIndent()
                    )
                    builder.append("-------------------\n")
                    payProcessor!!.magCardInputPIN(creditCard.getCardNumber())
                }
                CardReadMode.CONTACT, CardReadMode.CONTACTLESS -> {}
                else -> {}
            }
        }

        override fun confirmApplicationSelection(candidateList: List<CandidateAID>): CandidateAID? {
            // Implement this for the sake of certification
            var selectedIndex = 0
//            try {
//                selectedIndex = AppSelectDialog(this@EmvActivity, candidateList).call()
//            } catch (e: Exception) {
//                e.printStackTrace()
//            }
            return candidateList[selectedIndex]
        }

        override fun onPerformOnlineProcessing(creditCard: CreditCard, isOnlinePin: Boolean): OnlineRespEntity? {
            // IMPLEMENT THIS SO SEND TRANSACTION ONLINE
            // get tlv data
            println("info ::::: iccdataforonline ::: ${EmvUtil.getTlvStringData()}")
            var requestIccData = getIccData()
            requestIccData.apply {
                EMC_CARD_ = creditCard
                iccAsString = EmvUtil.getTlvStringData()
                CARD_HOLDER_NAME = creditCard?.holderName.toString()
                EMV_CARD_PIN_DATA = if (isOnlinePin) EmvPinData(creditCard.ksnData.toString(), creditCard.pin.toString()) else EmvPinData()
            }
            var responseEntity = this@EmvPaymentHandler.readCardStates?.sendTransactionOnline(requestIccData)
            println("online process ::: response code :::: ${responseEntity?.respCode}")
//            val entitiy =
//                OnlineRespEntity()
//            //            entitiy.setRespCode("05");
//            entitiy.setRespCode("00")
//            entitiy.setIccData("")
//            if ("00" == entitiy.getRespCode()) {
//                onCompleted(TransactionResultCode.APPROVED_BY_ONLINE, creditCard)
//            } else {
//                onCompleted(TransactionResultCode.DECLINED_BY_ONLINE, creditCard)
//            }
            return responseEntity
        }

        override fun onCompleted(result: TransactionResultCode?, creditCard: CreditCard?) {
            println("info:::::: on completed called:::: code ::::::${result?.name} :::: iccdata:::: ${EmvUtil.getTlvStringData()}")
            val resultText = StringBuilder()
            when (result) {
                TransactionResultCode.APPROVED_BY_OFFLINE -> {
                    var requestIccData = getIccData()
                    requestIccData.apply {
                        iccAsString = EmvUtil.getTlvStringData()
                    }
                    this@EmvPaymentHandler.readCardStates?.onEmvProcessed(requestIccData, TransactionResultCode.APPROVED_BY_OFFLINE)
                    resultText.append(TransactionResultCode.APPROVED_BY_OFFLINE.toString())
                }
                TransactionResultCode.APPROVED_BY_ONLINE -> {
                    var requestIccData = getIccData()
                    requestIccData.apply {
                        iccAsString = EmvUtil.getTlvStringData()
                        EMV_CARD_PIN_DATA = EmvPinData(creditCard?.ksnData.toString(), creditCard?.pin.toString())
                    }
                    this@EmvPaymentHandler.readCardStates?.onEmvProcessed(requestIccData, TransactionResultCode.APPROVED_BY_ONLINE)
                    resultText.append(TransactionResultCode.APPROVED_BY_ONLINE.toString())
                }
                TransactionResultCode.DECLINED_BY_OFFLINE -> {
                    this@EmvPaymentHandler.readCardStates?.onEmvProcessed(null, TransactionResultCode.DECLINED_BY_OFFLINE)
                    resultText.append(TransactionResultCode.DECLINED_BY_OFFLINE.toString())
                }
                TransactionResultCode.DECLINED_BY_ONLINE -> {
                    this@EmvPaymentHandler.readCardStates?.onEmvProcessed(null, TransactionResultCode.DECLINED_BY_ONLINE)
                    resultText.append(TransactionResultCode.DECLINED_BY_ONLINE.toString())
                }
                TransactionResultCode.DECLINED_BY_TERMINAL_NEED_REVERSE -> {
                    this@EmvPaymentHandler.readCardStates?.onEmvProcessed(null, TransactionResultCode.DECLINED_BY_TERMINAL_NEED_REVERSE)
                    resultText.append(TransactionResultCode.DECLINED_BY_TERMINAL_NEED_REVERSE.toString())
                }
                TransactionResultCode.ERROR_TRANSCATION_CANCEL -> {
                    this@EmvPaymentHandler.readCardStates?.onEmvProcessed(null, TransactionResultCode.ERROR_TRANSCATION_CANCEL)
                    resultText.append(TransactionResultCode.ERROR_TRANSCATION_CANCEL.toString())
                }
                TransactionResultCode.ERROR_UNKNOWN -> {
                    this@EmvPaymentHandler.readCardStates?.onEmvProcessed(null, TransactionResultCode.ERROR_UNKNOWN)
                    resultText.append(TransactionResultCode.ERROR_UNKNOWN.toString())
                }
                else -> {}
            }
              println(EmvUtil.showEmvTransResult().toString())
//            stopEmvProcess()
        }

        override fun onInputPin() {
            this@EmvPaymentHandler.readCardStates?.onPinInput()
        }
    }


    private fun stopEmvProcess() {
        println("this is called called called")
        try {
            mEmvL2!!.stopEmvProcess()
        } catch (e: RemoteException) {
            e.printStackTrace()
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }

    }

}