package com.lovisgod.iswhpay.domain.use_cases

import com.lovisgod.iswhpay.domain.HorizonRepository
import com.lovisgod.iswhpay.utils.HorizonPayException
import com.lovisgod.iswhpay.utils.models.TerminalInfo

class DownloadCapkUseCase (private val repository: HorizonRepository) {

    @Throws(HorizonPayException::class)
    suspend operator fun invoke(): Int{
        return repository.dowloadCapk()
    }
}