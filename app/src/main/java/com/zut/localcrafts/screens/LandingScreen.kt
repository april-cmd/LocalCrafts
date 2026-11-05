package com.zut.localcrafts.screens


import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LandingScreen(
    onExploreClick: () -> Unit,
    onArtisanClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "LocalCrafts",
            style = MaterialTheme.typography.headlineLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Discover beautiful handmade crafts from local artisans."
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = onExploreClick) {
            Text("Explore Crafts")
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(onClick = onArtisanClick) {
            Text("Artisan Login / Register")
        }
    }
}
