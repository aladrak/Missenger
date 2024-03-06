package com.missenger.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
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
                .padding(24.dp)
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
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthField(
    fieldName: String,
) {
    val inputText = remember { mutableStateOf(TextFieldValue()) }
    TextField(
        label = { Text(fieldName) },
        value = inputText.value,
        onValueChange = {
            inputText.value = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        singleLine = true,
//        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
//        keyboardActions = KeyboardActions(
//            onSearch = { onSearch(inputText.value.text) }
//        ),
        colors = TextFieldDefaults.textFieldColors(
            cursorColor = MaterialTheme.colorScheme.secondary,
            focusedTextColor = MaterialTheme.colorScheme.secondary,
            unfocusedTextColor = MaterialTheme.colorScheme.secondary,
            disabledTextColor = MaterialTheme.colorScheme.secondary,
            focusedLabelColor = MaterialTheme.colorScheme.secondary,
            unfocusedLabelColor = MaterialTheme.colorScheme.secondary,
            disabledLabelColor = MaterialTheme.colorScheme.secondary,
        )
    )
}