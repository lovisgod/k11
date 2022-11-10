package com.lovisgod.iswhpay.utils.models.iccData

import com.isw.iswkozen.core.data.utilsData.KeysUtils


data class EmvPinData (
    var ksn : String = KeysUtils.getIpekKsn(false).ksn,
    var CardPinBlock: String = "")