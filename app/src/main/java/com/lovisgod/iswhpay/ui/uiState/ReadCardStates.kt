package com.lovisgod.iswhpay.ui.uiState

import com.lovisgod.iswhpay.utils.models.iccData.RequestIccData
import com.lovisgod.iswhpay.utils.models.pay.OnlineRespEntity
import com.lovisgod.iswhpay.utils.models.pay.TransactionResultCode

interface ReadCardStates {

    fun onInsertCard()
    fun onRemoveCard()
    fun onPinInput()
    fun sendTransactionOnline(emvData: RequestIccData): OnlineRespEntity
    fun onEmvProcessing(message: String = "Please wait while we read card")
    fun onEmvProcessed(data: Any?, code: TransactionResultCode)
}