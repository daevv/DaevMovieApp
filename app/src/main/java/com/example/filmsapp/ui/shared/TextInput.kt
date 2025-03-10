package com.example.filmsapp.ui.shared

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun TextInput(
    singleLine: Boolean = true,
    maxLines: Int = 1,
    text: String,
    label: String,
    isPassword: Boolean = false,
    onValueChange: (String) -> Unit,
) {
    TextField(
        value = text,
        onValueChange = { onValueChange(it) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .background(
                color = Color(0x33FFFFFF),
                shape = RoundedCornerShape(12.dp)
            ),
        colors = TextFieldDefaults.colors(
            unfocusedContainerColor = Color.Transparent,
            focusedContainerColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            focusedTextColor = Color(0xFF212121),
            unfocusedTextColor = Color(0xFF424242),
            cursorColor = Color(0xFFFFA726)
        ),
        label = {
            Text(
                text = label,
                color = Color(0xFF251919),
                style = TextStyle(fontSize = 14.sp)
            )
        },
        singleLine = singleLine,
        maxLines = maxLines,
        textStyle = TextStyle(
            color = Color(0xFF212121),
            fontSize = 16.sp
        ),
        shape = RoundedCornerShape(12.dp),
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
    )
}