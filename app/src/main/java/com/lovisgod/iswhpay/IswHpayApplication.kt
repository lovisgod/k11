package com.lovisgod.iswhpay

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.IBinder.DeathRecipient
import android.os.RemoteException
import com.horizonpay.smartpossdk.PosAidlDeviceServiceUtil
import com.horizonpay.smartpossdk.PosAidlDeviceServiceUtil.DeviceServiceListen
import com.horizonpay.smartpossdk.aidl.IAidlDevice
import com.horizonpay.utils.BaseUtils
import com.lovisgod.iswhpay.domain.use_cases.AllUseCases
import com.lovisgod.iswhpay.utils.DeviceHelper
import com.lovisgod.iswhpay.utils.HorizonAppContainer
import com.pixplicity.easyprefs.library.Prefs

object IswHpayApplication {
    private val TAG = "IswHPayApplication"
    private var device: IAidlDevice? = null
    private var context: Application? = null




    fun getDevice(): IAidlDevice? {
        return device
    }

    object container {
        var horizonAppContainer = HorizonAppContainer()
        var horizonPayUseCase = horizonAppContainer.getUseCases()
    }


    fun onCreate(context: Context, application: Application) {
        this.context = application
        println("this is called first")

        Prefs.Builder()
            .setContext(context)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName("com.lovisgod.iswPay")
            .setUseDefaultSharedPreference(true)
            .build()

        BaseUtils.init(this.context!!)
        bindDriverService(context)
    }

    fun bindDriverService(context: Context) {
        println("this is called third")
        PosAidlDeviceServiceUtil.connectDeviceService(context, object : DeviceServiceListen {
            override fun onConnected(device: IAidlDevice) {
                this@IswHpayApplication.device = device
                try {
                    DeviceHelper.reset()
                    DeviceHelper.initDevices(this@IswHpayApplication, this@IswHpayApplication.context)
                    container.horizonAppContainer.emvDataKeyManager.initialize()
                    container.horizonAppContainer.emvPaymentHandler.initialize(context)
                    this@IswHpayApplication.device!!.asBinder().linkToDeath(deathRecipient, 0)
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }

            override fun error(errorcode: Int) {}
            override fun onDisconnected() {}
            override fun onUnCompatibleDevice() {}
        })
    }

    private val deathRecipient: DeathRecipient = object : DeathRecipient {
        override fun binderDied() {
            if (this@IswHpayApplication.device == null) {
                println("binderDied device is null")
                return
            }
            this@IswHpayApplication.device!!.asBinder().unlinkToDeath(this, 0)
            this@IswHpayApplication.device = null

            //reBind driver Service
            this@IswHpayApplication.context?.applicationContext?.let { bindDriverService(it) }
        }
    }
}