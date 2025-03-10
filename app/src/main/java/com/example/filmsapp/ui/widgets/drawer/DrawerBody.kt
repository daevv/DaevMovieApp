package com.example.filmsapp.ui.widgets.drawer

import com.example.filmsapp.ui.shared.CustomButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.filmsapp.R
import com.example.filmsapp.ui.theme.ButtonColor
import com.example.filmsapp.ui.theme.DrawerButtonColor
import com.example.filmsapp.ui.theme.GrayDark
import com.example.filmsapp.ui.theme.HeaderColor
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.firestore


@Composable
fun DrawerBody(
    syncAdminState: (Boolean) -> Unit = {},
    onAdminClick: () -> Unit = {},
    onGenreClick: (String) -> Unit = {}
) {

    val categoryList = listOf(
        "All",
        "Action",
        "Crime",
        "Series",
    )

    val isAdminState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { isAdmin { isAdmin ->
        isAdminState.value = isAdmin
        syncAdminState(isAdmin)
    } }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ButtonColor)
    ) {
        Image(
            painter = painterResource(id = R.drawable.drawable_bg),
            modifier = Modifier.fillMaxSize(),
            contentDescription = "DrawableBody",
            contentScale = ContentScale.Crop
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Categories",
                fontSize = 20.sp,
                color = HeaderColor,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(GrayDark)
            )
            LazyColumn(Modifier.fillMaxWidth()) {
                items(categoryList) { item ->
                    Column(Modifier
                        .fillMaxWidth()
                        .clickable {
                            onGenreClick(item)
                        }) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = item,
                            color = Color.Black,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .fillMaxWidth()
                                .wrapContentWidth()
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(1.dp)
                                .background(GrayDark)
                        )
                    }

                }
            }
            if (isAdminState.value) CustomButton(text = "Admin panel", buttonColor = DrawerButtonColor) { onAdminClick() }
        }
    }
}

fun isAdmin(onAdmin: (Boolean) -> Unit) {
    val uid = Firebase.auth.currentUser!!.uid
    val db = Firebase.firestore.collection("admin").document(uid).get()
        .addOnSuccessListener { onAdmin(it.get("isAdmin") as Boolean) }
}