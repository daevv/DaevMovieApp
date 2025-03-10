package com.example.filmsapp.ui.main_screen

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.filmsapp.R
import com.example.filmsapp.data.Film
import com.example.filmsapp.ui.film_details_screen.data.FilmDetailsObject

@Composable
fun FilmItem(
    film: Film,
    showEditButton: Boolean = false,
    navController: NavController,
    onEditClick: (Film) -> Unit = {},
    onFavClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .clickable {
                navController.navigate(FilmDetailsObject(key = film.key))
            }
    ) {

        var bitmap: Bitmap? = null

        try {
            val base64Image = Base64.decode(film.imageUrl, Base64.DEFAULT)
            bitmap = BitmapFactory.decodeByteArray(base64Image, 0, base64Image.size)
        } catch (e: IllegalArgumentException) {

        }


        AsyncImage(
            model = bitmap ?: R.drawable.film_test,
            contentDescription = "FilmPoster",
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clip(RoundedCornerShape(15.dp)),
            contentScale = ContentScale.Crop
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = film.title,
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = film.genre,
            color = Color.Gray,
            fontSize = 16.sp,
        )
        Spacer(modifier = Modifier.height(5.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                text = film.year,
                color = Color.Blue,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
            if (showEditButton) IconButton(onClick = {
                onEditClick(film)
            }) {
                Icon(
                    Icons.Default.Edit,
                    contentDescription = "EditIcon"
                )
            }
            IconButton(onClick = {
                onFavClick()
            }) {
                Icon(
                    if (film.isFavourite) {
                        Icons.Default.Favorite
                    } else {
                        Icons.Default.FavoriteBorder
                    },
                    contentDescription = "FavIcon"
                )
            }
        }

    }
}