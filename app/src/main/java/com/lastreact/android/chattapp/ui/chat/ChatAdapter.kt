package com.lastreact.android.chattapp.ui.chat

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.lastreact.android.chattapp.R
import com.lastreact.android.chattapp.data.model.Message
import com.lastreact.android.chattapp.databinding.MessageItemBinding
import java.text.SimpleDateFormat
import java.util.*

open class ChatAdapter(query: Query) : FireStoreAdapter<ChatAdapter.ViewHolder>(query) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position).toObject(Message::class.java))
    }

    inner class ViewHolder(private val binding: MessageItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(message: Message?) = with(binding) {
            message?.let {
                val pattern = this.root.context.getString(R.string.date_format)
                val simpleDateFormat = SimpleDateFormat(pattern, Locale.getDefault())
                val date: String = simpleDateFormat.format(it.timestamp)

                binding.messageTextView.text = it.text
                binding.timestampTextView.text = date
                binding.senderTextView.text = it.userName
            }
        }
    }

}