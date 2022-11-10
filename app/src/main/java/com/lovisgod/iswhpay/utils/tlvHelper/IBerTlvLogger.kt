package com.lovisgod.iswhpay.utils.tlvHelper

interface IBerTlvLogger {
    val isDebugEnabled: Boolean
    fun debug(aFormat: String?, vararg args: Any?)
}