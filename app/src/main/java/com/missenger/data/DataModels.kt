package com.missenger.data

import java.time.LocalDateTime

data class UserInfo (
    val id:         Int = 0,
    val username:   String = "",
    val firstname:  String = "",
    val lastname:   String = "",
)

data class MessageModel (
    val id:       Int = 0,
    val logged:   Int = 0,
    val message:  String = "",
    val from:     UserInfo = UserInfo(),
    val to:       UserInfo = UserInfo(),
    val datetime: LocalDateTime,
)

data class UserMessagesModel (
    val user_id : Int = 0,
    val friend_id: Int = 0,
)