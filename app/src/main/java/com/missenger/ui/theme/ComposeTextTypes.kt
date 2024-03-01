package com.missenger.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
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
        textAlign = textAlign,
        maxLines = 1,
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

@Composable
fun Line(
    width: Dp,
    color: Color
) {
    Box(
        modifier = Modifier
            .height(2.dp)
            .width(width)
            .background(color)
    )
}