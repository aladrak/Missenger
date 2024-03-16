package com.missenger.data

import android.content.SharedPreferences
import com.google.gson.GsonBuilder

class Prefs (pref: SharedPreferences) {
    private val prefs = pref
    private val gson = GsonBuilder().create()
    fun lastLogToPrefs(item: UserData) {
        val serializable = gson.toJson(item)
        prefs
            .edit()
            .putString(LOG_PREFS_KEY, serializable)
            .apply()
    }

//    fun secondListToPrefs(list: List<ListItemViewModel>) {
//        val serializableSet = list.map {
//            Json.encodeToString(it)
//        }.toSet()
//        prefs
//            .edit()
//            .putStringSet(SECOND_LIST_KEY, serializableSet)
//            .apply()
//    }

    fun getLoggedUser(): UserData {
        val item = prefs.getString(LOG_PREFS_KEY, "{\"id\":-1,\"password\":\"\",\"username\":\"\"}")
        return gson.fromJson(item, UserData::class.java)
    }

//    fun getSecondList(): List<ListItemViewModel> = prefs.getStringSet(SECOND_LIST_KEY, setOf())!!
//        .map {
//            Json.decodeFromString<ListItemViewModel>(it)
//        }.toList().sortedBy { it.id }

    companion object {
        private const val PREFS = "PREFS"
        private const val LOG_PREFS_KEY = "LOG_PREFS_KEY"
    }
}