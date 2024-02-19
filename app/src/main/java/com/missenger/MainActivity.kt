package com.missenger

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.missenger.data.SocialRepository
import com.missenger.ui.theme.MissengerTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SocialRepository().initPrefs(getPreferences(Context.MODE_PRIVATE))
        setContent {
            MissengerTheme {
                Router()
            }
        }
    }
}

//@Composable
//fun Greeting(name: String, modifier: Modifier = Modifier) {
//    Text(
//        text = "Hello $name!",
//        modifier = modifier
//    )
//}
//
//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    MissengerTheme {
//        Greeting("Android")
//    }
//}