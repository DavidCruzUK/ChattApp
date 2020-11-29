package com.lastreact.android.chattapp.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

@IgnoreExtraProperties
class Message {
    var id: String? = null
    var text: String? = null
    var userName: String? = null

    @ServerTimestamp
    var timestamp: Date? = null

    constructor()
    constructor(id: String?, text: String?, userName: String?, timestamp: Date?) {
        this.id = id
        this.text = text
        this.userName = userName
        this.timestamp = timestamp
    }
}
