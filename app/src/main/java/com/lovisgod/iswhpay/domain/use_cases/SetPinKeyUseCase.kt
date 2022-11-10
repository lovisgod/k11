package com.lovisgod.iswhpay.domain.use_cases

import com.lovisgod.iswhpay.domain.HorizonRepository
import com.lovisgod.iswhpay.utils.HorizonPayException

class SetPinKeyUseCase(val repository: HorizonRepository) {

    @Throws(HorizonPayException::class)
    suspend operator fun invoke(isDukpt: Boolean = true, key: String, ksn: String): Int{
        return repository.setPinKey(isDukpt, key, ksn)
    }
}