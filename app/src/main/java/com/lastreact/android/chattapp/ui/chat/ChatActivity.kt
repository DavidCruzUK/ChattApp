package com.lastreact.android.chattapp.ui.chat

import com.lastreact.android.chattapp.base.BaseActivity
import com.lastreact.android.chattapp.databinding.ActivityChatBinding

class ChatActivity : BaseActivity<ActivityChatBinding>() {
    override fun createViewBinding(): ActivityChatBinding =
        ActivityChatBinding.inflate(layoutInflater)
}