package com.lastreact.android.chattapp.ui.chat

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.lastreact.android.chattapp.R
import com.lastreact.android.chattapp.base.BaseActivity
import com.lastreact.android.chattapp.databinding.ActivityChatBinding
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.design.longSnackbar

class ChatActivity : BaseActivity<ActivityChatBinding>(), AnkoLogger {

    private lateinit var fireStore: FirebaseFirestore
    private lateinit var query: Query
    private lateinit var adapter: ChatAdapter
    private lateinit var channelReference: DocumentReference
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        val channelId = intent.extras?.getString(KEY_CHANNEL_ID)
            ?: throw IllegalArgumentException("ChatActivity::channelId is null")
        title = intent.extras?.getString(KEY_CHANNEL_NAME)
            ?: throw IllegalArgumentException("ChatActivity::title is null")
        initFireStore(channelId)
        initRecyclerView()
    }

    override fun onStart() {
        super.onStart()
        adapter.startListening()
    }

    override fun onStop() {
        super.onStop()
        adapter.stopListening()
    }

    private fun initFireStore(channelId: String) {
        fireStore = FirebaseFirestore.getInstance()
        channelReference = fireStore.collection(CHANNEL_COLLECTION).document(channelId)
        query = channelReference
            .collection(MESSAGE_COLLECTION)
            .orderBy(TIMESTAMP, Query.Direction.ASCENDING)
    }

    private fun initRecyclerView() {
        adapter = object : ChatAdapter(query) {
            override fun onError(e: FirebaseFirestoreException?) {
                binding.root.longSnackbar(getString(R.string.network_error))
                debug("FireStoreExemption: ${e?.message}")
            }

            override fun onDataChanged() {
                if (itemCount == 0) {
                    binding.chatEmptyView.visibility = View.VISIBLE
                    binding.chatRecyclerView.visibility = View.GONE
                } else {
                    binding.chatEmptyView.visibility = View.GONE
                    binding.chatRecyclerView.visibility = View.VISIBLE
                }
                binding.chatRecyclerView.layoutManager =
                    LinearLayoutManager(this@ChatActivity).apply {
                        stackFromEnd = true
                    }
                binding.chatRecyclerView.adapter = adapter
            }

        }
    }

    override fun createViewBinding(): ActivityChatBinding =
        ActivityChatBinding.inflate(layoutInflater)

    fun sendClicked(view: View) {

    }

    companion object {
        private const val LIMIT = 50
        const val KEY_CHANNEL_ID = "key_restaurant_id"
        const val KEY_CHANNEL_NAME = "key_channel_name"
        const val MESSAGE_COLLECTION = "messages"
        const val CHANNEL_COLLECTION = "channels"
        const val TIMESTAMP = "timestamp"
    }
}