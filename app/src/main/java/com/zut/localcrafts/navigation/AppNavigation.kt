package com.zut.localcrafts.navigation

import android.net.Uri
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.zut.localcrafts.screens.*

@Composable
fun AppNavigation(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {

    val navController = rememberNavController()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
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

            // Floating Theme Toggle Button at top-right
            SmallFloatingActionButton(
                onClick = onToggleTheme,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Text(
                    text = if (isDarkTheme) "☀️" else "🌙",
                    fontSize = 18.sp
                )
            }
        }
    }
}
