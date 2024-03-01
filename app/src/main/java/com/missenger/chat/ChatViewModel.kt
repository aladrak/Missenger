package com.missenger.chat

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.missenger.data.MessageModel
import com.missenger.data.SocialRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChatViewModel(
    private val friendId: Int,
) : ViewModel() {
    private val repository = SocialRepository()

    data class ChatState(
        val list: List<MessageModel>? = null,
        val load: Boolean = true
    )

    val State = MutableStateFlow(ChatState())

    fun sendMessage(friendId: Int, msg: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.sendMsg(friendId = friendId, message = msg)
            val resultMsg = repository.getMessages(friendId = friendId)
            withContext(Dispatchers.Main) {
                State.emit(
                    ChatState(
                        list = resultMsg.second,
                        load = true
                    )
                )
            }
        }
    }

//    val State: StateFlow<ChatState>
//        get() = state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getMessages(friendId = friendId)
            result.second?.let {
                withContext(Dispatchers.Main) {
                    State.emit(
                        ChatState(
                            list = result.second,
                            load = true
                        )
                    )
                }
            }
        }
    }

    companion object {
        fun Factory(friendId: Int): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                ChatViewModel(friendId)
            }
        }
    }
}