package com.lovisgod.iswhpay.domain.use_cases

import android.content.Context
import android.graphics.Bitmap
import com.lovisgod.iswhpay.domain.HorizonRepository
import com.lovisgod.iswhpay.ui.uiState.PrintingState
import com.lovisgod.iswhpay.ui.uiState.ReadCardStates
import com.lovisgod.iswhpay.utils.HorizonPayException

class PrintBitMapUseCase(private val repository: HorizonRepository) {

    suspend operator fun invoke(bitmap: Bitmap, printingState: PrintingState){
        return repository.printBitMap(bitmap, printingState)
    }
}
