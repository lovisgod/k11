package com.lovisgod.iswhpay.ui.UIView

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import com.isw.iswkozen.core.data.utilsData.KeysUtils
import com.lovisgod.iswhpay.IswHpayApplication
import com.lovisgod.iswhpay.R
import com.lovisgod.iswhpay.domain.use_cases.AllUseCases
import com.lovisgod.iswhpay.ui.uiState.ReadCardStates
import com.lovisgod.iswhpay.utils.ToastUtils
import com.lovisgod.iswhpay.utils.models.TerminalInfo
import com.lovisgod.iswhpay.utils.models.iccData.RequestIccData
import com.lovisgod.iswhpay.utils.models.pay.CreditCard
import com.lovisgod.iswhpay.utils.models.pay.OnlineRespEntitiy
import com.lovisgod.iswhpay.utils.models.pay.TransactionResultCode
import kotlinx.coroutines.*

class MainActivity : AppCompatActivity(), ReadCardStates {
    lateinit var loadPinKeyBtn: Button
    lateinit var loadAid: Button
    lateinit var loadTerminalConfig: Button
    lateinit var startpay: Button
    lateinit var useCases: AllUseCases
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loadAid = findViewById(R.id.loadaid)
        loadPinKeyBtn = findViewById(R.id.pinkey)
        loadTerminalConfig = findViewById(R.id.loadTerminal)
        startpay = findViewById(R.id.startPay)

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
                    var ipekKsn = KeysUtils.getIpekKsn(false)
                    var ksn = ipekKsn.ksn
                    var key = ipekKsn.ipek
                    var isDukpt = true
                    var ret = useCases.setPinKeyUseCase(isDukpt, key, ksn)
                    println("dukpt ret ::: $ret")
                }
            }
        }

        loadTerminalConfig.setOnClickListener {

            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                   val terminalInfo = TerminalInfo()
                   var ret  = useCases.setTerminalConfigUseCase(terminalInfo)
                    println("config ret ::: $ret")
                }
            }
        }

        startpay.setOnClickListener {

            GlobalScope.launch {
                withContext(Dispatchers.IO) {
                    useCases.emvPayUseCase(100L, this@MainActivity)
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

    override fun sendTransactionOnline(creditCard: CreditCard): OnlineRespEntitiy {
        this.runOnUiThread {
            ToastUtils.showLong("Transaction need to go online", this)
            println("Transaction need to go online")
        }
        return OnlineRespEntitiy().also {
            it.respCode = "00"
            it.iccData = "${creditCard.emvData.iccData}"
        }
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


}