package com.zut.localcrafts.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zut.localcrafts.screens.*

@Composable
fun AppNavigation() {

    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "landing"
    ) {

        composable("landing") {
            LandingScreen(
                onExploreClick = {
                    navController.navigate("home")
                },
                onArtisanClick = {
                    navController.navigate("auth")
                }
            )
        }

        composable("auth") {
            ArtisanAuthScreen()
        }

        composable("home") {
            HomeScreen()
        }

        composable("search") {
            SearchScreen()
        }

        composable("profile") {
            ArtisanProfileScreen()
        }
    }
}


