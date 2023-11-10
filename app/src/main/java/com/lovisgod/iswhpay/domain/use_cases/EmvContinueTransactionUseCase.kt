package com.lovisgod.iswhpay.domain.use_cases

import android.content.Context
import com.lovisgod.iswhpay.domain.HorizonRepository
import com.lovisgod.iswhpay.ui.uiState.ReadCardStates
import com.lovisgod.iswhpay.utils.HorizonPayException

class EmvContinueTransactionUseCase (private val repository: HorizonRepository) {

    @Throws(HorizonPayException::class)
    suspend operator fun invoke(condition: Boolean){
        return repository.continueTransaction(condition)
    }
}