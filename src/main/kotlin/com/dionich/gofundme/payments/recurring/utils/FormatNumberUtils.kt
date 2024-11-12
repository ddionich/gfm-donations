package com.dionich.gofundme.payments.recurring.utils

fun Double.format(): String = String.format("%s", if (this % 1.0 == 0.0) "%.0f".format(this) else "%.2f".format(this))