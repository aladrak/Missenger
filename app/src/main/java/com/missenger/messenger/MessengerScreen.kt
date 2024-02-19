package com.missenger.messenger

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.missenger.R
import com.missenger.data.MessageModel
import com.missenger.ui.theme.MediumText
import com.missenger.ui.theme.SmallText
import kotlinx.coroutines.flow.StateFlow
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessengerScreen(
    state: StateFlow<MessengerViewModel.MessengerState>,
    onClickAction: (Int) -> Unit
) {
    val value = state.collectAsState().value
    if (value.model == null) {
        MediumText(text = "Some Error")
    } else {
        ConstraintLayout {
            val (list, button) = createRefs()
            Column(
                modifier = Modifier
                    .constrainAs(list) {
                        top.linkTo(parent.top)
                    }
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
                    .constrainAs(button){
                        bottom.linkTo(parent.bottom, margin = 16.dp)
                        end.linkTo(parent.end, margin = 16.dp)
                    },
                onClick = {

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
}

@Composable
fun ChatItem (
    item: MessageModel,
    onClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(0.dp, 0.dp, 0.dp, 0.dp)
            .clickable {
                onClick(
                    if (item.from.id == item.logged) {
                        item.to.id
                    } else {
                        item.from.id
                    }
                )
            }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            MediumText(text = if (item.from.id == item.logged) {item.to.username} else {item.from.username})
            SmallText(text = item.datetime.format(DateTimeFormatter.ofPattern("d MMM, HH:mm")).toString())
        }
        Row() {
            SmallText(text = item.from.username + ": ")
            SmallText(text = item.message)
        }
    }
}

@Composable
fun CardHolder (

) {
    OutlinedCard(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {

    }
}