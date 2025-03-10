package com.example.filmsapp.ui.login.data

import kotlinx.serialization.Serializable

@Serializable
data class MainScreenDataObject(
    var uid: String = "",
    var email: String = "",
)
