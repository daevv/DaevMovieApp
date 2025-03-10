package com.example.filmsapp.ui.favourites_screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.filmsapp.data.Favourite
import com.example.filmsapp.data.Film
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun FavouritesScreen(
    db: FirebaseFirestore,
    uid: String,
    navController: NavController
) {
    val favouritesListState = remember { mutableStateOf(emptyList<Film>()) }

    LaunchedEffect(Unit) {
        db.collection("users")
            .document(uid)
            .collection("favs")
            .get()
            .addOnSuccessListener { favsTask ->
                val favKeys = favsTask.toObjects(Favourite::class.java).map { it.key }
                if (favKeys.isEmpty()) {
                    favouritesListState.value = emptyList()
                } else {
                    db.collection("films")
                        .whereIn("key", favKeys)
                        .get()
                        .addOnSuccessListener { filmsTask ->
                            favouritesListState.value = filmsTask.toObjects(Film::class.java)
                        }
                }
            }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (favouritesListState.value.isEmpty()) {
            // Placeholder для пустого состояния
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary.copy(alpha = 0.3f),
                    modifier = Modifier.size(80.dp)
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "No Favourites Yet",
                    style = MaterialTheme.typography.headlineSmall,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Add some films to your favourites!",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                )
            }
        } else {
            // Список избранных фильмов
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Your Favourites",
                        style = MaterialTheme.typography.headlineMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 28.sp
                        ),
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(favouritesListState.value) { film ->
                    FavouriteFilmCard(film = film, navController = navController)
                }
            }
        }
    }
}