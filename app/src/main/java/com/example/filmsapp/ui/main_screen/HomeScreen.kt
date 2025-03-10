package com.example.filmsapp.ui.main_screen

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.filmsapp.data.Favourite
import com.example.filmsapp.data.Film
import com.example.filmsapp.ui.login.data.MainScreenDataObject
import com.example.filmsapp.ui.main_screen.drawer.DrawerBody
import com.example.filmsapp.ui.main_screen.drawer.DrawerHeader
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(
    navData: MainScreenDataObject,
    db: FirebaseFirestore,
    navController: NavController,
    onFilmEditClick: (Film) -> Unit,
    onAdminClick: () -> Unit
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val coroutineScope = rememberCoroutineScope()
    val filmsListState = remember { mutableStateOf(emptyList<Film>()) }
    val isAdminState = remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        getAllFavsIds(db, navData.uid) { favs ->
            getAllFilms(db, favs, "All") { films ->
                filmsListState.value = films
            }
        }
    }

    ModalNavigationDrawer(
        drawerState = drawerState,
        modifier = Modifier.fillMaxWidth(),
        drawerContent = {
            Column(Modifier.fillMaxWidth(0.7f)) {
                DrawerHeader(navData.email)
                DrawerBody(
                    syncAdminState = { isAdmin ->
                        isAdminState.value = isAdmin
                    },
                    onAdminClick = {
                        coroutineScope.launch {
                            drawerState.close()
                        }
                        onAdminClick()
                    },
                    onGenreClick = { genre ->
                        getAllFavsIds(db, navData.uid) { favs ->
                            getAllFilms(db, favs, genre) { films ->
                                filmsListState.value = films
                            }
                        }
                        coroutineScope.launch {
                            drawerState.close()
                        }
                    }
                )
            }
        }
    ) {
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxSize()
        ) {
            items(filmsListState.value) { film ->
                FilmItem(
                    film = film,
                    showEditButton = isAdminState.value,
                    onEditClick = { onFilmEditClick(it) },
                    onFavClick = {
                        filmsListState.value = filmsListState.value.map { item ->
                            if (item.key == film.key) {
                                onFavs(db, navData.uid, Favourite(item.key), !item.isFavourite)
                                item.copy(isFavourite = !item.isFavourite)
                            } else {
                                item
                            }
                        }
                    },
                    navController = navController
                )
            }
        }
    }
}

private fun getAllFilms(
    db: FirebaseFirestore,
    idsList: List<String>,
    genre: String,
    onFilms: (List<Film>) -> Unit
) {
    val query = if (genre == "All") {
        db.collection("films")
    } else {
        db.collection("films").whereEqualTo("genre", genre)
    }
    query.get()
        .addOnSuccessListener { task ->
            Log.d("myTag", idsList.toString())
            val filmsList = task.toObjects(Film::class.java).map {
                if (idsList.contains(it.key)) {
                    it.copy(isFavourite = true)
                } else {
                    it
                }
            }
            Log.d("myTag", filmsList.toString())
            onFilms(filmsList)
        }
}

private fun getAllFavsIds(db: FirebaseFirestore, uid: String, onFavs: (List<String>) -> Unit) {
    db.collection("users").document(uid).collection("favs").get()
        .addOnSuccessListener { task ->
            val idsList = task.toObjects(Favourite::class.java)
            val keyList = arrayListOf<String>()
            idsList.forEach { keyList.add(it.key) }
            onFavs(keyList)
        }
}

private fun onFavs(
    db: FirebaseFirestore,
    uid: String,
    favourite: Favourite,
    isFav: Boolean
) {
    if (isFav) {
        db.collection("users").document(uid).collection("favs").document(favourite.key)
            .set(favourite)
    } else {
        db.collection("users").document(uid).collection("favs").document(favourite.key)
            .delete()
    }
}