package com.zut.localcrafts.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zut.localcrafts.viewmodel.LocalCraftsViewModel
import com.zut.localcrafts.viewmodel.LocalCraftsViewModelFactory

private data class SampleProduct(
    val name: String,
    val artisan: String,
    val price: String,
    val category: String,
    val emoji: String
)

@Composable
fun HomeScreen(
    onProductClick: (String) -> Unit
) {
    val context = LocalContext.current
    val viewModel: LocalCraftsViewModel = viewModel(
        factory = LocalCraftsViewModelFactory(context)
    )

    val dbArtisans by viewModel.artisans.collectAsState()
    val dbProducts by viewModel.products.collectAsState()

    var searchText by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf(
        "All", "Jewellery", "Clothing", "Pottery",
        "Baskets", "Decor"
    )

    val sampleProductsList = listOf(
        SampleProduct("Beaded Necklace", "Grace Crafts", "K120", "Jewellery", "📿"),
        SampleProduct("Handwoven Basket", "Martha Designs", "K180", "Baskets", "🧺"),
        SampleProduct("Clay Flower Vase", "Art by Peter", "K150", "Pottery", "🏺"),
        SampleProduct("Traditional Bag", "Lydia Creations", "K200", "Clothing", "👜"),
        SampleProduct("Wooden Decoration", "John's Workshop", "K100", "Decor", "🪵")
    )

    val customDbProducts = dbProducts.map { dbProd ->
        val artisan = dbArtisans.find { it.artisanId == dbProd.artisanId }
        val artisanDisplayName = artisan?.businessName?.ifBlank { artisan.fullName }
            ?: artisan?.fullName
            ?: "Local Artisan"
        SampleProduct(
            name = dbProd.productName,
            artisan = artisanDisplayName,
            price = "K${dbProd.price}",
            category = dbProd.category,
            emoji = "✨"
        )
    }

    val allProducts = customDbProducts + sampleProductsList

    val filteredProducts = allProducts.filter { product ->
        val matchesSearch =
            product.name.contains(searchText, ignoreCase = true) ||
                    product.artisan.contains(searchText, ignoreCase = true)

        val matchesCategory =
            selectedCategory == "All" ||
                    product.category.equals(selectedCategory, ignoreCase = true)

        matchesSearch && matchesCategory
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "LocalCrafts",
            fontSize = 25.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Discover Local Creations",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Explore unique handmade products from talented local artisans."
        )

        Spacer(modifier = Modifier.height(22.dp))

        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Search crafts or artisans") },
            placeholder = { Text("What are you looking for?") },
            singleLine = true
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Browse Categories",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier.horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            categories.forEach { category ->

                FilterChip(
                    selected = selectedCategory == category,
                    onClick = { selectedCategory = category },
                    label = { Text(category) }
                )
            }
        }

        Spacer(modifier = Modifier.height(26.dp))

        Text(
            text = "Featured Products",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(14.dp))

        if (filteredProducts.isEmpty()) {
            Text(
                text = "No products found. Try another search."
            )
        } else {

            filteredProducts.forEach { product ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp)
                        .clickable {
                            onProductClick(product.name)
                        },
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(
                        defaultElevation = 3.dp
                    )
                ) {

                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(85.dp)
                                .background(
                                    MaterialTheme.colorScheme.surfaceVariant,
                                    RoundedCornerShape(12.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = product.emoji,
                                fontSize = 38.sp
                            )
                        }

                        Spacer(modifier = Modifier.width(16.dp))

                        Column(
                            modifier = Modifier.weight(1f)
                        ) {

                            Text(
                                text = product.name,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )

                            Spacer(modifier = Modifier.height(5.dp))

                            Text(
                                text = "By ${product.artisan}",
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = product.price,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Support Local Artisans",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Every handmade creation tells a story. Discover the people behind the products and support their creativity."
        )

        if (dbArtisans.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))

            dbArtisans.forEach { artisan ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp)
                    ) {
                        Text(
                            text = artisan.businessName.ifBlank { artisan.fullName },
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        )
                        if (artisan.fullName.isNotBlank()) {
                            Text(
                                text = "Artisan: ${artisan.fullName}",
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        if (artisan.location.isNotBlank()) {
                            Text(
                                text = "Location: ${artisan.location}",
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                        if (artisan.description.isNotBlank()) {
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = artisan.description,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
