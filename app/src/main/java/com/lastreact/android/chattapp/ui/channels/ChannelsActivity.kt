package com.lastreact.android.chattapp.ui.channels

import android.view.View
import com.lastreact.android.chattapp.base.BaseActivity
import com.lastreact.android.chattapp.databinding.ActivityChannelsBinding

class ChannelsActivity : BaseActivity<ActivityChannelsBinding>() {

    override fun createViewBinding(): ActivityChannelsBinding =
        ActivityChannelsBinding.inflate(layoutInflater)


    fun showChannelDialog(view: View) {

    }
}