package com.lovisgod.iswhpay.domain.use_cases

import android.content.Context
import com.lovisgod.iswhpay.domain.HorizonRepository
import com.lovisgod.iswhpay.ui.uiState.ReadCardStates
import com.lovisgod.iswhpay.utils.HorizonPayException

class EmvSetIsKimonoUseCase (private val repository: HorizonRepository) {

    @Throws(HorizonPayException::class)
    suspend operator fun invoke(isKimono: Boolean){
        return repository.setIsKimono(isKimono)
    }
}