package com.zut.localcrafts.navigation

import android.net.Uri
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DarkMode
import androidx.compose.material.icons.outlined.LightMode
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.zut.localcrafts.screens.*

@Composable
fun AppNavigation(
    isDarkTheme: Boolean,
    onToggleTheme: () -> Unit
) {

    val navController = rememberNavController()

    // Which screen is showing (the floating bar only appears on "home")
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route

    // Each tap on the bar's Search or Home button bumps a counter that HomeScreen reacts to
    var searchFocusTick by remember { mutableIntStateOf(0) }
    var scrollToTopTick by remember { mutableIntStateOf(0) }

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
                        onLoginSuccess = { artisanId ->
                            navController.navigate("profile/${Uri.encode(artisanId)}") {
                                // Back from the dashboard should not return to the login form
                                popUpTo("auth") { inclusive = true }
                            }
                        }
                    )
                }

                composable("home") {
                    HomeScreen(
                        searchFocusTick = searchFocusTick,
                        scrollToTopTick = scrollToTopTick,
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

                composable(
                    route = "profile/{artisanId}",
                    arguments = listOf(navArgument("artisanId") { type = NavType.StringType })
                ) { backStackEntry ->
                    val artisanId =
                        backStackEntry.arguments?.getString("artisanId") ?: ""

                    ArtisanProfileScreen(
                        artisanId = artisanId,
                        onExploreClick = {
                            navController.navigate("home")
                        },
                        onLogout = {
                            navController.navigate("landing") {
                                popUpTo(0) { inclusive = true }
                            }
                        }
                    )
                }
            }

            // Floating bottom bar (home screen only)
            if (currentRoute == "home") {
                FloatingNavBar(
                    onHomeClick = { scrollToTopTick++ },
                    onSearchClick = { searchFocusTick++ },
                    onSellClick = { navController.navigate("auth") },
                    modifier = Modifier.align(Alignment.BottomCenter)
                )
            }

            // Theme toggle at top-right: a small round button with an outlined icon
            Surface(
                onClick = onToggleTheme,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .statusBarsPadding()
                    .padding(top = 8.dp, end = 16.dp)
                    .size(40.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.surface,
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
                shadowElevation = 3.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Crossfade(targetState = isDarkTheme, label = "themeIcon") { dark ->
                        Icon(
                            imageVector = if (dark) Icons.Outlined.LightMode
                            else Icons.Outlined.DarkMode,
                            contentDescription = if (dark) "Switch to light theme"
                            else "Switch to dark theme",
                            tint = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}