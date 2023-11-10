package com.lovisgod.iswhpay.utils

object CardManipulatorUtil {

    fun getCardType(cardNumber: String): String {

            // Remove any spaces and non-digit characters
            val cleanedCardNumber = cardNumber.replace("\\D".toRegex(), "")

            // Define regular expressions for different card types
            val PATTERN_VISA = "^4[0-9]{12}(?:[0-9]{3})?$".toRegex()
            val PATTERN_MASTERCARD = "^(?:5[1-5][0-9]{2}|222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720)[0-9]{12}$".toRegex()
            val PATTERN_AMERICAN_EXPRESS = "^3[47][0-9]{13}$".toRegex()
            val PATTERN_DINERS_CLUB = "^3(?:0[0-5]|[68][0-9])[0-9]{11}$".toRegex()
            val PATTERN_DISCOVER = "^6(?:011|5[0-9]{2})[0-9]{12}$".toRegex()
            val PATTERN_JCB = "^(?:2131|1800|35[0-9]{3})[0-9]{11}$".toRegex()
            val PATTERN_VERVE = "^((506(0|1))|(507(8|9))|(6500))[0-9]{12,15}$".toRegex()

            // Match the card number against the patterns
            return when {
                PATTERN_VISA.matches(cleanedCardNumber) -> "Visa"
                PATTERN_MASTERCARD.matches(cleanedCardNumber) -> "MasterCard"
                PATTERN_AMERICAN_EXPRESS.matches(cleanedCardNumber) -> "American Express"
                PATTERN_DISCOVER.matches(cleanedCardNumber) -> "Discover"
                PATTERN_VERVE.matches(cleanedCardNumber) -> "Verve"
                else -> "Unknown Card Type"
            }
    }

}