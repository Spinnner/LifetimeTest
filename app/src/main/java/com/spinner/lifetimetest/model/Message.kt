package com.spinner.lifetimetest.model

import androidx.annotation.IdRes

data class Message(val type: Int, val text: String? = null, @IdRes val icon: Int? = null)

const val MESSAGE_TYPE_USER_TEXT = 0
const val MESSAGE_TYPE_USER_MENU = 1
const val MESSAGE_TYPE_BOT_TEXT = 2
const val MESSAGE_TYPE_BOT_MENU = 3