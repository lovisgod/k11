package com.lovisgod.iswhpay.domain.use_cases

import com.lovisgod.iswhpay.domain.HorizonRepository
import com.lovisgod.iswhpay.utils.HorizonPayException
import com.lovisgod.iswhpay.utils.models.TerminalInfo

class SetTerminalConfigUseCase(private val repository: HorizonRepository) {
    @Throws(HorizonPayException::class)
    suspend operator fun invoke(terminalInfo: TerminalInfo): Int{
        return repository.setTerminalConfig(terminalInfo)
    }
}