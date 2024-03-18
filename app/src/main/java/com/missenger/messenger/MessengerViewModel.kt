package com.missenger.messenger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.missenger.data.MessageModel
import com.missenger.data.SocialRepository
import com.missenger.data.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessengerViewModel(
    private val repository: SocialRepository
) : ViewModel() {
//    private val repository = SocialRepository()

    data class MessengerState(
        val model: List<MessageModel>? = null,
        val searchResult: List<UserInfo>? = null,
        val load: Boolean = true,
    )
    val State = MutableStateFlow(MessengerState())

//    val State: StateFlow<MessengerState>
//        get() = state.asStateFlow()
    fun logOut() {
        repository.logOutUser()
    }
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
    companion object {
        fun Factory(repository: SocialRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                MessengerViewModel(repository)
            }
        }
    }
}