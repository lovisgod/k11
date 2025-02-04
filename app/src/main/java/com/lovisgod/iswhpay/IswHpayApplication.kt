package com.lovisgod.iswhpay

import android.app.Application
import android.content.Context
import android.content.ContextWrapper
import android.os.IBinder.DeathRecipient
import android.os.RemoteException
import com.horizonpay.smartpossdk.PosAidlDeviceServiceUtil
import com.horizonpay.smartpossdk.PosAidlDeviceServiceUtil.DeviceServiceListen
import com.horizonpay.smartpossdk.aidl.IAidlDevice
import com.horizonpay.smartpossdk.data.SysConst.DeviceInfo
import com.horizonpay.utils.BaseUtils
import com.lovisgod.iswhpay.utils.DeviceHelper
import com.lovisgod.iswhpay.utils.HorizonAppContainer
import com.pixplicity.easyprefs.library.Prefs

object IswHpayApplication {
    private val TAG = "IswHPayApplication"
    private var device: IAidlDevice? = null
    private var context: Application? = null




    fun getDevice(): IAidlDevice? {
        println("this is getting here here here here")
        return device
    }

    fun getDeviceSerial(): String? {
        println("<-------- About to make call for device serial -------->")

        return DeviceHelper.getSysHandle().also {
            println("<-------- Just got sysHandle, moving on to get device info -------->")
        }
            .deviceInfo.also {
                println("<-------- Just got device info, moving on to get Device serial -------->")
            }.getString(DeviceInfo.DEVICE_SN)
    }

    object container {
        var horizonAppContainer = HorizonAppContainer()
        var horizonPayUseCase = horizonAppContainer.getUseCases()
    }


    fun onCreate(context: Context, application: Application, serialNo: (String?) -> Unit) {
        this.context = application
        println("this is called first")
        println("<-------- OnCreate called for IswHpayApplication -------->")

        Prefs.Builder()
            .setContext(context)
            .setMode(ContextWrapper.MODE_PRIVATE)
            .setPrefsName("com.lovisgod.iswPay")
            .setUseDefaultSharedPreference(true)
            .build()

        BaseUtils.init(this.context!!).also {
            println("<-------- BaseUtils initialized -------->")
        }
        bindDriverService(context, { serialNo(it) })
    }

    fun bindDriverService(context: Context, serialNo: ((String?) -> Unit)?) {
        println("<-------- Bind driver service is called -------->")

        println("this is called third")
        PosAidlDeviceServiceUtil.connectDeviceService(context, object : DeviceServiceListen {

            override fun onConnected(device: IAidlDevice) {
                println("<-------- DeviceServiceListen - onConnected is called -------->")

                println("device is connected")
                println("this is application :::: $this@IswHpayApplication")
                println(device)
                this@IswHpayApplication.device = device
                try {
                    DeviceHelper.reset()
                    println("<-------- Init devices is just about to be called -------->")
                    DeviceHelper.initDevices(this@IswHpayApplication, this@IswHpayApplication.context).also {
                        println("<-------- Init devices is called -------->")
                    }
                    container.horizonAppContainer.emvDataKeyManager.initialize()
                    container.horizonAppContainer.emvPaymentHandler.initialize(context)
                    this@IswHpayApplication.device!!.asBinder().linkToDeath(deathRecipient, 0)

                    if (serialNo != null) {
                        serialNo(getDeviceSerial())
                    }
                } catch (e: RemoteException) {
                    e.printStackTrace()
                }
            }

            override fun error(errorcode: Int) {
                println("Error happened connecting device")
            }
            override fun onDisconnected() {
                println("Device disconnected")
            }
            override fun onUnCompatibleDevice() {
                println("Error un-compatible device")
            }
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
            this@IswHpayApplication.context?.applicationContext?.let { bindDriverService(it) {} }
        }
    }
}