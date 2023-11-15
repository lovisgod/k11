package com.lovisgod.iswhpay.utils

import com.lovisgod.iswhpay.data.EmvDataKeyManager
import com.lovisgod.iswhpay.data.DataSource
import com.lovisgod.iswhpay.data.EmvPaymentHandler
import com.lovisgod.iswhpay.domain.HorizonRepository
import com.lovisgod.iswhpay.domain.use_cases.*


class HorizonAppContainer {

    val emvDataKeyManager = EmvDataKeyManager()
    val emvPaymentHandler = EmvPaymentHandler()
    private val dataSource = DataSource(emvDataKeyManager, emvPaymentHandler)
    private val repository = HorizonRepository(dataSource)


    fun getUseCases(): AllUseCases {
        println("this got called")
         return AllUseCases(
             downloadAid = DownloadAidUseCase(repository),
             downloadCapkUseCase = DownloadCapkUseCase(repository),
             setTerminalConfigUseCase = SetTerminalConfigUseCase(repository),
             setPinKeyUseCase = SetPinKeyUseCase(repository),
             emvPayUseCase = EmvPayUseCase(repository),
             printBitMapUseCase = PrintBitMapUseCase(repository),
             continueTransactionUseCase = EmvContinueTransactionUseCase(repository),
             emvSetIsKimonoUseCase = EmvSetIsKimonoUseCase(repository)
         )
    }

//    fun initializeEmvDataManager()

}

