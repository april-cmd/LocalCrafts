
package com.zut.localcrafts.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private data class ArtisanListing(
    val artisanName: String,
    val businessName: String,
    val price: String,
    val location: String,
    val description: String,
    val contact: String,
    val otherProducts: List<String>
)

private val sampleArtisans = listOf(
    ArtisanListing(
        artisanName = "Grace Banda",
        businessName = "Grace Crafts",
        price = "K120",
        location = "Lusaka",
        description = "Handmade jewellery created with care and creativity.",
        contact = "Not provided yet",
        otherProducts = listOf("Bracelets", "Earrings", "Anklets")
    ),
    ArtisanListing(
        artisanName = "Martha Phiri",
        businessName = "Martha Designs",
        price = "K150",
        location = "Ndola",
        description = "Beautiful handmade crafts inspired by local traditions.",
        contact = "Not provided yet",
        otherProducts = listOf("Baskets", "Decorations", "Handwoven bags")
    ),
    ArtisanListing(
        artisanName = "Lydia Mwansa",
        businessName = "Lydia Creations",
        price = "K100",
        location = "Kitwe",
        description = "Unique handmade products made by a local artisan.",
        contact = "Not provided yet",
        otherProducts = listOf("Necklaces", "Traditional bags", "Accessories")
    )
)

@Composable
fun ProductComparisonScreen(productName: String) {

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {

        Text(
            text = "Artisans offering $productName",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Compare prices and discover local artisans.",
            style = MaterialTheme.typography.bodyMedium
        )

        Spacer(modifier = Modifier.height(20.dp))

        sampleArtisans.forEach { artisan ->

            var expanded by remember(artisan.artisanName) {
                mutableStateOf(false)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 3.dp
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Text(
                        text = artisan.businessName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(text = "Artisan: ${artisan.artisanName}")

                    Text(text = "Price: ${artisan.price}")

                    Text(text = "Location: ${artisan.location}")

                    Spacer(modifier = Modifier.height(8.dp))

                    TextButton(
                        onClick = {
                            expanded = !expanded
                        }
                    ) {
                        Text(
                            text = if (expanded) {
                                "Show less ▲"
                            } else {
                                "View artisan details ▼"
                            }
                        )
                    }

                    if (expanded) {

                        HorizontalDivider()

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "About the business",
                            fontWeight = FontWeight.Bold
                        )

                        Text(text = artisan.description)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Contact",
                            fontWeight = FontWeight.Bold
                        )

                        Text(text = artisan.contact)

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Other products",
                            fontWeight = FontWeight.Bold
                        )

                        artisan.otherProducts.forEach { product ->
                            Text(text = "• $product")
                        }
                    }
                }
            }
        }
    }
}
