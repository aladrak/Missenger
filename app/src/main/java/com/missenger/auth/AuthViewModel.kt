package com.missenger.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.missenger.data.LogUserModel
import com.missenger.data.RegUserModel
import com.missenger.data.SocialRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AuthViewModel(
    private val repository: SocialRepository
) : ViewModel() {
//    private val repository = SocialRepository()
    data class AuthState( val code: Int = -1 )
    val State = MutableStateFlow(AuthState())
    fun login(item: LogUserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.logUser(item)
            withContext(Dispatchers.Main) {
                State.emit(
                    AuthState(
                        code = result.first,
                    )
                )
            }
        }
    }
    fun registration(item: RegUserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = repository.regUser(item)
            withContext(Dispatchers.Main) {
                State.emit(
                    AuthState(
                        code = result.first,
                    )
                )
            }
        }
    }
    companion object {
        fun Factory(repository: SocialRepository): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                AuthViewModel(repository)
            }
        }
    }
}