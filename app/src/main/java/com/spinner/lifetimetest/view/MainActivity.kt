package com.spinner.lifetimetest.view

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.spinner.lifetimetest.viewmodel.ChatViewModel
import com.spinner.lifetimetest.R
import com.spinner.lifetimetest.callback.BotMenuMessageClickListener
import com.spinner.lifetimetest.helpers.*
import com.spinner.lifetimetest.model.MESSAGE_TYPE_BOT_TEXT
import com.spinner.lifetimetest.model.MESSAGE_TYPE_USER_TEXT
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.messages_menu_user.*
import org.jetbrains.anko.dip
import java.util.*


class MainActivity : AppCompatActivity(),
    BotMenuMessageClickListener {

    private val vm by lazy {
        ChatViewModel(
            application
        )
    }
    private val chatAdapter by lazy {
        RvChatMessage(
            this
        )
    }
    private val animBottomUp by lazy { AnimationUtils.loadAnimation(this,
        R.anim.bottom_up
    ) }
    private val animBottomDown by lazy { AnimationUtils.loadAnimation(this,
        R.anim.bottom_down
    ) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setViews()
        setClickListeners()
        setObservers()
        loadInitialData()
    }

    private fun setViews() {
        rvChat.adapter = chatAdapter

        edMessage.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(p0: Editable) {
                ivSendMessage.alpha = if (p0.isBlank()) 0.5f else 1f
                ivSendMessage.isEnabled = p0.isNotBlank()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

        layoutMain.viewTreeObserver.addOnGlobalLayoutListener {
            layoutMain?.let {
                val heightDiff: Int = layoutMain.rootView.height - layoutMain.height
                if (heightDiff > dip(200)) {
                    scrollChatToEnd()
                }
            }
        }
    }

    private fun setClickListeners() {
        ivSendMessage.setOnClickListener {
            if (edMessage.text.isNotBlank()) {
                hideKeyboard()
                runDelayed({
                    vm.newUserTextMessage(edMessage.text.toString(), false)
                    edMessage.text.clear()
                }, 200)
            }
        }

        listOf<TextView>(tvMessageBtn1, tvMessageBtn2, tvMessageBtn3).onEach {
            it.setOnClickListener {
                layoutMessagesMenu.animateAndHide(animBottomDown)
                layoutEditText.show()

                val text = (it as TextView).text.toString()
                vm.newUserTextMessage(text, false)
            }
        }
    }

    private fun loadInitialData() {
        vm.addInitialMessages()
    }

    private fun setObservers() {
        vm.getNewMessage().nonNullObserve(this) {
            val isFirstMessage = chatAdapter.itemCount == 0
            chatAdapter.addMessage(it)
            scrollChatToEnd()

            if (isFirstMessage.not() && vm.isWaitingForUser() && it.type == MESSAGE_TYPE_BOT_TEXT) {
                showKeyboardOrMessagesMenu()
            } else if (isFirstMessage.not() && vm.isWaitingForUser().not() && it.type == MESSAGE_TYPE_USER_TEXT) {
                showRandomBotMessage()
            }
        }
    }

    private fun showKeyboardOrMessagesMenu() {
        if (Random().nextBoolean()) {
            runDelayed({
                layoutEditText.hide()
                layoutMessagesMenu.show()
                layoutMessagesMenu.startAnimation(animBottomUp)
                rvChat.smoothScrollToPosition(chatAdapter.itemCount)
            }, 600)
        } else edMessage.showKeyboard(this)
    }

    private fun showRandomBotMessage() {
        if (Random().nextBoolean()) vm.newBotMenuMessage(true)
        else vm.newBotRandomTextMessage(true)
    }

    private fun scrollChatToEnd() {
        rvChat.smoothScrollToPosition(chatAdapter.itemCount - 1)
    }

    override fun onBotMenuMessageClicked(text: String) {
        vm.newUserTextMessage(text, true)
        vm.newBotRandomTextMessage(true, 1300L)
    }
}
