package com.lovisgod.iswhpay.ui.uiState

interface PinHandler {
    fun onCancel()
//    fun onConfirm()
    fun onPinResponse(pinblock: String = "", ksn: String)
}