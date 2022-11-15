package com.lovisgod.iswhpay.utils.networkHandler.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import org.simpleframework.xml.Element
import org.simpleframework.xml.Root

@Parcelize
@Root(name = "tokenPassportRequest", strict = false)
data class TokenRequestModel (
    @field:Element(name = "terminalInformation", required = false)
    var terminalInformation: TerminalInformationRequest? = null
): Parcelable