package com.missenger.messenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.missenger.data.MessageModel
import com.missenger.data.SocialRepository
import com.missenger.data.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessengerViewModel : ViewModel() {
    private val repository = SocialRepository()

    data class MessengerState(
        val model: List<MessageModel>? = null,
        val searchResult: List<UserInfo>? = null,
        val load: Boolean = true,
    )
    val State = MutableStateFlow(MessengerState())

//    val State: StateFlow<MessengerState>
//        get() = state.asStateFlow()
    fun searchUser(str: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val resultMsg = repository.getLastMessages()
            val result = repository.searchUser(str)
            result.second?.let {
                withContext(Dispatchers.Main) {
                    State.emit(
                        MessengerState(
                            model = resultMsg.second,
                            searchResult = result.second,
                        )
                    )
                }
            }
        }
    }
    init {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getLastMessages()
            result.second?.let {
                withContext(Dispatchers.Main) {
                    State.emit(
                        MessengerState(
                            model = result.second,
                            load = true,
                        )
                    )
                }
            }
        }
    }
}