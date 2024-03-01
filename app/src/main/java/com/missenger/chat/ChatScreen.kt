package com.missenger.chat

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import com.missenger.R
import com.missenger.data.MessageModel
import com.missenger.ui.theme.SmallText
import kotlinx.coroutines.flow.StateFlow
import java.time.format.DateTimeFormatter

@Composable
fun ChatScreen(
    model: StateFlow<ChatViewModel.ChatState>,
    onSend: (String) -> Unit = {}
) {
    val state by model.collectAsState()
    val inputText = remember { mutableStateOf(TextFieldValue()) }
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        if (!state.list.isNullOrEmpty()) {
            MessageList(state.list!!)
        } else {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                SmallText(text = "Нет сообщений")
            }
        }
        Row(
            modifier = Modifier
                .padding(7.dp, 0.dp, 7.dp, 5.dp)
        ) {
            SendField(inputText, onSend)
        }
    }
}
@Composable
fun MessageList(
    list: List<MessageModel>
) {
    Column(
        modifier = Modifier
            .padding(5.dp, 5.dp, 5.dp, 0.dp)
            .fillMaxWidth()
            .height(650.dp)
            .verticalScroll(rememberScrollState(0), reverseScrolling = true)
    ) {
        list.reversed().forEach {
            MessageItem(it)
        }
    }
}

@Composable
fun MessageItem(
    item: MessageModel,
) {
    Column(
        modifier = Modifier
//            .clickable {
//
//            }
    ) {
        Column(
            modifier = Modifier
                .padding(5.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SmallText(text = item.from.username.plus("#".plus(item.from.id)))
                SmallText(
                    text = item.datetime.format(DateTimeFormatter.ofPattern("d MMM, HH:mm")).toString()
                )
            }
            SmallText(text = item.message)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SendField(
    inputText: MutableState<TextFieldValue>,
    onSend: (String) -> Unit = {},
) {
    OutlinedTextField(
//            label = { Text("Send") },
        value = inputText.value,
        onValueChange = {
            inputText.value = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
//            singleLine = true,
//            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Send),
//            keyboardActions = KeyboardActions(
//                onSend = {
//                    onSend(inputText.value.text)
//                }
//            ),
        trailingIcon = {
            IconButton(
                modifier = Modifier
                    .size(50.dp),
                onClick = {
                    onSend(inputText.value.text)
                    inputText.value = TextFieldValue("")
                },
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_send_24),
                    "Send btn",
                )
            }
        },
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = MaterialTheme.colorScheme.secondary,
            unfocusedBorderColor = MaterialTheme.colorScheme.secondary,
            disabledBorderColor = MaterialTheme.colorScheme.secondary,
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