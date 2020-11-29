package com.lastreact.android.chattapp.ui.chat

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.lastreact.android.chattapp.R
import com.lastreact.android.chattapp.base.BaseActivity
import com.lastreact.android.chattapp.data.model.Message
import com.lastreact.android.chattapp.databinding.ActivityChatBinding
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.debug
import org.jetbrains.anko.design.longSnackbar
import java.util.*

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

    private fun addMessage(text: String): Task<Void> {
        val chatReference = channelReference
            .collection(MESSAGE_COLLECTION)
            .document()
        val currentUser = auth.currentUser
        val message = Message(currentUser?.uid ?: "", text.trim(), currentUser?.displayName ?: "", null)
        return fireStore.runTransaction { transition ->
            transition[chatReference] = message
            null
        }
    }

    fun sendClicked(view: View) {
        if (binding.messageEditText.text.isNullOrBlank()) {
            binding.root.longSnackbar("Text cannot be null")
            return
        }
        onSendMessageButtonState(false)
        addMessage(binding.messageEditText.text.toString())
            .addOnSuccessListener(this) {
                reset()
                onSendMessageButtonState(true)
            }
            .addOnFailureListener(this) {
                binding.root.longSnackbar(getString(R.string.network_error))
                debug("FireStoreException: ${it.message}")
                onSendMessageButtonState(true)
            }
    }

    private fun onSendMessageButtonState(isAvailable: Boolean) {
        binding.sendButton.isEnabled = isAvailable
        binding.sendButton.isClickable = isAvailable
    }

    private fun reset() {
        binding.messageEditText.text.clear()
        binding.chatRecyclerView.adapter?.let {
            binding.chatRecyclerView.smoothScrollToPosition(it.itemCount - 1)
        }
    }

    companion object {
        const val KEY_CHANNEL_ID = "key_channel_id"
        const val KEY_CHANNEL_NAME = "key_channel_name"
        const val MESSAGE_COLLECTION = "messages"
        const val CHANNEL_COLLECTION = "channels"
        const val TIMESTAMP = "timestamp"
    }
}