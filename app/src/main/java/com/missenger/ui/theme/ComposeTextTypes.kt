package com.missenger.ui.theme

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp

@Composable
fun MediumText (
    text: String,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Center
) {
    Text (
        text = text,
        modifier = modifier,
        fontSize = 21.sp,
        textAlign = textAlign
    )
}
@Composable
fun SmallText (
    text: String,
    modifier: Modifier = Modifier
) {
    Text (
        text = text,
        modifier = modifier,
        fontSize = 16.sp
    )
}