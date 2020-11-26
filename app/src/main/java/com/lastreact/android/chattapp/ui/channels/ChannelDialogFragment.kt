package com.lastreact.android.chattapp.ui.channels

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.lastreact.android.chattapp.base.BaseDialogFragment
import com.lastreact.android.chattapp.databinding.DialogChannelBinding

class ChannelDialogFragment : BaseDialogFragment<DialogChannelBinding>(), View.OnClickListener {

    companion object {
        const val TAG = "ChannelDialogFragment"
    }

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

    override fun createViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): DialogChannelBinding = DialogChannelBinding.inflate(inflater, container, false)

    private fun onCreateClicked() {
        // TODO: implementation create channel
    }

    private fun onCancelClicked() {
        binding.channelNameEditText.text.clear()
        binding.channelDescriptionEditText.text.clear()
        dismiss()
    }

}