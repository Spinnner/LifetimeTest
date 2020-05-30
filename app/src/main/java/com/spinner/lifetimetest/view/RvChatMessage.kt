package com.spinner.lifetimetest.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.spinner.lifetimetest.R
import com.spinner.lifetimetest.callback.BotMenuMessageClickListener
import com.spinner.lifetimetest.model.MESSAGE_TYPE_BOT_MENU
import com.spinner.lifetimetest.model.MESSAGE_TYPE_BOT_TEXT
import com.spinner.lifetimetest.model.MESSAGE_TYPE_USER_TEXT
import com.spinner.lifetimetest.model.Message
import kotlinx.android.synthetic.main.messages_menu_bot.view.*
import kotlinx.android.synthetic.main.messages_menu_bot_item.view.*
import kotlinx.android.synthetic.main.text_message_bot.view.*
import kotlinx.android.synthetic.main.text_message_user.view.*


class RvChatMessage(val listener: BotMenuMessageClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val listMessages = mutableListOf<Message>()

    inner class UserTextMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.tvMessageUser
    }

    inner class BotTextMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvMessage: TextView = itemView.tvMessageBot
    }

    inner class BotMenuMessageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val layoutMessagesMenu: LinearLayout = itemView.layoutMessagesMenuBot
        private val inflater: LayoutInflater = LayoutInflater.from(itemView.context)

        init {
            val viewMessage1 =
                inflater.inflate(R.layout.messages_menu_bot_item, layoutMessagesMenu, false)
            viewMessage1.ivIcon.setImageResource(R.drawable.ic_bottle)
            viewMessage1.tvMessageBotItem.setText(R.string.message_menu_1)

            val viewMessage2 =
                inflater.inflate(R.layout.messages_menu_bot_item, layoutMessagesMenu, false)
            viewMessage2.ivIcon.setImageResource(R.drawable.ic_upload)
            viewMessage2.tvMessageBotItem.setText(R.string.message_menu_2)

            val viewMessage3 =
                inflater.inflate(R.layout.messages_menu_bot_item, layoutMessagesMenu, false)
            viewMessage3.ivIcon.setImageResource(R.drawable.ic_note)
            viewMessage3.tvMessageBotItem.setText(R.string.message_menu_3)

            val viewMessage4 =
                inflater.inflate(R.layout.messages_menu_bot_item, layoutMessagesMenu, false)
            viewMessage4.ivIcon.setImageResource(R.drawable.ic_support)
            viewMessage4.tvMessageBotItem.setText(R.string.message_menu_4)

            listOf<View>(viewMessage1, viewMessage2, viewMessage3, viewMessage4).onEach {
                it.setOnClickListener {
                    listener.onBotMenuMessageClicked(it.tvMessageBotItem.text.toString())
                }
            }

            layoutMessagesMenu.apply {
                addView(viewMessage1)
                addView(viewMessage2)
                addView(viewMessage3)
                addView(viewMessage4)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            MESSAGE_TYPE_USER_TEXT -> UserTextMessageViewHolder(
                inflater.inflate(
                    R.layout.text_message_user,
                    parent,
                    false
                )
            )
            MESSAGE_TYPE_BOT_TEXT -> BotTextMessageViewHolder(
                inflater.inflate(
                    R.layout.text_message_bot,
                    parent,
                    false
                )
            )
            MESSAGE_TYPE_BOT_MENU -> BotMenuMessageViewHolder(
                inflater.inflate(
                    R.layout.messages_menu_bot,
                    parent,
                    false
                )
            )
            else -> throw IllegalArgumentException()
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            MESSAGE_TYPE_USER_TEXT -> onBindUserTextMessage(holder, listMessages[position])
            MESSAGE_TYPE_BOT_TEXT -> onBindBotTextMessage(holder, listMessages[position])
            MESSAGE_TYPE_BOT_MENU -> onBindBotMenuMessage(holder, listMessages[position])
            else -> throw IllegalArgumentException()
        }
    }

    private fun onBindUserTextMessage(holder: RecyclerView.ViewHolder, message: Message) {
        (holder as UserTextMessageViewHolder).tvMessage.text = message.text
    }

    private fun onBindBotTextMessage(holder: RecyclerView.ViewHolder, message: Message) {
        (holder as BotTextMessageViewHolder).tvMessage.text = message.text
    }

    private fun onBindBotMenuMessage(holder: RecyclerView.ViewHolder, message: Message) {

    }

    override fun getItemCount() = listMessages.size

    override fun getItemViewType(position: Int) = listMessages[position].type


    fun addMessage(message: Message) {
        listMessages.add(message)
        notifyItemInserted(itemCount-1)
    }
}