package com.lovisgod.iswhpay.domain

import android.content.Context
import com.lovisgod.iswhpay.data.DataSource
import com.lovisgod.iswhpay.ui.uiState.ReadCardStates
import com.lovisgod.iswhpay.utils.models.TerminalInfo

class HorizonRepository(val dataSource: DataSource) {
    suspend fun downloadAid() = dataSource.downloadAid()
    suspend fun dowloadCapk() = dataSource.downloadCapk()
    suspend fun setTerminalConfig(terminalInfo: TerminalInfo) = dataSource.setEmvParameter(terminalInfo)
    suspend fun setPinKey(
        isDukpt: Boolean = true, key: String = "", ksn: String = "") = dataSource.setPinKey(isDukpt, key, ksn)
    suspend fun pay(amount: Long, readCardStates: ReadCardStates, context: Context) = dataSource.pay(amount, readCardStates, context)
}