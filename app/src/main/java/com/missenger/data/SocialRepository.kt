package com.missenger.data

import android.content.SharedPreferences
import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SocialRepository {
    private val client = OkHttpClient()

    private lateinit var prefs: Prefs

    data class UserData (
        var id: Int,
        var username: String,
        var password: String,
    )

    var loggedUser : UserData = UserData(1, "", "")
//    private var ID : Int = 0

    fun initPrefs(pref: SharedPreferences) {
        prefs = Prefs(pref)
//        prefs.putIdToPrefs(1)
        val id = if (prefs.getLoggedId() == 0)  {1} else {
            prefs.getLoggedId()
        }
//        prefs.putIdToPrefs(1)
//        ID = id
        loggedUser = UserData(id, "", "")
    }
    suspend fun regUser (
        username: String,
        password: String,
        lastname: String,
        firstname: String,
    ) : Pair<Int, Int> {
        data class RegModel (
            val username: String = "",
            val password: String = "",
            val last_name: String = "",
            val first_name: String = "",
        )

        val gson = GsonBuilder().create()
        val message = gson.toJson(RegModel(username, password, lastname, firstname))

        val JSON = "application/json; charset=utf-8".toMediaType()
        val request = Request.Builder()
            .url(REGISTRATION)
            .post(
                message.toRequestBody(JSON)
            )
            .build()
        return try {
            val response = client.newCall(request).execute()
            val item = JSONObject(response.body!!.string())
            val result = item.getInt("id")
            Pair(response.code, result)
        } catch (ex: Exception) {
            Log.e("OK_HTTP", ex.message ?: "")
            Pair(0, 0)
        }
    }
    suspend fun getUserInfo (
        name: String
    ) : Pair<Int, UserInfo?> {
        val request = Request.Builder()
            .url(GET_USER_INFO + name)
            .method("GET", null)
            .build()
        return try {
            val response = client.newCall(request).execute()
            val json = JSONObject(response.body!!.string())
            Pair (response.code, UserInfo(
                id = json.getInt("id"),
                username = json.getString("username"),
                firstname = json.getString("first_name"),
                lastname = json.getString("last_name"),
            ))
        } catch (ex: Exception) {
            Log.e("OK_HTTP", ex.message ?: "")
            Pair(0, null)
        }
    }

    suspend fun getLastMessages (
        id: Int = loggedUser.id,
    ) : Pair<Int, List<MessageModel>?> {

        data class IdModel (
            val user_id: Int,
        )
        val gson = GsonBuilder().create()
        val message = gson.toJson(IdModel(user_id = id))

        val JSON = "application/json; charset=utf-8".toMediaType()
        val request = Request.Builder()
            .url(GET_LAST_MSG)
            .post(
                message.toRequestBody(JSON)
            )
            .build()
        return try {
            val response = client.newCall(request).execute()
            val list = JSONObject(response.body!!.string()).getJSONArray("last_messages")
            val resultList = mutableListOf<MessageModel>()
            // adding
            if (list is JSONArray) {
                (0 until list.length()).forEach {
                    val item = list.getJSONObject(it)
                    val from = item.getJSONObject("from")
                    val to = item.getJSONObject("to")
                    resultList.add(
                        MessageModel(
                            item.getInt("id"),
                            loggedUser.id,
                            item.getString("message"),
                            UserInfo(
                                from.getInt("id"),
                                from.getString("username"),
                                from.getString("last_name"),
                                from.getString("first_name"),
                            ),
                            UserInfo(
                                to.getInt("id"),
                                to.getString("username"),
                                to.getString("last_name"),
                                to.getString("first_name"),
                            ),
                            parseDate(item.getString("datetime")),
                        )
                    )
                }
            }
            Pair (response.code, resultList)
        } catch (ex: Exception) {
            Log.e("OK_HTTP", ex.message ?: "")
            Pair(0, null)
        }
    }

    suspend fun getMessages (
        userId: Int = loggedUser.id,
        friendId: Int,
    ) : Pair<Int, List<MessageModel>?> {
        val gson = GsonBuilder().create()
        val message = gson.toJson(UserMessagesModel(userId, friendId))

        val JSON = "application/json; charset=utf-8".toMediaType()
        val request = Request.Builder()
            .url(GET_USER_MSG)
            .post(
                message.toRequestBody(JSON)
            )
            .build()

        return try {
            val response = client.newCall(request).execute()
            val list = JSONObject(response.body!!.string()).getJSONArray("messages_list")
            val resultList = mutableListOf<MessageModel>()
            // adding
            if (list is JSONArray) {
                (0 until list.length()).forEach {
                    val item = list.getJSONObject(it)
                    val from = item.getJSONObject("from")
                    val to = item.getJSONObject("to")
                    resultList.add(
                        MessageModel(
                            item.getInt("id"),
                            loggedUser.id,
                            item.getString("message"),
                            UserInfo(
                                from.getInt("id"),
                                from.getString("username"),
                                from.getString("last_name"),
                                from.getString("first_name"),
                            ),
                            UserInfo(
                                to.getInt("id"),
                                to.getString("username"),
                                to.getString("last_name"),
                                to.getString("first_name"),
                            ),
                            parseDate(item.getString("datetime")),
                        )
                    )
                }
            }
            Pair (response.code, resultList)
        } catch (ex: Exception) {
            Log.e("OK_HTTP", ex.message ?: "")
            Pair(0, null)
        }
    }

    suspend fun sendMsg (
        userId: Int = loggedUser.id,
        friendId: Int,
        message: String,
    ) : Int {
        data class SendModel (
            val from: Int,
            val to: Int,
            val message: String,
        )

        val gson = GsonBuilder().create()
        val sender = gson.toJson(SendModel(from = userId, to = friendId, message = message))

        val JSON = "application/json; charset=utf-8".toMediaType()
        val request = Request.Builder()
            .url(SEND_MSG)
            .post(
                sender.toRequestBody(JSON)
            )
            .build()
        return try {
            val response = client.newCall(request).execute()
            response.code
        } catch (ex: Exception) {
            Log.e("OK_HTTP", ex.message ?: "")
            0
        }
    }

    suspend fun searchUser(
        searchString: String
    ) : Pair<Int, List<UserInfo>?> {
        data class SearchModel (
            val search_string: String,
        )

        val gson = GsonBuilder().create()
        val message = gson.toJson(SearchModel(searchString))

        val JSON = "application/json; charset=utf-8".toMediaType()
        val request = Request.Builder()
            .url(SEARCH_USER)
            .post(
                message.toRequestBody(JSON)
            )
            .build()

        return try {
            val response = client.newCall(request).execute()
            val list = JSONObject(response.body!!.string()).getJSONArray("search_users")
            val resultList = mutableListOf<UserInfo>()
            if (list is JSONArray) {
                (0 until list.length()).forEach {
                    val item = list.getJSONObject(it)
                    resultList.add(
                        UserInfo(
                            item.getInt("id"),
                            item.getString("username"),
                            item.getString("last_name"),
                            item.getString("first_name"),
                        )
                    )
                }
            }
            Pair (response.code, resultList)
        } catch (ex: Exception) {
            Log.e("OK_HTTP", ex.message ?: "")
            Pair(0, null)
        }
    }

    private fun parseDate(date: String): LocalDateTime {
        return LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSSSSS"))
    }

    companion object {
        private const val LOGIN = "http://109.196.164.62:5000/login/" // POST
        private const val REGISTRATION = "http://109.196.164.62:5000/register/" // POST

        private const val SEARCH_USER = "http://109.196.164.62:5000/search" // POST
        private const val GET_USER_INFO = "http://109.196.164.62:5000/user/" // GET

        private const val SEND_MSG = "http://109.196.164.62:5000/send" // POST
        private const val GET_LAST_MSG = "http://109.196.164.62:5000/message" // POST
        private const val GET_USER_MSG = "http://109.196.164.62:5000/user-message" // POST
    }
}