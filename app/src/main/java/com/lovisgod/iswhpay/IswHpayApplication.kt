package com.lovisgod.iswhpay

import android.app.Application
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

class IswHpayApplication: Application() {
    private val TAG = "IswHPayApplication"
    private var INSTANCE: IswHpayApplication? = null
    private var device: IAidlDevice? = null


     fun getINSTANCE(): IswHpayApplication? {
        return INSTANCE
    }

    fun getDevice(): IAidlDevice? {
        return device
    }

    object container {
        var horizonAppContainer = HorizonAppContainer()
        var horizonPayUseCase = horizonAppContainer.getUseCases()
    }


    override fun onCreate() {
        println("this is called first")
        super.onCreate()

        Prefs.Builder()
            .setContext(this)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName(packageName)
            .setUseDefaultSharedPreference(true)
            .build()

        INSTANCE = this
        BaseUtils.init(this)
        bindDriverService()
    }

    fun bindDriverService() {
        println("this is called third")
        PosAidlDeviceServiceUtil.connectDeviceService(this, object : DeviceServiceListen {
            override fun onConnected(device: IAidlDevice) {
                this@IswHpayApplication.device = device
                try {
                    DeviceHelper.reset()
                    DeviceHelper.initDevices(this@IswHpayApplication)
                    container.horizonAppContainer.emvDataKeyManager.initialize()
                    container.horizonAppContainer.emvPaymentHandler.initialize()
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
            bindDriverService()
        }
    }
}