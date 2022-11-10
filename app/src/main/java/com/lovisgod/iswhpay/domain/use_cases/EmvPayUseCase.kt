package com.lovisgod.iswhpay.domain.use_cases

import com.lovisgod.iswhpay.domain.HorizonRepository
import com.lovisgod.iswhpay.ui.uiState.ReadCardStates
import com.lovisgod.iswhpay.utils.HorizonPayException

class EmvPayUseCase (private val repository: HorizonRepository) {

    @Throws(HorizonPayException::class)
    suspend operator fun invoke(amount:Long, readCardStates: ReadCardStates){
        return repository.pay(amount, readCardStates)
    }
}