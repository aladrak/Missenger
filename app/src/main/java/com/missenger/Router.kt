package com.missenger

import android.annotation.SuppressLint
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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
import com.missenger.data.SocialRepository
import com.missenger.messenger.MessengerScreen
import com.missenger.messenger.MessengerViewModel

enum class Router(@StringRes val title: Int) {
    Auth(title = R.string.auth),
    Messenger(title = R.string.messenger),
    Chat(title = R.string.chat),
}

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("StateFlowValueCalledInComposition")
@Composable
fun Router (
    repository: SocialRepository,
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = navController.currentDestination
    Scaffold { innerPadding ->
        Surface(
            color = MaterialTheme.colorScheme.onSecondary,
            contentColor = MaterialTheme.colorScheme.secondary
        ) {
            NavHost(
                navController = navController,
                startDestination =
                    if (repository.LoggedUser.id == -1) {Router.Auth.name} else {Router.Messenger.name},
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable(route = Router.Auth.name) {
                    val viewModel: AuthViewModel = viewModel(factory = AuthViewModel.Factory(repository))
                    AuthScreen(
                        viewModel.State,
                        { viewModel.login(it) },
                        { viewModel.registration(it) },
                        { navController.navigate(Router.Messenger.name) { popUpTo(0) } }
                    )
                }
                composable(route = Router.Messenger.name) {
                    val viewModel: MessengerViewModel = viewModel(factory = MessengerViewModel.Factory(repository))
                    MessengerScreen(
                        viewModel.State,
                        { navController.navigate(Router.Chat.name.plus("/$it")) },
                        { viewModel.searchUser(it) },
                        {
                            viewModel.logOut()
                            navController.navigate(Router.Auth.name) { popUpTo(0) }
                        }
                    )
                }
                composable(
                    route = Router.Chat.name.plus("/{friendId}"),
                    arguments = listOf(navArgument("friendId") { type = NavType.IntType })
                ) {
                    val friendId = it.arguments?.getInt("friendId") ?: -1
                    val viewModel: ChatViewModel = viewModel(factory = ChatViewModel.Factory(friendId, repository))
                    ChatScreen(
                        viewModel.State,
                        { viewModel.sendMessage(friendId, it) },
                    )
                }
            }
        }
    }
}