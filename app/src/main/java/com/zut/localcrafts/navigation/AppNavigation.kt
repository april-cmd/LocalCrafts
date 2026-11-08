package com.zut.localcrafts.navigation

import android.net.Uri
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
            ArtisanAuthScreen(
                onLoginSuccess = {
                    navController.navigate("profile")
                }
            )
        }

        composable("home") {
            HomeScreen(
                onProductClick = { productName ->
                    navController.navigate(
                        "search/${Uri.encode(productName)}"
                    )
                }
            )
        }

        composable("search/{productName}") { backStackEntry ->
            val productName =
                backStackEntry.arguments?.getString("productName") ?: ""

            ProductComparisonScreen(
                productName = productName
            )
        }

        composable("profile") {
            ArtisanProfileScreen(
                onExploreClick = {
                    navController.navigate("home")
                }
            )
        }
    }
}