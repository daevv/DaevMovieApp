package com.example.filmsapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.filmsapp.ui.add_film_screen.AddFilmScreen
import com.example.filmsapp.ui.add_film_screen.data.AddFilmObject
import com.example.filmsapp.ui.favourites_screen.FavouritesScreen
import com.example.filmsapp.ui.film_details_screen.FilmDetailsScreen
import com.example.filmsapp.ui.film_details_screen.data.FilmDetailsObject
import com.example.filmsapp.ui.login.LoginScreen
import com.example.filmsapp.ui.login.data.LoginScreenObject
import com.example.filmsapp.ui.login.data.MainScreenDataObject
import com.example.filmsapp.ui.main_screen.MainScreen
import com.example.filmsapp.ui.widgets.bottom_menu.BottomMenuItem
import com.example.filmsapp.ui.settings_screen.SettingsScreen
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            val db = remember { Firebase.firestore }

            NavHost(navController = navController, startDestination = LoginScreenObject) {
                composable<LoginScreenObject> {
                    LoginScreen { navData ->
                        navController.navigate(navData)
                    }
                }

                composable<MainScreenDataObject> { navEntry ->
                    val navData = navEntry.toRoute<MainScreenDataObject>()
                    val innerNavController = rememberNavController()

                    Scaffold(
                        bottomBar = {
                            NavigationBar {
                                val items = listOf(
                                    BottomMenuItem.Home,
                                    BottomMenuItem.Favs,
                                    BottomMenuItem.Settings
                                )
                                val currentRoute = innerNavController.currentBackStackEntryAsState().value?.destination?.route

                                items.forEach { item ->
                                    NavigationBarItem(
                                        selected = currentRoute == item.route,
                                        onClick = {
                                            innerNavController.navigate(item.route) {
                                                popUpTo(innerNavController.graph.startDestinationId)
                                                launchSingleTop = true
                                            }
                                        },
                                        icon = {
                                            Icon(
                                                painter = painterResource(id = item.iconId),
                                                contentDescription = item.title
                                            )
                                        }
                                    )
                                }
                            }
                        }
                    ) { paddingValues ->
                        NavHost(
                            navController = innerNavController,
                            startDestination = BottomMenuItem.Home.route,
                            modifier = Modifier.padding(paddingValues)
                        ) {
                            composable(BottomMenuItem.Home.route) {
                                MainScreen(
                                    navData = navData,
                                    db = db,
                                    onFilmEditClick = { film ->
                                        navController.navigate(AddFilmObject(key = film.key))
                                    },
                                    onAdminClick = {
                                        navController.navigate(AddFilmObject())
                                    },
                                    navController = innerNavController
                                )
                            }
                            composable(BottomMenuItem.Favs.route) {
                                FavouritesScreen(db = db, uid = navData.uid, navController = navController)
                            }
                            composable(BottomMenuItem.Settings.route) {
                                SettingsScreen(email = navData.email, db = db, uid = navData.uid, navController = navController)
                            }
                            composable<FilmDetailsObject> { navEntry ->
                                val filmDetailsData = navEntry.toRoute<FilmDetailsObject>()
                                FilmDetailsScreen(navData = filmDetailsData)
                            }
                        }
                    }
                }

                composable<AddFilmObject> { navEntry ->
                    val navData = navEntry.toRoute<AddFilmObject>()
                    AddFilmScreen(navData) {
                        navController.popBackStack()
                    }
                }
            }
        }
    }
}
