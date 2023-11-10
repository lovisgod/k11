package com.lovisgod.iswhpay.utils.models

import android.os.Parcelable
import com.google.gson.Gson
import com.pixplicity.easyprefs.library.Prefs
import kotlinx.android.parcel.Parcelize
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Parcelize
@Root(name = "terminalInfoBySerials", strict = false)
data class TerminalInfo(

    @field:Element(name = "terminalCode", required = false)
    var terminalCode: String = "",

    @field:Element(name = "cardAcceptorId", required = false)
    var cardAcceptorId: String = "",

    @field:Element(name = "merchantId", required = false)
    var merchantId: String = "",

    @field:Element(name = "merchantName", required = false)
    var merchantName: String = "Interswitch ng sample terminal",

    @field:Element(name = "merchantAddress1", required = false)
    var merchantAddress1: String = "Building 9, Oko Awo Street, VI, Lagos",

    @field:Element(name = "merchantAddress2", required = false)
    var merchantAddress2: String = "Lagos state",

    @field:Element(name = "merchantPhoneNumber", required = false)
    var merchantPhoneNumber: String = "08165656988",

    @field:Element(name = "merchantEmail", required = false)
    var merchantEmail: String = "sample@interswitchng.com",

    @field:Element(name = "merchantState", required = false)
    var merchantState: String = "Lagos state",

    @field:Element(name = "tmsRouteType", required = false)
    var tmsRouteType: String = "kimono",

    @field:Element(name = "merchantCity", required = false)
    var merchantCity: String = "Lagos",

    @field:Element(name = "qtbMerchantCode", required = false)
    var qtbMerchantCode: String? = "",

    @field:Element(name = "qtbMerchantAlias", required = false)
    var qtbMerchantAlias: String? = "",

    @field:Element(name = "cardAcceptorNameLocation", required = false)
    var cardAcceptorNameLocation: String = "",

    @field:Element(name = "merchantCategoryCode", required = false)
    var merchantCategoryCode: String = "8099",

    var  terminalCountryCode: String = "0566",
    var  transCurrencyCode: String = "0566",
    var transCurrencyExp: String = "02",
    var terminalType: String = "22",
    var terminalCapabilities: String = "E0F8C8",
    var terminalExtCapabilities: String = "F000F0F001",
    var terminalEntryMode: String = "05"
): Parcelable {
    override fun toString(): String {
        """code: ${terminalCode}
                   capailty: ${terminalCapabilities}
                   name: ${merchantName}
                """.trimIndent()
        return super.toString()
    }
}


object ConfigInfoHelper {
    fun readTerminalInfo(): TerminalInfo {
        val dataString = Prefs.getString("TERMINAL_INFO_KEY")
        return Gson().fromJson(dataString, TerminalInfo::class.java) ?: TerminalInfo()
    }


    fun saveTerminalInfo(data: TerminalInfo) {
        val dataString = Gson().toJson(data)
        Prefs.putString("TERMINAL_INFO_KEY", dataString)
    }
}