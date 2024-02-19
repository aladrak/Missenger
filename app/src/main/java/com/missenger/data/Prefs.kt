package com.missenger.data

import android.content.SharedPreferences

class Prefs (pref: SharedPreferences) {
    private val prefs = pref

    fun putIdToPrefs(id: Int) {
        prefs
            .edit()
            .putInt(ID_PREFS_KEY, id)
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

    fun getLoggedId(): Int = prefs.getInt(ID_PREFS_KEY, 0)

//    fun getSecondList(): List<ListItemViewModel> = prefs.getStringSet(SECOND_LIST_KEY, setOf())!!
//        .map {
//            Json.decodeFromString<ListItemViewModel>(it)
//        }.toList().sortedBy { it.id }

    companion object {
        private const val PREFS = "PREFS"
        private const val ID_PREFS_KEY = "ID_PREFS_KEY"
    }
}