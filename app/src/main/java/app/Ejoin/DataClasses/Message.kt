package app.Ejoin.DataClasses

import com.google.firebase.Timestamp
import java.util.*

data class Message(
    var message: String = "",
    var from: String = "",
    var to: String="",
    var time: Timestamp? =null
)
