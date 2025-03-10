package com.example.filmsapp.ui.add_film_screen.data

import kotlinx.serialization.Serializable

@Serializable
data class AddFilmObject(
    val key: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val year: String = "",
    val genre: String = "",
)