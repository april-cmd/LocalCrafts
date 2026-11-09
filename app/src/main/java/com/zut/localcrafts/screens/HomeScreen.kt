package com.zut.localcrafts.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.res.painterResource
import com.zut.localcrafts.R
import com.zut.localcrafts.ui.ProductImage
import com.zut.localcrafts.viewmodel.LocalCraftsViewModel
import com.zut.localcrafts.viewmodel.LocalCraftsViewModelFactory

// One product as the home screen shows it (from the database or the samples)
private data class ProductItem(
    val name: String,
    val artisan: String,
    val price: String,
    val category: String,
    val emoji: String,
    val imagePath: String? = null
)

// Category name + the emoji shown on its tile
private val categories = listOf(
    "All" to "✨",
    "Jewellery" to "📿",
    "Clothing" to "👗",
    "Pottery" to "🏺",
    "Baskets" to "🧺",
    "Decor" to "🪵"
)

private fun formatPrice(price: Double): String {
    return if (price % 1.0 == 0.0) "K${price.toInt()}" else "K${"%.2f".format(price)}"
}

@Composable
fun HomeScreen(
    onProductClick: (String) -> Unit,
    searchFocusTick: Int = 0,
    scrollToTopTick: Int = 0
) {
    val context = LocalContext.current
    val viewModel: LocalCraftsViewModel = viewModel(
        factory = LocalCraftsViewModelFactory(context)
    )

    val dbArtisans by viewModel.artisans.collectAsState()
    val dbProducts by viewModel.products.collectAsState()

    // rememberSaveable keeps the search and category when the phone is rotated
    var searchText by rememberSaveable { mutableStateOf("") }
    var selectedCategory by rememberSaveable { mutableStateOf("All") }

    val scrollState = rememberScrollState()
    val focusRequester = remember { FocusRequester() }

    // The bottom bar's Search button: scroll up and open the keyboard
    LaunchedEffect(searchFocusTick) {
        if (searchFocusTick > 0) {
            scrollState.animateScrollTo(0)
            focusRequester.requestFocus()
        }
    }

    // The bottom bar's Home button: scroll back to the top
    LaunchedEffect(scrollToTopTick) {
        if (scrollToTopTick > 0) {
            scrollState.animateScrollTo(0)
        }
    }

    val sampleProducts = listOf(
        ProductItem("Beaded Necklace", "Grace Crafts", "K120", "Jewellery", "📿"),
        ProductItem("Handwoven Basket", "Martha Designs", "K180", "Baskets", "🧺"),
        ProductItem("Clay Flower Vase", "Art by Peter", "K150", "Pottery", "🏺"),
        ProductItem("Traditional Bag", "Lydia Creations", "K200", "Clothing", "👜"),
        ProductItem("Wooden Decoration", "John's Workshop", "K100", "Decor", "🪵")
    )

    // Products artisans added (with their uploaded photo) come first
    val savedProducts = dbProducts.map { dbProduct ->
        val artisan = dbArtisans.find { it.artisanId == dbProduct.artisanId }
        val artisanName = artisan?.businessName?.ifBlank { artisan.fullName }
            ?: artisan?.fullName
            ?: "Local Artisan"
        ProductItem(
            name = dbProduct.productName,
            artisan = artisanName,
            price = formatPrice(dbProduct.price),
            category = dbProduct.category,
            emoji = "✨",
            imagePath = dbProduct.imageUri
        )
    }

    val allProducts = savedProducts + sampleProducts

    val filteredProducts = allProducts.filter { product ->
        val matchesSearch =
            product.name.contains(searchText, ignoreCase = true) ||
                    product.artisan.contains(searchText, ignoreCase = true)
        val matchesCategory =
            selectedCategory == "All" ||
                    product.category.equals(selectedCategory, ignoreCase = true)
        matchesSearch && matchesCategory
    }

    val showNewArrivals = searchText.isBlank() && selectedCategory == "All"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {

        // HEADER: soft colour glow, headline and search bar
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.tertiary.copy(alpha = 0.16f),
                            Color.Transparent
                        ),
                        center = Offset(0f, 100f),
                        radius = 800f
                    )
                )
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.07f),
                            Color.Transparent
                        ),
                        center = Offset(1100f, 450f),
                        radius = 700f
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                // End padding leaves room for the sun/moon theme button
                Text(
                    text = "LocalCrafts",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.padding(end = 56.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Your next handmade find\nis just a search away",
                    fontSize = 24.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.SemiBold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(22.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = MaterialTheme.colorScheme.surface,
                    tonalElevation = 2.dp,
                    shadowElevation = 4.dp
                ) {
                    Row(
                        modifier = Modifier.padding(start = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_nav_search),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        TextField(
                            value = searchText,
                            onValueChange = { searchText = it },
                            modifier = Modifier
                                .weight(1f)
                                .focusRequester(focusRequester),
                            placeholder = { Text("Search crafts or artisans") },
                            singleLine = true,
                            colors = TextFieldDefaults.colors(
                                focusedContainerColor = Color.Transparent,
                                unfocusedContainerColor = Color.Transparent,
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent
                            )
                        )
                        if (searchText.isNotEmpty()) {
                            IconButton(onClick = { searchText = "" }) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_nav_close),
                                    contentDescription = "Clear search"
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))
            }
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {

            // CATEGORY GRID (2 rows of 3)
            categories.chunked(3).forEach { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    rowItems.forEach { (name, emoji) ->
                        CategoryTile(
                            name = name,
                            emoji = emoji,
                            selected = selectedCategory == name,
                            onClick = { selectedCategory = name },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }

            // NEW ARRIVALS (only when nothing is being searched or filtered)
            if (showNewArrivals && allProducts.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "New arrivals",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    allProducts.take(6).forEach { item ->
                        Surface(
                            onClick = { onProductClick(item.name) },
                            modifier = Modifier.width(210.dp),
                            shape = RoundedCornerShape(20.dp),
                            color = MaterialTheme.colorScheme.surface,
                            tonalElevation = 1.dp,
                            shadowElevation = 2.dp
                        ) {
                            Column(modifier = Modifier.padding(8.dp)) {
                                ProductImage(
                                    path = item.imagePath,
                                    emoji = item.emoji,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(130.dp)
                                        .clip(RoundedCornerShape(14.dp)),
                                    emojiSize = 48.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = item.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "By ${item.artisan}",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = item.price,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }
                }
            }

            // PRODUCT LIST
            Spacer(modifier = Modifier.height(22.dp))

            Text(
                text = if (showNewArrivals) "Popular crafts" else "Results",
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (filteredProducts.isEmpty()) {
                // Empty state
                Text(
                    text = "No products found. Try another search or category.",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                filteredProducts.forEach { item ->
                    Surface(
                        onClick = { onProductClick(item.name) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 1.dp,
                        shadowElevation = 2.dp
                    ) {
                        Row(
                            modifier = Modifier.padding(10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            ProductImage(
                                path = item.imagePath,
                                emoji = item.emoji,
                                modifier = Modifier
                                    .size(84.dp)
                                    .clip(RoundedCornerShape(16.dp))
                            )

                            Spacer(modifier = Modifier.width(14.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 16.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = "By ${item.artisan}",
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = item.category,
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Text(
                                        text = item.price,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // ARTISANS
            if (dbArtisans.isNotEmpty()) {
                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "Meet the artisans",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(12.dp))

                dbArtisans.forEach { artisan ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 10.dp),
                        shape = RoundedCornerShape(20.dp),
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 1.dp,
                        shadowElevation = 2.dp
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = artisan.businessName.ifBlank { artisan.fullName },
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                            if (artisan.location.isNotBlank()) {
                                Text(
                                    text = artisan.location,
                                    fontSize = 13.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            if (artisan.description.isNotBlank()) {
                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = artisan.description,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // Space so the floating bottom bar never covers the last item
            Spacer(modifier = Modifier.height(120.dp))
        }
    }
}

@Composable
private fun CategoryTile(
    name: String,
    emoji: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = if (selected) {
            MaterialTheme.colorScheme.primaryContainer
        } else {
            MaterialTheme.colorScheme.surface
        },
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
            }
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = emoji, fontSize = 24.sp)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = name,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}