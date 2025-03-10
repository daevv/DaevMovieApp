package com.example.filmsapp.data

data class Film(
    val key: String = "",
    val title: String = "",
    val description: String = "",
    val imageUrl: String = "",
    val year: String = "",
    val genre: String = "",
    val isFavourite: Boolean = false
)
