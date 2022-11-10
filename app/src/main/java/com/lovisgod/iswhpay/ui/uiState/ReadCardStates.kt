package com.lovisgod.iswhpay.ui.uiState

import com.lovisgod.iswhpay.utils.models.pay.CreditCard
import com.lovisgod.iswhpay.utils.models.pay.OnlineRespEntitiy
import com.lovisgod.iswhpay.utils.models.pay.TransactionResultCode

interface ReadCardStates {

    fun onInsertCard()
    fun onRemoveCard()
    fun onPinInput()
    fun sendTransactionOnline(creditCard: CreditCard): OnlineRespEntitiy
    fun onEmvProcessing(message: String = "Please wait while we read card")
    fun onEmvProcessed(data: Any?, code: TransactionResultCode)
}