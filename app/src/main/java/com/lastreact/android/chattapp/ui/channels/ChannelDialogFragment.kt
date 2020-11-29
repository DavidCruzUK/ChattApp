package com.lastreact.android.chattapp.ui.channels

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lastreact.android.chattapp.base.BaseDialogFragment
import com.lastreact.android.chattapp.data.model.Channel
import com.lastreact.android.chattapp.databinding.DialogChannelBinding
import java.lang.ref.WeakReference

class ChannelDialogFragment : BaseDialogFragment<DialogChannelBinding>(), View.OnClickListener {

    companion object {
        const val TAG = "ChannelDialogFragment"
    }

    private lateinit var listener: WeakReference<ChannelsActivity>

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.createChannelButton.setOnClickListener(this)
        binding.cancelChannelButton.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            binding.createChannelButton.id -> onCreateClicked()
            binding.cancelChannelButton.id -> onCancelClicked()
            else -> Unit
        }
    }

    override fun onResume() {
        super.onResume()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
        ) ?: Log.e(TAG, "$TAG::onResume dialog or window is null")
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is ChannelsActivity) {
            listener = WeakReference(context)
        }
    }

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogChannelBinding = DialogChannelBinding.inflate(inflater, container, false)

    private fun onCreateClicked() {
        val channel = Channel(
            null,
            binding.channelNameEditText.text.toString().trim(),
            binding.channelDescriptionEditText.text.toString().trim(),
        )
        binding.channelNameEditText.text.clear()
        binding.channelDescriptionEditText.text.clear()
        listener.get()?.onChannel(channel)
        dismiss()
    }

    private fun onCancelClicked() {
        binding.channelNameEditText.text.clear()
        binding.channelDescriptionEditText.text.clear()
        dismiss()
    }

}