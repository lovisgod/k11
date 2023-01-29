package com.lovisgod.iswhpay.ui.UIView

import android.app.Application
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.ScrollView
import com.lovisgod.iswhpay.IswHpayApplication
import com.lovisgod.iswhpay.R
import com.lovisgod.iswhpay.domain.SampleNetworkRepository
import com.lovisgod.iswhpay.domain.use_cases.AllUseCases
import com.lovisgod.iswhpay.ui.uiState.PrintingState
import com.lovisgod.iswhpay.ui.uiState.ReadCardStates
import com.lovisgod.iswhpay.utils.KeysUtilx
import com.lovisgod.iswhpay.utils.ToastUtils
import com.lovisgod.iswhpay.utils.models.TerminalInfo
import com.lovisgod.iswhpay.utils.models.iccData.RequestIccData
import com.lovisgod.iswhpay.utils.models.pay.OnlineRespEntity
import com.lovisgod.iswhpay.utils.models.pay.TransactionResultCode
import kotlinx.coroutines.*
import kotlin.properties.Delegates

class MainActivity : AppCompatActivity(), ReadCardStates, PrintingState {
    lateinit var loadPinKeyBtn: Button
    lateinit var loadAid: Button
    lateinit var loadTerminalConfig: Button
    lateinit var startpay: Button
    lateinit var downloadtoken: Button
    lateinit var printBtn: Button
    lateinit var xxxxxx : ScrollView
    lateinit var useCases: AllUseCases
    var amount by Delegates.notNull<Int>()
     var respEntity: OnlineRespEntity = OnlineRespEntity().apply {
         respCode = "12"
     }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        IswHpayApplication.onCreate(this, SampleApplication())

        loadAid = findViewById(R.id.loadaid)
        loadPinKeyBtn = findViewById(R.id.pinkey)
        loadTerminalConfig = findViewById(R.id.loadTerminal)
        startpay = findViewById(R.id.startPay)
        downloadtoken = findViewById(R.id.downloadToken)
        printBtn = findViewById(R.id.printScreen)
        xxxxxx = findViewById(R.id.vvvv)

       handleClicks()
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun handleClicks () {
        println("this is called second")
        useCases = IswHpayApplication.container.horizonPayUseCase
        loadAid.setOnClickListener {

            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    var retaid = useCases.downloadAid()
                    var retcapk =  useCases.downloadCapkUseCase()
                    println("aid ret ::: $retaid:::: capk ret ::: $retcapk")
                }
            }
        }

        loadPinKeyBtn.setOnClickListener {

            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    val ipekKsn = KeysUtilx.getIpekKsn(false)
                    val ksn = ipekKsn.ksn
                    val key = ipekKsn.ipek
                    val isDukpt = true
                    val ret = useCases.setPinKeyUseCase(isDukpt, key, ksn)
                    println("dukpt ret ::: $ret")
                }
            }
        }

        loadTerminalConfig.setOnClickListener {

            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                   val terminalInfo = TerminalInfo()

                    val ret  = useCases.setTerminalConfigUseCase(terminalInfo)
                    println("config ret ::: $ret")
                }
            }
        }

        startpay.setOnClickListener {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    amount = 100
                    useCases.emvPayUseCase(amount.toLong(), this@MainActivity, this@MainActivity)
                }
            }
        }

        downloadtoken.setOnClickListener {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    SampleNetworkRepository().getToken()
                }
            }
        }

        printBtn.setOnClickListener {
            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    val bitmap = ToastUtils.loadBitmapFromView(xxxxxx)
                    if (bitmap != null) {
                        useCases.printBitMapUseCase(bitmap, this@MainActivity)
                    }
                }
            }
        }
    }

    override fun onInsertCard() {
       this.runOnUiThread {
           ToastUtils.showLong("Kindly insert your card", this)
       }
    }

    override fun onRemoveCard() {
        this.runOnUiThread {
            ToastUtils.showLong("Kindly remove your card", this)
            println("Kindly remove your card")
        }
    }

    override fun onPinInput() {
        this.runOnUiThread {
            ToastUtils.showLong("Kindly input your card pin", this)
            println("Kindly input your card pin")
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    override fun sendTransactionOnline(emvData: RequestIccData): OnlineRespEntity {
        this.runOnUiThread {
            ToastUtils.showLong("Transaction need to go online", this)
            println("Transaction need to go online")
            println("info::: iccdata going online ::: ${emvData.iccAsString}")
        }
        GlobalScope.launch {
            return@launch withContext(Dispatchers.IO) {
                val response = SampleNetworkRepository().makeTransactionOnline(emvData, amount)
               withContext(Dispatchers.Main) {
                   respEntity = response
                   respEntity
               }
            }
        }
        println("info:::: re")
        return respEntity
    }


    override fun onEmvProcessing(message: String) {
       this.runOnUiThread {
           ToastUtils.showLong("Hiiiiiii $message", this)
           println("Hiiiiiii $message")
       }
    }

    override fun onEmvProcessed(data: Any?, code: TransactionResultCode) {
        this.runOnUiThread {
            ToastUtils.showLong("Emv has been processed", this)
            println("Emv has been processed")
        }
        var datax  = data as RequestIccData
        println("code :::: ${code.name} :::::: data :::: ${data.TRANSACTION_AMOUNT}:::: ${data.CARD_HOLDER_NAME}:::: ${data.EMV_CARD_PIN_DATA.CardPinBlock}")
    }

    override fun onSuccess(code: Int) {
        println("printing successful::: $code")
    }

    override fun onError(error: Int) {
        println("printing error::: $error")
    }


}

class SampleApplication: Application() {

}