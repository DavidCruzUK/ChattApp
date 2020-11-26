package com.lastreact.android.chattapp.ui.chat

import android.os.Bundle
import android.view.View
import com.lastreact.android.chattapp.base.BaseActivity
import com.lastreact.android.chattapp.databinding.ActivityChatBinding

class ChatActivity : BaseActivity<ActivityChatBinding>() {

    override fun createViewBinding(): ActivityChatBinding =
        ActivityChatBinding.inflate(layoutInflater)

    fun sendClicked(view: View) {

    }
}