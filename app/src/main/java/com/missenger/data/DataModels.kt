package com.missenger.data

import java.time.LocalDateTime

data class UserInfo (
    val id:         Int = 0,
    val username:   String = "",
    val firstname:  String = "",
    val lastname:   String = "",
)
data class UserData (
    var id: Int,
    var username: String,
    var password: String,
) {}
data class RegUserModel (
    val username:   String = "",
    val password:   String = "",
    val firstname:  String = "",
    val lastname:   String = "",
)
data class LogUserModel (
    val username:   String = "",
    val password:   String = "",
)

data class MessageModel (
    val id:       Int = 0,
    val logged:   Int = 0,
    val message:  String = "",
    val from:     UserInfo = UserInfo(),
    val to:       UserInfo = UserInfo(),
    val datetime: LocalDateTime,
)