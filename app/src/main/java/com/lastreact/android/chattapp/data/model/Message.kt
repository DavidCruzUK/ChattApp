package com.lastreact.android.chattapp.data.model

import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.firebase.firestore.ServerTimestamp
import java.util.*

@IgnoreExtraProperties
class Message {
    var userId: String? = null
    var text: String? = null
    var userName: String? = null

    @ServerTimestamp
    var timestamp: Date? = null

    constructor()
    constructor(userId: String?, text: String?, userName: String?, timestamp: Date?) {
        this.userId = userId
        this.text = text
        this.userName = userName
        this.timestamp = timestamp
    }
}
