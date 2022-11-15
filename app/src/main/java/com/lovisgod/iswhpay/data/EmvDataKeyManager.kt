package com.lovisgod.iswhpay.data

import android.os.RemoteException
import android.os.SystemClock
import com.horizonpay.smartpossdk.aidl.emv.AidEntity
import com.horizonpay.smartpossdk.aidl.emv.IAidlEmvL2
import com.horizonpay.smartpossdk.aidl.pinpad.DukptObj
import com.horizonpay.smartpossdk.aidl.pinpad.IAidlPinpad
import com.horizonpay.smartpossdk.data.PinpadConst
import com.lovisgod.iswhpay.utils.*
import com.lovisgod.iswhpay.utils.models.ConfigInfoHelper.saveTerminalInfo
import com.lovisgod.iswhpay.utils.models.TerminalInfo
import com.pixplicity.easyprefs.library.Prefs

class EmvDataKeyManager {
    private var mEmvL2: IAidlEmvL2? = null
    private var isSupport = false
    private var pinpad: IAidlPinpad? = null
    private var isSupportPinPad = false

    fun initialize() {
        mEmvL2 = DeviceHelper.getEmvHandler()
        isSupport = mEmvL2!!.isSupport()
        pinpad = DeviceHelper.getPinpad()
        isSupportPinPad = pinpad.let {
            return@let it!!.isSupport()
        }
    }

     fun downloadAID() {
        val aidEntityList: List<AidEntity> = AidsUtil.getAllAids()
        var ret = false
        for (i in aidEntityList.indices) {
            val tip = "Download aid" + String.format("(%d)", i)
           println("event::::::: ===>>> $tip")
            val emvAidPara = aidEntityList[i]
            try {
                ret = mEmvL2!!.addAid(emvAidPara)
                if (!ret) {
                    break
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
    }

     fun clearAID() {
        try {
            val ret = mEmvL2!!.deleteAllAids()
            println("clear aid ::: $ret")
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun downloadCapk() {
        val capkEntityList = AidsUtil.getAllCapks()
        try {
            mEmvL2!!.addCapks(capkEntityList)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }


        var ret = false
        for (i in capkEntityList.indices) {
            val tip = "Download capk" + String.format("(%d)", i)
            println(tip)
            val emvCapkPara = capkEntityList[i]
            try {
                ret = mEmvL2!!.addCapk(emvCapkPara)
                if (!ret) {
                    break
                }
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
            SystemClock.sleep(50)
            println( "Download capk :" + emvCapkPara.rid + if (ret == true) " success" else " fail")
        }
        println("Download capk " + if (ret == true) "success" else "fail")
    }

    fun clearCapk() {
        try {
            val ret = mEmvL2!!.deleteAllCapks()
            println("clear capk ::: $ret")
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
    }

    fun setEmvConfig(terminalInfo: TerminalInfo) {
        saveTerminalInfo(terminalInfo)
        mEmvL2?.termConfig = EmvUtil.getInitTermConfig(terminalInfo)
    }


    private fun setDukpt(key: String, ksn:String): Int {
        pinpad!!.setKeyAlgorithm(PinpadConst.KeyAlgorithm.DUKPT)

        val dukptObj = DukptObj(
            key,
            ksn,
            PinpadConst.DukptKeyType.DUKPT_IPEK_PLAINTEXT,
            PinpadConst.DukptKeyIndex.DUKPT_KEY_INDEX_1
        )
        val res  = pinpad!!.dukptKeyLoad(dukptObj)
        pinpad!!.dukptKsnIncrease(PinpadConst.DukptKeyIndex.DUKPT_KEY_INDEX_0)
        Prefs.putString("KSN", StringManipulator.dropLastCharacter(ksn))
        return  res
    }

    fun setPinKey(isDukpt: Boolean, key: String = "", ksn: String = ""): Int {
        if (isDukpt) return  setDukpt(key, ksn) else return IswHpCodes.NOT_SUPPORTED // implement pin key later
    }
}