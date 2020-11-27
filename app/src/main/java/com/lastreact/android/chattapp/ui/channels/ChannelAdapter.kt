package com.lastreact.android.chattapp.ui.channels

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.Query
import com.lastreact.android.chattapp.data.model.Channel
import com.lastreact.android.chattapp.databinding.ChannelItemBinding
import com.lastreact.android.chattapp.base.FireStoreAdapter
import java.util.*

open class ChannelAdapter(query: Query, private val listener: (channel: Channel?) -> Unit) :
    FireStoreAdapter<ChannelAdapter.ViewHolder>(query) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChannelItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getSnapshot(position).toObject(Channel::class.java), listener)
    }

    inner class ViewHolder(private val binding: ChannelItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(channel: Channel?, listener: (channel: Channel?) -> Unit) = with(binding) {
            channel?.let {
                channelNameTextView.text = it.name
                descriptionTextView.text = it.description
                channelIcon.setColorFilter(getRandomColor(),
                    android.graphics.PorterDuff.Mode.SRC_IN)
            }
            this.root.setOnClickListener { listener(channel) }
        }
    }

    private fun getRandomColor(): Int {
        val rnd = Random()
        return Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256))
    }
}