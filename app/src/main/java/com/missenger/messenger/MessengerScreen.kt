package com.missenger.messenger

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.missenger.R
import com.missenger.data.MessageModel
import com.missenger.data.UserInfo
import com.missenger.ui.theme.AppColor
import com.missenger.ui.theme.MediumText
import com.missenger.ui.theme.SmallText
import kotlinx.coroutines.flow.StateFlow
import java.time.format.DateTimeFormatter

@Composable
fun MessengerScreen(
    state: StateFlow<MessengerViewModel.MessengerState>,
    onClickAction: (Int) -> Unit,
    onSearch: (String) -> Unit,
) {
    val openDialog = remember {mutableStateOf(true)}
    val value = state.collectAsState().value
    if (openDialog.value) {
        if (value.model == null) {
//            MediumText(text = "Some Error")
            CircularProgressIndicator(
                modifier = Modifier
                    .fillMaxSize()
            )
        } else {
            ConstraintLayout {
                val (list, button) = createRefs()
                Column(
                    modifier = Modifier
                        .constrainAs(list) {top.linkTo(parent.top)}
                        .fillMaxWidth()
                        .padding(5.dp, 0.dp, 5.dp, 0.dp)
                ) {
                    if (value.model.isNotEmpty()) {
                        value.model.forEach {
                            ChatItem(
                                it,
                                onClickAction
                            )
                        }
                    } else {
                        MediumText(text = "No chats!")
                    }
                }
                FloatingActionButton(
                    modifier = Modifier
                        .size(55.dp)
                        .constrainAs(button) {
                            bottom.linkTo(parent.bottom, margin = 16.dp)
                            end.linkTo(parent.end, margin = 16.dp)
                        },
                    containerColor = AppColor,
                    contentColor = MaterialTheme.colorScheme.primary,
                    onClick = {
                        openDialog.value = false
                    },
                    content = {
                        Icon(
                            painter = painterResource(id = R.drawable.baseline_add_24),
                            "add btn"
                        )
                    }
                )
            }
        }
    } else {
        CardHolder(
            state,
            { openDialog.value = true },
            onClickAction,
            onSearch,
        )
    }
}

@Composable
fun ChatItem (
    item: MessageModel,
    onClick: (Int) -> Unit
) {
    val friendId = getFriendId(item)
    val friendName = getFriendName(item)
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(0.dp, 2.dp, 0.dp, 2.dp)
            .background(
                color = MaterialTheme.colorScheme.primary,
                RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
            .clickable {
                onClick(friendId)
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp, 5.dp, 8.dp, 5.dp)
        ) {
            ColorWithText(friendId, friendName[0].uppercaseChar().toString())
            Column(
                modifier = Modifier
                    .padding(8.dp, 0.dp, 4.dp, 0.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    MediumText(
                        text = friendName.plus("#".plus(friendId)),
                        modifier = Modifier
                    )
                    SmallText(
                        text = item.datetime.format(DateTimeFormatter.ofPattern("d MMM, HH:mm"))
                            .toString()
                    )
                }
                Row(
                    horizontalArrangement = Arrangement.Start
                ) {
                    SmallText(text = item.from.username + ": ")
                    Text(text = item.message, maxLines = 1)
                }
            }
        }
//        Line(370.dp, MaterialTheme.colorScheme.onSecondary)
    }
}

@Composable
fun CardHolder(
    state: StateFlow<MessengerViewModel.MessengerState>,
    closeDialog: () -> Unit,
    onClick: (Int) -> Unit,
    onSearch: (String) -> Unit,
) {
    val value = state.collectAsState().value
    OutlinedCard(
        modifier = Modifier
            .padding(15.dp)
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .padding(10.dp, 10.dp, 10.dp, 0.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            MediumText(text = "User Searcher")
            FloatingActionButton(
                modifier = Modifier
                    .size(55.dp),
                contentColor = MaterialTheme.colorScheme.primary,
                containerColor = AppColor,
                onClick = { closeDialog() },
                content = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_close_24),
                        "close btn"
                    )
                }
            )
        }
        Column(
            modifier = Modifier
                .padding(10.dp, 0.dp, 10.dp, 0.dp)
                .wrapContentHeight()
                .fillMaxWidth(),
        ) {
            SearchUserField(onSearch)
            Column(
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .fillMaxHeight()
            ) {
                if (value.searchResult.isNullOrEmpty()) {
                    MediumText(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                            .align(alignment = Alignment.CenterHorizontally),
                        text = "No results."
                    )
                } else {
                    value.searchResult.forEach {
                        UserInfoItem(it, onClick)
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchUserField(
    onSearch: (String) -> Unit,
) {
    val inputText = remember { mutableStateOf(TextFieldValue()) }
    OutlinedTextField(
        label = { Text("Username") },
        value = inputText.value,
        onValueChange = {
            inputText.value = it
        },
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = { onSearch(inputText.value.text) }
        ),
        trailingIcon = {
            IconButton(
                modifier = Modifier
                    .size(50.dp),
                onClick = { onSearch(inputText.value.text) },
            ) {
                Icon(
                    painter = painterResource(R.drawable.baseline_search_24),
                    "search btn",
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

@Composable
fun UserInfoItem(
    item: UserInfo,
    onClick: (Int) -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(
                    item.id
                )
            }
    ) {
        MediumText(
            modifier = Modifier
                .padding(5.dp),
            text = item.username.plus("#".plus(item.id.toString()))
        )
    }
}
@Composable
fun ColorWithText(userId: Int, text: String) {
    Box(
        modifier = Modifier
            .height(50.dp)
            .width(50.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color(0xFF29a672 + userId * 128),
                    shape = RoundedCornerShape(32.dp)
                )
        )
        Text(
            text = text,
            color = Color.White,
            style = MaterialTheme.typography.titleMedium
        )
    }
}
fun getFriendId(item: MessageModel): Int {
    return if (item.from.id == item.logged) {
        item.to.id
    } else {
        item.from.id
    }
}
fun getFriendName(item: MessageModel): String {
    return if (item.from.id == item.logged) {
        item.to.username
    } else {
        item.from.username
    }
}