package com.lastreact.android.chattapp.ui.channels

import android.os.Bundle
import android.view.View
import com.lastreact.android.chattapp.base.BaseActivity
import com.lastreact.android.chattapp.databinding.ActivityChannelsBinding

class ChannelsActivity : BaseActivity<ActivityChannelsBinding>() {

    private lateinit var channelsDialog: ChannelDialogFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        channelsDialog = ChannelDialogFragment()
    }

    override fun createViewBinding(): ActivityChannelsBinding =
        ActivityChannelsBinding.inflate(layoutInflater)


    fun showChannelDialog(view: View) {
        channelsDialog.show(supportFragmentManager, ChannelDialogFragment.TAG)
    }
}