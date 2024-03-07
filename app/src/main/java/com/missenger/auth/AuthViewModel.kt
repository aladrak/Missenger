package com.missenger.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.missenger.data.RegUserModel
import com.missenger.data.SocialRepository
import com.missenger.data.UserInfo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val repository = SocialRepository()

    data class AuthState(
        val model: UserInfo? = UserInfo(),
        val load: Boolean = true
    )
    fun registration(item: RegUserModel) {
        viewModelScope.launch(Dispatchers.IO) {
            val resultMsg = repository.regUser(item.username, item.password, item.lastname, item.firstname)
//            result.second?.let {
//                withContext(Dispatchers.Main) {
//                    State.emit(
//                        AuthState(
//                            model = resultMsg.second,
//                        )
//                    )
//                }
//            }
        }
    }

    val State = MutableStateFlow(AuthState())

//    init {
//        viewModelScope.launch(Dispatchers.IO) {
//            val result = repository.getUserInfo("1")
//            result.second?.let {
//                withContext(Dispatchers.Main) {
//                    State.emit(
//                        AuthState(
//                            model = result.second,
//                            load = true
//                        )
//                    )
//                }
//            }
//        }
//    }
}