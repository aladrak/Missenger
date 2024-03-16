package com.missenger.auth

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.missenger.R
import com.missenger.data.LogUserModel
import com.missenger.data.RegUserModel
import kotlinx.coroutines.flow.StateFlow

@Composable
fun AuthScreen(
    state: StateFlow<AuthViewModel.AuthState>,
    onClickLog: (LogUserModel) -> Unit,
    onClickReg: (RegUserModel) -> Unit,
    navigate: () -> Unit,
) {
    val value = state.collectAsState().value

    if (value.logged != -1 || value.code == 200) {
        navigate()
    }

    var tabIndex by remember { mutableIntStateOf(0) }

    val tabs = listOf("Login", "Registration")

    Column(modifier = Modifier.fillMaxWidth()) {
        TabRow(selectedTabIndex = tabIndex) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    text = { Text(title) },
                    selected = tabIndex == index,
                    onClick = { tabIndex = index }
                )
            }
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedCard() {
                when (tabIndex) {
                    0 -> LoginScreen(state, onClickLog, navigate)
                    1 -> RegistrationScreen(value, onClickReg, navigate)
                }
            }
        }
    }
}
@Composable
fun LoginScreen(
    state: StateFlow<AuthViewModel.AuthState>,
    onClickAction: (LogUserModel) -> Unit,
    navigate: () -> Unit,
) {
    val context = LocalContext.current
    val value = state.collectAsState().value
    val username = remember { mutableStateOf(TextFieldValue()) }
    val password = remember { mutableStateOf(TextFieldValue()) }
    AuthField("Username", username)
    AuthField("Password", password)
    IconButton(
        onClick = {
            if (username.value.text.isNotEmpty() && password.value.text.isNotEmpty()) {
                onClickAction(LogUserModel(username.value.text, password.value.text))
                if (value.code == 200) {
                    navigate()
                } else {
                    Toast.makeText(
                        context,
                        "Error! " + value.code.toString(),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            } else {
                Toast.makeText(
                    context,
                    "Empty fields!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        },
        content = {
            Icon(
                painter = painterResource(id = R.drawable.baseline_check_24),
                "check btn"
            )
        }
    )
}
@Composable
fun RegistrationScreen(
    value: AuthViewModel.AuthState,
    onClickAction: (RegUserModel) -> Unit,
    navigate: () -> Unit,
) {

}
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthField(
    fieldName: String,
    inputText: MutableState<TextFieldValue>
) {
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