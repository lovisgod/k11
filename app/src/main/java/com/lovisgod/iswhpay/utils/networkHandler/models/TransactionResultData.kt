package com.lovisgod.iswhpay.utils.networkHandler.models

import android.os.Parcelable

import kotlinx.android.parcel.Parcelize
import java.time.LocalDate.now
import java.util.*


@Parcelize
data class TransactionResultData(
    val paymentType: String = "",
    val stan: String = "",
    val dateTime: String = "",
    val amount: String = "",
    val type: TransactionType,
    val cardPan: String ="",
    val cardType: Int = 0,
    val cardExpiry: String = "",
    val authorizationCode: String = "",
    var tid: String = "",
    var merchantId: String = "",
    var merchantName: String = "",
    var merchantLocation: String = "",
    val responseMessage: String = "",
    val responseCode: String = "",
    val AID: String = "",
    val code: String = "",
    val telephone: String = "",
    val txnDate: Long = 0L,
    val transactionId: String = "",
    val cardHolderName: String,
    val remoteResponseCode: String = "",
    val biller: String? = "",
    val customerDescription: String? = "",
    val surcharge: String? = "",
    val additionalAmounts: String? = "",
    var customerName: String? = "",
    var ref : String? = "",
    var accountNumber: String? = "",
    var transactionCurrencyCode: String = "566"
    //val additionalInfo: String? = ""
):Parcelable


enum class TransactionType {
    PayCode,
    Card,
    Cash,
    Transfer,
    QR,
    Ussd
}

enum class CardLessTransactionType {
    Transfer,
    QR,
    Ussd
}

enum class PaymentType(val value: Int) {
    PURCHASE(0),
    TRANSFER(0),
    CASHOUT(0)
}


/**
 * This enum type identifies the
 * different Bank Account types
 */
enum class AccountType(val value: String) {
    Default("00"),
    Savings("10"),
    Current("20"),
    Credit("30")
}



