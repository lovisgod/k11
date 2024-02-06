package com.lovisgod.iswhpay.data

import android.content.Context
import android.graphics.Bitmap
import android.text.BoringLayout
import com.lovisgod.iswhpay.ui.uiState.PrintingState
import com.lovisgod.iswhpay.ui.uiState.ReadCardStates
import com.lovisgod.iswhpay.utils.IswHpCodes
import com.lovisgod.iswhpay.utils.models.TerminalInfo

class DataSource(val emvDataKeyManager: EmvDataKeyManager, val emvPaymentHandler: EmvPaymentHandler) {

    suspend fun downloadAid(): Int {
        return try {
            emvDataKeyManager.clearAID()
            emvDataKeyManager.downloadAID()
            IswHpCodes.SUCCESS
        } catch (e: Exception) {
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }

    suspend fun downloadCapk(): Int {
        return try {
            emvDataKeyManager.clearCapk()
            emvDataKeyManager.downloadCapk()
            IswHpCodes.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }

    suspend fun setEmvParameter(terminalInfo: TerminalInfo): Int {
        return try {
            emvDataKeyManager.setEmvConfig(terminalInfo)
            IswHpCodes.SUCCESS
        } catch (e: Exception) {
            e.printStackTrace()
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }

    suspend fun setPinKey(isDukpt: Boolean = true, key: String = "", ksn: String = ""): Int {
        return try {
            emvDataKeyManager.setPinKey(isDukpt, key, ksn)
        } catch (e:Exception) {
            e.printStackTrace()
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }

    suspend fun pay(amount: Long, readCardStates: ReadCardStates, context: Context) {
        try {
            emvPaymentHandler.pay(amount, readCardStates, context)
        } catch (e: Exception) {
            e.printStackTrace()
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }

    suspend fun setIsKimono(isKimono: Boolean){
        try {
            emvPaymentHandler.setIsKimono(isKimono)
        } catch (e: Exception) {
            e.printStackTrace()
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }


    suspend fun continueTransaction(condition: Boolean) {
        try {
            emvPaymentHandler.continueTransaction(condition)
        } catch (e: Exception) {
            e.printStackTrace()
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }

    suspend fun stopTransaction() {
        try {
            emvPaymentHandler.stopTransaction()
        } catch (e: Exception) {
            e.printStackTrace()
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }
    suspend fun printBitMap(bitmap: Bitmap, printingState: PrintingState) {
        try {
            emvPaymentHandler.handlePrinting(bitmap, printingState)
        } catch (e: Exception) {
            e.printStackTrace()
            IswHpCodes.GENERAL_EMV_EXCEPTION
        }
    }
}