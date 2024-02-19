package com.missenger.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AuthScreen(
    state: StateFlow<AuthViewModel.AuthState>,
) {
    val value = state.collectAsState().value
    if (value.load) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            Text(
                text = value.model?.id.toString()
            )
            Text(
                text = value.model?.username ?: "ERROR"
            )
            Text(
                text = value.model?.firstname ?: "none"
            )
            Text(
                text = value.model?.lastname ?: "none"
            )
        }
    } else {
        CircularProgressIndicator(
            modifier = Modifier
                .fillMaxSize()
        )
    }
}