package com.lovisgod.iswhpay.utils

object StringManipulator{
    fun dropLastCharacter(values: String): String {
        if (values.isNotEmpty()) {
            val xxx = values.dropLast(1)
            return  xxx
        } else {
            return values
        }
    }


    fun dropFirstCharacter(values: String): String {
        if (values.isNotEmpty()) {
            val xxx = values.drop(4)
            println("this is usable string : ${xxx}")
            return xxx
        } else {
            return values
        }

    }
}