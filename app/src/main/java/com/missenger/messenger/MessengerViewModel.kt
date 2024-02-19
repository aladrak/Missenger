package com.missenger.messenger

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.missenger.data.MessageModel
import com.missenger.data.SocialRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessengerViewModel : ViewModel() {
    private val repository = SocialRepository()

    data class MessengerState(
        val model: List<MessageModel>? = null,
        val load: Boolean = true,
    )
    @Composable
    @Preview
    fun PreviewMessenger() {
        MessengerScreen(state = State, onClickAction = {})
    }
    val State = MutableStateFlow(MessengerState())

//    val State: StateFlow<MessengerState>
//        get() = state.asStateFlow()

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