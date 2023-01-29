package com.lovisgod.iswhpay.utils.models.iccData

import com.lovisgod.iswhpay.utils.KeysUtilx


data class EmvPinData (
    var ksn : String = KeysUtilx.getIpekKsn(false).ksn,
    var CardPinBlock: String = "")