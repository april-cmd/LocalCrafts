package com.zut.localcrafts.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zut.localcrafts.R

@Composable
fun LandingScreen(
    onExploreClick: () -> Unit,
    onArtisanClick: () -> Unit
) {
    val background = MaterialTheme.colorScheme.background

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
    ) {
        // Top part: tilted photo tiles that fade into the background colour
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.62f)
                .clipToBounds()
        ) {
            HeroCollage()
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            0.55f to Color.Transparent,
                            1f to background
                        )
                    )
            )
        }

        // Bottom part: title, tagline and buttons
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = "Welcome to\nLocalCrafts",
                fontSize = 36.sp,
                lineHeight = 42.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onBackground
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Discover handmade creations from local artisans, all in one place.",
                fontSize = 15.sp,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Dark pill in light mode, light pill in dark mode (colours come from the theme)
            Button(
                onClick = onExploreClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.onBackground,
                    contentColor = MaterialTheme.colorScheme.background
                )
            ) {
                Text("Explore Crafts", fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(
                onClick = onArtisanClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(50)
            ) {
                Text("Artisan Login / Register", fontSize = 16.sp)
            }
        }
    }
}

/**
 * Four photo tiles (craft_1 to craft_4 in res/drawable), each tilted a little
 * and separated by thin gaps, like the reference design.
 */
@Composable
private fun HeroCollage() {
    Box(modifier = Modifier.fillMaxSize()) {
        CollageTile(
            image = R.drawable.craft_1, width = 225, height = 270, rotation = -6f,
            alignment = Alignment.TopStart, x = -25, y = -20
        )
        CollageTile(
            image = R.drawable.craft_2, width = 225, height = 250, rotation = 7f,
            alignment = Alignment.TopEnd, x = 25, y = -10
        )
        CollageTile(
            image = R.drawable.craft_3, width = 225, height = 280, rotation = 7f,
            alignment = Alignment.CenterStart, x = -25, y = 125
        )
        CollageTile(
            image = R.drawable.craft_4, width = 225, height = 260, rotation = -7f,
            alignment = Alignment.CenterEnd, x = 25, y = 135
        )
    }
}

@Composable
private fun BoxScope.CollageTile(
    image: Int,
    width: Int,
    height: Int,
    rotation: Float,
    alignment: Alignment,
    x: Int,
    y: Int
) {
    Image(
        painter = painterResource(id = image),
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .align(alignment)
            .offset(x = x.dp, y = y.dp)
            .size(width = width.dp, height = height.dp)
            .rotate(rotation)
            .clip(RoundedCornerShape(32.dp))
    )
}