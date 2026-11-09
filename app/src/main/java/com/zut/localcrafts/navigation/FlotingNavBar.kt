package com.zut.localcrafts.navigation

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.zut.localcrafts.R

/**
 * Floating bottom bar: Home on the left, a terracotta "Sell" button in the
 * middle (artisans add their products), and Search on the right.
 */
@Composable
fun FloatingNavBar(
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSellClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        shape = RoundedCornerShape(36.dp),
        color = MaterialTheme.colorScheme.surface,
        tonalElevation = 6.dp,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarItem(
                icon = R.drawable.ic_nav_home,
                label = "Home",
                selected = true,
                onClick = onHomeClick
            )

            Button(
                onClick = onSellClick,
                modifier = Modifier.height(48.dp),
                shape = RoundedCornerShape(50),
                contentPadding = PaddingValues(horizontal = 24.dp)
            ) {
                Icon(painter = painterResource(R.drawable.ic_nav_add), contentDescription = null)
                Spacer(modifier = Modifier.width(4.dp))
                Text("Sell", fontSize = 15.sp)
            }

            NavBarItem(
                icon = R.drawable.ic_nav_search,
                label = "Search",
                selected = false,
                onClick = onSearchClick
            )
        }
    }
}

@Composable
private fun NavBarItem(
    @DrawableRes icon: Int,
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    val tint = if (selected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(painter = painterResource(icon), contentDescription = label, tint = tint)
        Text(text = label, fontSize = 11.sp, color = tint)
    }
}