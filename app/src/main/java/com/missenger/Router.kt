package com.missenger

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.missenger.auth.AuthScreen
import com.missenger.auth.AuthViewModel
import com.missenger.chat.ChatScreen
import com.missenger.chat.ChatViewModel
import com.missenger.messenger.MessengerScreen
import com.missenger.messenger.MessengerViewModel
import com.missenger.ui.theme.SmallText

enum class Router(@StringRes val title: Int) {
    Auth(title = R.string.auth),
    Messenger(title = R.string.messenger),
    Chat(title = R.string.chat),
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Router (
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
//    val currentScreen =
    Scaffold(
        topBar = {
            TopAppBar(
                modifier = Modifier
                    .wrapContentHeight(),
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.secondary,
                ),
                title = {
                    SmallText(stringResource(R.string.app_name))
                },
                navigationIcon = {
                    IconButton(
                        modifier = Modifier
                            .size(50.dp),
                        onClick = { },
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.baseline_menu_24),
                            "Nav btn",
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        NavHost (
            navController = navController,
            startDestination = Router.Messenger.name,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            composable(route = Router.Auth.name) {
                val viewModel: AuthViewModel = viewModel()
                AuthScreen(viewModel.State)
            }
            composable(route = Router.Messenger.name) {
                val viewModel: MessengerViewModel = viewModel()
                MessengerScreen(
                    viewModel.State,
                    { navController.navigate(Router.Chat.name.plus("/$it")) }
                )
            }
            composable(
                route = Router.Chat.name.plus("/{friendId}"),
                arguments = listOf(navArgument("friendId") {type = NavType.IntType})
            ) {
                val friendId = it.arguments?.getInt("friendId") ?: -1
                val viewModel: ChatViewModel = viewModel(factory = ChatViewModel.Factory(friendId))
                ChatScreen(viewModel.State, {viewModel.sendMessage(friendId, it)})

            }
        }
    }
}