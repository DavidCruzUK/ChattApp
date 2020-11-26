package com.lastreact.android.chattapp.ui.channels

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.lastreact.android.chattapp.R
import com.lastreact.android.chattapp.base.BaseActivity
import com.lastreact.android.chattapp.data.model.Channel
import com.lastreact.android.chattapp.databinding.ActivityChannelsBinding
import com.lastreact.android.chattapp.ui.chat.ChatActivity
import com.lastreact.android.chattapp.ui.login.LoginActivity
import org.jetbrains.anko.*
import org.jetbrains.anko.design.longSnackbar

class ChannelsActivity : BaseActivity<ActivityChannelsBinding>(), AnkoLogger {

    private lateinit var channelsDialog: ChannelDialogFragment
    private lateinit var fireStore: FirebaseFirestore
    private lateinit var query: Query
    private lateinit var adapter: ChannelAdapter
    private lateinit var channelReference: DocumentReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        channelsDialog = ChannelDialogFragment()
        auth = FirebaseAuth.getInstance()
        initFireStore()
        initRecyclerView()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.logout -> {
                auth.signOut()
                startActivity(intentFor<LoginActivity>().clearTask().newTask())
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    override fun createViewBinding(): ActivityChannelsBinding =
        ActivityChannelsBinding.inflate(layoutInflater)


    fun showChannelDialog(view: View) {
        channelsDialog.show(supportFragmentManager, ChannelDialogFragment.TAG)
    }

    private fun initRecyclerView() {
        adapter = object : ChannelAdapter(query, this@ChannelsActivity::channelListener) {
            override fun onError(e: FirebaseFirestoreException?) {
                binding.root.longSnackbar(getString(R.string.network_error))
                debug("FireStoreException: ${e?.message}")
            }

            override fun onDataChanged() {
                if (itemCount == 0) {
                    binding.emptyView.visibility = View.VISIBLE
                    binding.channelsRecyclerView.visibility = View.GONE
                } else {
                    binding.emptyView.visibility = View.GONE
                    binding.channelsRecyclerView.visibility = View.VISIBLE
                }
                binding.channelsRecyclerView.layoutManager =
                    LinearLayoutManager(this@ChannelsActivity)
                binding.channelsRecyclerView.adapter = adapter
            }
        }
    }

    private fun channelListener(channel: Channel?) {
        channel?.let {
            startActivity(
                intentFor<ChatActivity>(
                    ChatActivity.KEY_CHANNEL_ID to it.id,
                    ChatActivity.KEY_CHANNEL_NAME to it.name
                )
            )
        }
    }

    private fun initFireStore() {
        fireStore = FirebaseFirestore.getInstance()
        channelReference = fireStore.collection(CHANNELS).document()
        query = fireStore.collection(CHANNELS).orderBy(NAME, Query.Direction.DESCENDING)
    }

    companion object {
        private const val CHANNELS = "channels"
        private const val NAME = "name"
    }
}