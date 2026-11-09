package com.zut.localcrafts.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zut.localcrafts.viewmodel.LocalCraftsViewModel
import com.zut.localcrafts.viewmodel.LocalCraftsViewModelFactory

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
        contact = "+260 971 234567 | grace@crafts.com",
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
        contact = "+260 966 876543",
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
        contact = "+260 955 112233",
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
        contact = "+260 977 445566",
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
        contact = "+260 962 998877",
        products = listOf(
            "Wooden Decoration", "Wooden Crafts", "Decor"
        )
    )
)

@Composable
fun ProductComparisonScreen(productName: String) {

    val context = LocalContext.current
    val viewModel: LocalCraftsViewModel = viewModel(
        factory = LocalCraftsViewModelFactory(context)
    )

    val dbArtisans by viewModel.artisans.collectAsState()
    val dbProducts by viewModel.products.collectAsState()

    val dbListings = dbProducts
        .filter { it.productName.contains(productName, ignoreCase = true) }
        .map { prod ->
            val artisan = dbArtisans.find { it.artisanId == prod.artisanId }
            val otherProducts = dbProducts
                .filter { it.artisanId == prod.artisanId }
                .map { it.productName }

            val contactInfo = listOfNotNull(
                artisan?.phoneNumber?.takeIf { it.isNotBlank() },
                artisan?.email?.takeIf { it.isNotBlank() }
            ).joinToString(" | ").ifBlank { "Not provided yet" }

            if (artisan != null) {
                ArtisanListing(
                    artisanName = artisan.fullName,
                    businessName = artisan.businessName.ifBlank { artisan.fullName },
                    price = "K${prod.price}",
                    location = artisan.location,
                    description = artisan.description,
                    contact = contactInfo,
                    products = if (otherProducts.isNotEmpty()) otherProducts else listOf(prod.productName)
                )
            } else {
                ArtisanListing(
                    artisanName = "Local Artisan",
                    businessName = "Artisan Store",
                    price = "K${prod.price}",
                    location = "Lusaka",
                    description = prod.description,
                    contact = "Not provided yet",
                    products = listOf(prod.productName)
                )
            }
        }

    val sampleMatches = sampleArtisans.filter { artisan ->
        artisan.products.any { product ->
            product.contains(productName, ignoreCase = true)
        }
    }

    val matchingArtisans = (dbListings + sampleMatches).distinctBy { it.businessName }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
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
                                text = "Contact Information",
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
