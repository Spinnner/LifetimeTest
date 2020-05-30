package com.spinner.lifetimetest.helpers

import android.os.Handler

fun runDelayed(code: () -> Unit, delayMillis: Long) {
    Handler().postDelayed(code, delayMillis)
}