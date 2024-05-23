package com.baltajmn.sleeper.common

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

fun String.isNumericOrBlank(): Boolean =
    this.matches("-?[0-9]+(\\.[0-9]+)?".toRegex()) || this.isBlank()