package com.lastreact.android.chattapp.base

import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.*

abstract class FireStoreAdapter<VH : RecyclerView.ViewHolder?>(private var query: Query) :
    RecyclerView.Adapter<VH>(), EventListener<QuerySnapshot> {

    private var registration: ListenerRegistration? = null
    private val snapshot = ArrayList<DocumentSnapshot>()

    companion object {
        const val TAG = "FireStoreAdapter"
    }

    fun startListening() {
        if (registration == null) {
            registration = query.addSnapshotListener(this)
        }
    }

    fun stopListening() {
        if (registration != null) {
            registration?.remove()
            registration = null
        }
        snapshot.clear()
        notifyDataSetChanged()
    }

    private fun onDocumentAdded(change: DocumentChange) {
        snapshot.add(change.newIndex, change.document)
        notifyItemInserted(change.newIndex)
    }

    private fun onDocumentModified(change: DocumentChange) {
        if (change.oldIndex == change.newIndex) {
            snapshot[change.oldIndex] = change.document
            notifyItemChanged(change.oldIndex)
        } else {
            snapshot.removeAt(change.oldIndex)
            snapshot.add(change.newIndex, change.document)
            notifyItemMoved(change.oldIndex, change.newIndex)
        }
    }

    private fun onDocumentRemoved(change: DocumentChange) {
        snapshot.removeAt(change.oldIndex)
        notifyItemRemoved(change.oldIndex)
    }

    override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
        if (error != null) {
            Log.e(TAG, "onEvent Error: ", error)
            return
        }

        if (value != null) {
            for (change in value.documentChanges) {
                when (change.type) {
                    DocumentChange.Type.ADDED -> onDocumentAdded(change)
                    DocumentChange.Type.MODIFIED -> onDocumentModified(change)
                    DocumentChange.Type.REMOVED -> onDocumentRemoved(change)
                }
            }
        }
        onDataChanged()
    }

    protected open fun onError(e: FirebaseFirestoreException?) {}
    protected open fun onDataChanged() {}

    override fun getItemCount(): Int {
        Log.d(TAG, "getItemCount: ${snapshot.size}")
        return snapshot.size
    }

    protected fun getSnapshot(index: Int): DocumentSnapshot {
        return snapshot[index]
    }

}