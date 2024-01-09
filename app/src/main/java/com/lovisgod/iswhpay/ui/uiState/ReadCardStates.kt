package com.lovisgod.iswhpay.ui.uiState

import com.lovisgod.iswhpay.utils.models.iccData.RequestIccData
import com.lovisgod.iswhpay.utils.models.pay.OnlineRespEntity
import com.lovisgod.iswhpay.utils.models.pay.TransactionResultCode
import com.lovisgod.iswhpay.utils.networkHandler.models.AccountType

interface ReadCardStates {

    fun onInsertCard()

    fun onCardDetected() {
        println("card has been detected")
    }
    fun onRemoveCard()
    fun onPinInput()
    fun sendTransactionOnline(emvData: RequestIccData): OnlineRespEntity
    fun onEmvProcessing(message: String = "Please wait while we read card")
    fun onEmvProcessed(data: Any?, code: TransactionResultCode)
    fun onSelectAccountType(): AccountType

    fun onCardRead(cardType: String, cardNo: String) {
        println("$cardType card has been read")
    }
}