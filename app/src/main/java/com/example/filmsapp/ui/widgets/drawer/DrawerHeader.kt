package com.example.filmsapp.ui.widgets.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filmsapp.R
import com.example.filmsapp.ui.theme.HeaderColor

@Composable
fun DrawerHeader(email: String) {
    Column(
        Modifier
            .fillMaxWidth()
            .height(179.dp)
            .background(HeaderColor),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Logo",
            modifier = Modifier.size(90.dp)
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "DAEV Movie App",
            color = Color.Black,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold
        )
    }
}