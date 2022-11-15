package com.lovisgod.iswhpay.utils

import com.pixplicity.easyprefs.library.Prefs
import java.util.*

object Constants {

    fun ISW_KIMONO_BASE_URL(isTest: Boolean): String{
        return if(!isTest) Production.ISW_KIMONO_BASE_URL
        else Test.ISW_KIMONO_BASE_URL
    }

    fun ISW_KIMONO_URL(isTest: Boolean): String {
        return if(isTest) Test.ISW_KIMONO_URL
        else Production.ISW_KIMONO_URL
    }

    fun PAYMENT_CODE(isTest: Boolean): String {
        return if(isTest) Test.PAYMENT_CODE
        else Production.PAYMENT_CODE
    }

    fun ISW_TERMINAL_PORT(isTest: Boolean): Int {
        return if(isTest) Test.ISW_TERMINAL_PORT_CTMS
        else Production.ISW_TERMINAL_PORT_CTMS
    }

    fun getKeyLabl(isTest: Boolean): String {
        return if (isTest) "000006" else "000002"
    }

    val ISW_KIMONO_KEY_URL: String
        get() {
            return Production.ISW_KEY_DOWNLOAD_URL
        }

    //    internal const val KIMONO_END_POINT = "kmw/v2/kimonoservice"
    internal const val KIMONO_END_POINT = "kmw/kimonoservice"
    internal const val KIMONO_MERCHANT_DETAILS_END_POINT = "kmw/serialid/{terminalSerialNo}.xml"
    internal const val KIMONO_MERCHANT_DETAILS_END_POINT_AUTO = "kmw/v2/serialid/{terminalSerialNo}"
    internal const val ISW_TOKEN_URL = "/kimonotms/requesttoken/perform-process"


    var CLSS_POS_DATA_CODE = "A10101711344101"
    var CONTACT_POS_DATA_CODE_PIN = "510101511344101"
    var CONTACT_POS_DATA_CODE_NO_PIN = "511101511344101"
    var POS_ENTRY_MODE = "051"
    var POS_DATA_CODE = ""
    const val TIMEOUT_CODE = "0x0x0"

    /**
     * This method returns the next STAN (System Trace Audit Number)
     */
    fun getNextStan(): String {
        var stan = Prefs.getInt("STAN", 0)

        // compute and save new stan
        val newStan = if (stan > 999999) 0 else ++stan
        Prefs.putInt("STAN", newStan)

        return String.format(Locale.getDefault(), "%06d", newStan)
    }


    private object Production {

        const val ISW_USSD_QR_BASE_URL = "https://api.interswitchng.com/paymentgateway/api/v1/"
        const val ISW_TOKEN_BASE_URL = "https://passport.interswitchng.com/passport/"
        const val ISW_IMAGE_BASE_URL = "https://mufasa.interswitchng.com/p/paymentgateway/"
        const val ISW_KIMONO_URL = "https://kimono.interswitchng.com/kmw/v2/kimonoservice"
        const val ISW_KIMONO_BASE_URL = "https://kimono.interswitchng.com/"
        const val ISW_TERMINAL_IP_EPMS = "196.6.103.73"
        const val ISW_TERMINAL_PORT_EPMS = 5043

        const val ISW_TERMINAL_IP_CTMS = "196.6.103.18"
        const val ISW_TERMINAL_PORT_CTMS = 5008
        const val ISW_KEY_DOWNLOAD_URL = "http://kimono.interswitchng.com/kmw/keydownloadservice"
        const val PAYMENT_CODE = "04358001"
    }

    private object Test {
        //        const val ISW_USSD_QR_BASE_URL = "https://api.interswitchng.com/paymentgateway/api/v1/"
        const val ISW_USSD_QR_BASE_URL = "https://project-x-merchant.k8.isw.la/paymentgateway/api/v1/"
        const val ISW_TOKEN_BASE_URL = "https://passport.interswitchng.com/passport/"
        const val ISW_IMAGE_BASE_URL = "https://mufasa.interswitchng.com/p/paymentgateway/"
        const val ISW_KIMONO_URL = "https://qa.interswitchng.com/kmw/v2/kimonoservice"
        const val ISW_KIMONO_BASE_URL = "https://qa.interswitchng.com/"
        const val ISW_TERMINAL_IP_EPMS = "196.6.103.72"
        const val ISW_TERMINAL_PORT_EPMS = 5043

        const val ISW_TERMINAL_IP_CTMS = "196.6.103.10"
        const val ISW_TERMINAL_PORT_CTMS = 55533
        const val PAYMENT_CODE = "051626554287"
    }

    /**
     * This method returns the next STAN (System Trace Audit Number)
     */
    fun getNextKsnCounter(): String {
        var ksn = Prefs.getInt("KSNCOUNTER", 0)

        // compute and save new stan
        val newKsn = if (ksn >= 9) 1 else ++ksn
        Prefs.putInt("KSNCOUNTER", ksn)

        return newKsn.toString()
    }
}