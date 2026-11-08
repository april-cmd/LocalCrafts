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
    val products: List<String>
)

private val sampleArtisans = listOf(

    ArtisanListing(
        artisanName = "Grace Banda",
        businessName = "Grace Crafts",
        price = "K120",
        location = "Lusaka",
        description = "Handmade jewellery created with care and creativity.",
        contact = "Not provided yet",
        products = listOf(
            "Beaded Necklace", "Bracelets", "Earrings", "Anklets"
        )
    ),

    ArtisanListing(
        artisanName = "Martha Phiri",
        businessName = "Martha Designs",
        price = "K180",
        location = "Ndola",
        description = "Beautiful handmade crafts inspired by local traditions.",
        contact = "Not provided yet",
        products = listOf(
            "Handwoven Basket", "Decorations", "Handwoven Bags"
        )
    ),

    ArtisanListing(
        artisanName = "Peter",
        businessName = "Art by Peter",
        price = "K150",
        location = "Lusaka",
        description = "Unique handmade pottery and decorative pieces.",
        contact = "Not provided yet",
        products = listOf(
            "Clay Flower Vase", "Clay Pots", "Pottery"
        )
    ),

    ArtisanListing(
        artisanName = "Lydia Mwansa",
        businessName = "Lydia Creations",
        price = "K200",
        location = "Kitwe",
        description = "Traditional handmade bags and accessories.",
        contact = "Not provided yet",
        products = listOf(
            "Traditional Bag", "Accessories", "Handmade Bags"
        )
    ),

    ArtisanListing(
        artisanName = "John",
        businessName = "John's Workshop",
        price = "K100",
        location = "Lusaka",
        description = "Handcrafted wooden decorations made with care.",
        contact = "Not provided yet",
        products = listOf(
            "Wooden Decoration", "Wooden Crafts", "Decor"
        )
    )
)

@Composable
fun SearchScreen(productName: String) {

    // Find artisans who offer the selected product
    val matchingArtisans = sampleArtisans.filter { artisan ->
        artisan.products.any { product ->
            product.equals(productName, ignoreCase = true)
        }
    }

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

        if (matchingArtisans.isEmpty()) {

            Text(
                text = "No artisans currently offer this product.",
                style = MaterialTheme.typography.bodyLarge
            )

        } else {

            matchingArtisans.forEach { artisan ->

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

                            artisan.products
                                .filterNot {
                                    it.equals(productName, ignoreCase = true)
                                }
                                .forEach { product ->
                                    Text(text = "• $product")
                                }
                        }
                    }
                }
            }
        }
    }
}
