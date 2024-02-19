package com.missenger.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.missenger.data.SocialRepository
import com.missenger.data.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel : ViewModel() {
    private val repository = SocialRepository()

    data class AuthState(
        val model: UserInfo? = UserInfo(),
        val load: Boolean = true
    )

    val State = MutableStateFlow(AuthState())

//    val State: StateFlow<AuthState>
//        get() = state.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.getUserInfo("1")
            result.second?.let {
                withContext(Dispatchers.Main) {
                    State.emit(
                        AuthState(
                            model = result.second,
                            load = true
                        )
                    )
                }
            }
        }
    }
}