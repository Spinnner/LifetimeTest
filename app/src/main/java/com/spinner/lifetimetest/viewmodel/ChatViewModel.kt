package com.spinner.lifetimetest.viewmodel


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.spinner.lifetimetest.R
import com.spinner.lifetimetest.helpers.runDelayed
import com.spinner.lifetimetest.model.MESSAGE_TYPE_BOT_MENU
import com.spinner.lifetimetest.model.MESSAGE_TYPE_BOT_TEXT
import com.spinner.lifetimetest.model.MESSAGE_TYPE_USER_TEXT
import com.spinner.lifetimetest.model.Message


class ChatViewModel(val app: Application) : AndroidViewModel(app) {

    private val message = MutableLiveData<Message>()
    private var isWaitingForUser = true
    private val DELAY_MESSAGE = 500L

    fun addInitialMessages() {
        val msg1 = Message(MESSAGE_TYPE_BOT_TEXT, app.getString(R.string.message_hello))
        val msg2 = Message(MESSAGE_TYPE_BOT_MENU)

        sendMessage(msg1)
        sendMessage(msg2, 1000L)
    }

    fun newBotMenuMessage(isWaitingForUser: Boolean) {
        this.isWaitingForUser = isWaitingForUser

        val msg = Message(MESSAGE_TYPE_BOT_MENU)
        sendMessage(msg, 800)
    }

    fun newUserTextMessage(text: String, isWaitingForUser: Boolean, delay: Long = DELAY_MESSAGE) {
        this.isWaitingForUser = isWaitingForUser
        val msg = Message(MESSAGE_TYPE_USER_TEXT, text)
        sendMessage(msg, delay)
    }

    fun newBotRandomTextMessage(isWaitingForUser: Boolean, delay: Long = DELAY_MESSAGE) {
        this.isWaitingForUser = isWaitingForUser
        val randomText = app.resources.getStringArray(R.array.random_strings).random()
        val msg = Message(MESSAGE_TYPE_BOT_TEXT, randomText)
        sendMessage(msg, delay)
    }

    private fun sendMessage(msg: Message, delay: Long = DELAY_MESSAGE) {
        runDelayed({
            message.value = msg
        }, delay)
    }

    fun getNewMessage(): LiveData<Message> = message

    fun isWaitingForUser() = isWaitingForUser

}