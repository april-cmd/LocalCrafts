package com.zut.localcrafts.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Logout
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.DeleteOutline
import androidx.compose.material.icons.outlined.Description
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.LocationOn
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material.icons.outlined.Brush
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zut.localcrafts.data.Product
import com.zut.localcrafts.ui.ImageStorage
import com.zut.localcrafts.ui.ProductImage
import com.zut.localcrafts.ui.theme.craftFieldColors
import com.zut.localcrafts.viewmodel.LocalCraftsViewModel
import com.zut.localcrafts.viewmodel.LocalCraftsViewModelFactory
import java.util.Locale
import java.util.UUID
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun ArtisanProfileScreen(
    artisanId: String,
    onExploreClick: () -> Unit,
    onLogout: () -> Unit
) {

    // Connect to the database through the ViewModel
    val context = LocalContext.current

    val viewModel: LocalCraftsViewModel = viewModel(
        factory = LocalCraftsViewModelFactory(context)
    )

    val allProducts by viewModel.products.collectAsState()
    val artisans by viewModel.artisans.collectAsState()

    // Only the artisan who logged in, and only their own products
    val artisan = artisans.firstOrNull { it.artisanId == artisanId }
    val myProducts = allProducts.filter { it.artisanId == artisanId }

    // Editable business information
    var artisanName by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var speciality by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }

    LaunchedEffect(artisan) {
        if (artisan != null) {
            artisanName = artisan.fullName
            businessName = artisan.businessName
            phoneNumber = artisan.phoneNumber
            location = artisan.location
            speciality = artisan.speciality
            description = artisan.description
        }
    }

    var selectedSection by rememberSaveable { mutableStateOf("My Profile") }
    var profileMessage by remember { mutableStateOf("") }
    var productNotice by remember { mutableStateOf("") }

    // Product dialog state
    var showProductDialog by rememberSaveable { mutableStateOf(false) }
    var editingProductId by rememberSaveable { mutableStateOf<String?>(null) }
    var productName by rememberSaveable { mutableStateOf("") }
    var productDescription by rememberSaveable { mutableStateOf("") }
    var productCategory by rememberSaveable { mutableStateOf("") }
    var productPrice by rememberSaveable { mutableStateOf("") }
    var productImagePath by rememberSaveable { mutableStateOf<String?>(null) }
    var dialogError by remember { mutableStateOf("") }
    var productToDelete by remember { mutableStateOf<Product?>(null) }

    val scope = rememberCoroutineScope()

    // Opens the phone's photo picker. No storage permission is needed.
    val photoPicker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) {
            scope.launch {
                val savedPath = withContext(Dispatchers.IO) {
                    ImageStorage.saveFromUri(context, uri)
                }
                if (savedPath != null) {
                    productImagePath = savedPath
                } else {
                    dialogError = "Could not load that photo. Please try another."
                }
            }
        }
    }

    val openAddProduct = {
        editingProductId = null
        productImagePath = null
        productName = ""
        productDescription = ""
        productCategory = ""
        productPrice = ""
        dialogError = ""
        productNotice = ""
        selectedSection = "My Products"
        showProductDialog = true
    }

    // The profile has not loaded yet (or this account no longer exists)
    if (artisan == null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CircularProgressIndicator(color = MaterialTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Loading your profile...",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            TextButton(onClick = onLogout) { Text("Back to start") }
        }
        return
    }

    // Numbers shown under the name
    val productCount = myProducts.size
    val categoryCount = myProducts.map { it.category.trim().lowercase() }.distinct().size
    val averagePrice = if (myProducts.isEmpty()) null else myProducts.map { it.price }.average()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
    ) {

        // HEADER: cover, avatar, name and handle
        Box(modifier = Modifier.fillMaxWidth()) {

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.linearGradient(
                            listOf(Color(0xFF353B43), Color(0xFF0E1013))
                        )
                    )
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.tertiary.copy(alpha = 0.35f),
                                Color.Transparent
                            ),
                            center = Offset(900f, 0f),
                            radius = 700f
                        )
                    )
                    .statusBarsPadding()
            ) {
                Spacer(modifier = Modifier.height(132.dp))
            }

            // Log out button, top-left like the back button in the reference
            Surface(
                onClick = onLogout,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .statusBarsPadding()
                    .padding(top = 8.dp, start = 16.dp)
                    .size(40.dp),
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.16f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.Logout,
                        contentDescription = "Log out",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            // Avatar overlapping the cover
            Surface(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = 44.dp)
                    .size(88.dp),
                shape = CircleShape,
                color = MaterialTheme.colorScheme.tertiary,
                border = BorderStroke(4.dp, MaterialTheme.colorScheme.background),
                shadowElevation = 4.dp
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = initialsOf(artisan.businessName, artisan.fullName),
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onTertiary
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(54.dp))

        Text(
            text = artisan.businessName,
            fontSize = 22.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 24.dp)
        )

        Text(
            text = "@" + artisan.businessName.lowercase().filter { it.isLetterOrDigit() },
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )

        val subtitle = listOf(artisan.speciality, artisan.location)
            .filter { it.isNotBlank() }
            .joinToString("  •  ")
        if (subtitle.isNotEmpty()) {
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                fontSize = 13.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // STATS ROW
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            StatItem(
                value = productCount.toString(),
                label = "Products",
                modifier = Modifier.weight(1f)
            )
            StatDivider()
            StatItem(
                value = categoryCount.toString(),
                label = "Categories",
                modifier = Modifier.weight(1f)
            )
            StatDivider()
            StatItem(
                value = if (averagePrice == null) "-" else formatPrice(averagePrice),
                label = "Avg. price",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(20.dp))

        // ACTION BUTTONS
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Button(
                onClick = onExploreClick,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(24.dp)
            ) {
                Text("Explore Crafts", fontWeight = FontWeight.SemiBold)
            }

            OutlinedButton(
                onClick = openAddProduct,
                modifier = Modifier
                    .weight(1f)
                    .height(46.dp),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Add,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text("Add Product", fontWeight = FontWeight.SemiBold)
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // TABS (underlined, like the reference)
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("My Profile", "My Products").forEach { section ->
                val selected = selectedSection == section
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .clickable {
                            selectedSection = section
                            profileMessage = ""
                            productNotice = ""
                        },
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = section,
                        fontSize = 15.sp,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal,
                        color = if (selected) MaterialTheme.colorScheme.onSurface
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(vertical = 14.dp)
                    )
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(2.dp)
                            .background(
                                if (selected) MaterialTheme.colorScheme.tertiary
                                else MaterialTheme.colorScheme.outline.copy(alpha = 0.6f)
                            )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {

            if (selectedSection == "My Profile") {

                // PROFILE SECTION
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = MaterialTheme.colorScheme.surface,
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                ) {
                    Column(modifier = Modifier.padding(18.dp)) {

                        Text(
                            text = "Business Information",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(2.dp))

                        Text(
                            text = "Signed in as ${artisan.email}",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        ProfileField(artisanName, { artisanName = it }, "Artisan Name", Icons.Outlined.Person)
                        Spacer(modifier = Modifier.height(12.dp))
                        ProfileField(businessName, { businessName = it }, "Business Name", Icons.Outlined.Storefront)
                        Spacer(modifier = Modifier.height(12.dp))
                        ProfileField(phoneNumber, { phoneNumber = it }, "Phone / WhatsApp Number", Icons.Outlined.Phone)
                        Spacer(modifier = Modifier.height(12.dp))
                        ProfileField(location, { location = it }, "Location", Icons.Outlined.LocationOn)
                        Spacer(modifier = Modifier.height(12.dp))
                        ProfileField(speciality, { speciality = it }, "Craft Speciality", Icons.Outlined.Brush)
                        Spacer(modifier = Modifier.height(12.dp))
                        ProfileField(
                            value = description,
                            onValueChange = { description = it },
                            label = "Business Description",
                            icon = Icons.Outlined.Description,
                            singleLine = false,
                            minLines = 3
                        )

                        Spacer(modifier = Modifier.height(20.dp))

                        Button(
                            onClick = {
                                if (
                                    artisanName.isBlank() ||
                                    businessName.isBlank() ||
                                    phoneNumber.isBlank() ||
                                    location.isBlank() ||
                                    speciality.isBlank() ||
                                    description.isBlank()
                                ) {
                                    profileMessage = "Please fill in all the fields."
                                } else {
                                    // copy() keeps the email and the saved password
                                    viewModel.saveArtisan(
                                        artisan.copy(
                                            fullName = artisanName.trim(),
                                            businessName = businessName.trim(),
                                            phoneNumber = phoneNumber.trim(),
                                            location = location.trim(),
                                            speciality = speciality.trim(),
                                            description = description.trim()
                                        )
                                    )
                                    profileMessage = "Profile updated successfully!"
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Save Changes", fontWeight = FontWeight.SemiBold)
                        }

                        if (profileMessage.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            val isError = profileMessage.startsWith("Please")
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (!isError) {
                                    Icon(
                                        imageVector = Icons.Outlined.CheckCircle,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.tertiary,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                }
                                Text(
                                    text = profileMessage,
                                    fontSize = 14.sp,
                                    color = if (isError) MaterialTheme.colorScheme.error
                                    else MaterialTheme.colorScheme.tertiary
                                )
                            }
                        }
                    }
                }

            } else {

                // PRODUCTS SECTION
                Text(
                    text = "My Products ($productCount)",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Add and manage the products you showcase.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                if (productNotice.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Outlined.CheckCircle,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = productNotice,
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (myProducts.isEmpty()) {
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = MaterialTheme.colorScheme.surface,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
                    ) {
                        Column(
                            modifier = Modifier.padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Storefront,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            Text(
                                text = "No products yet",
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Add your first product so customers can find your work.",
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }

                // Two products per row
                myProducts.chunked(2).forEach { rowProducts ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 14.dp),
                        horizontalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        rowProducts.forEach { product ->
                            ProductTile(
                                product = product,
                                modifier = Modifier.weight(1f),
                                onEdit = {
                                    editingProductId = product.productId
                                    productName = product.productName
                                    productDescription = product.description
                                    productCategory = product.category
                                    productPrice = if (product.price % 1.0 == 0.0) product.price.toLong().toString() else product.price.toString()
                                    productImagePath = product.imageUri
                                    dialogError = ""
                                    productNotice = ""
                                    showProductDialog = true
                                },
                                onDelete = { productToDelete = product }
                            )
                        }
                        if (rowProducts.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }

    // ADD AND EDIT PRODUCT DIALOG
    if (showProductDialog) {

        AlertDialog(
            onDismissRequest = { showProductDialog = false },

            title = {
                Text(if (editingProductId == null) "Add Product" else "Edit Product")
            },

            text = {
                Column(
                    modifier = Modifier.verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        label = { Text("Product Name") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = craftFieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = productDescription,
                        onValueChange = { productDescription = it },
                        label = { Text("Description") },
                        modifier = Modifier.fillMaxWidth(),
                        colors = craftFieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = productCategory,
                        onValueChange = { productCategory = it },
                        label = { Text("Category") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = craftFieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = productPrice,
                        onValueChange = { productPrice = it },
                        label = { Text("Price (K)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth(),
                        colors = craftFieldColors(),
                        shape = RoundedCornerShape(12.dp)
                    )

                    // Product photo
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (productImagePath.isNullOrBlank()) {
                            ImagePlaceholder(
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(12.dp))
                            )
                        } else {
                            ProductImage(
                                path = productImagePath,
                                emoji = "",
                                modifier = Modifier
                                    .size(72.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                emojiSize = 28.sp
                            )
                        }

                        OutlinedButton(
                            onClick = {
                                photoPicker.launch(
                                    PickVisualMediaRequest(
                                        ActivityResultContracts.PickVisualMedia.ImageOnly
                                    )
                                )
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text(
                                if (productImagePath.isNullOrBlank()) "Add photo" else "Change photo"
                            )
                        }
                    }

                    if (dialogError.isNotEmpty()) {
                        Text(
                            text = dialogError,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 13.sp
                        )
                    }
                }
            },

            confirmButton = {
                TextButton(
                    onClick = {
                        val priceValue = productPrice.toDoubleOrNull()

                        if (
                            productName.isNotBlank() &&
                            productDescription.isNotBlank() &&
                            productCategory.isNotBlank() &&
                            priceValue != null &&
                            priceValue > 0
                        ) {
                            val isNew = editingProductId == null

                            val product = Product(
                                productId = editingProductId ?: UUID.randomUUID().toString(),
                                artisanId = artisan.artisanId,
                                productName = productName.trim(),
                                description = productDescription.trim(),
                                category = productCategory.trim(),
                                price = priceValue,
                                imageUri = productImagePath
                            )

                            if (isNew) {
                                viewModel.saveProduct(product)
                                productNotice = "Product added successfully!"
                            } else {
                                viewModel.updateProduct(product)
                                productNotice = "Product updated successfully!"
                            }

                            showProductDialog = false
                        } else {
                            dialogError = "Please enter all details and a valid price."
                        }
                    }
                ) {
                    Text("Save", fontWeight = FontWeight.SemiBold)
                }
            },

            dismissButton = {
                TextButton(onClick = { showProductDialog = false }) {
                    Text("Cancel")
                }
            }
        )
    }

    // DELETE CONFIRMATION
    productToDelete?.let { product ->
        AlertDialog(
            onDismissRequest = { productToDelete = null },
            title = { Text("Delete product?") },
            text = { Text("\"${product.productName}\" will be removed from your products.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.deleteProduct(product)
                        productNotice = "Product deleted."
                        productToDelete = null
                    },
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text("Delete", fontWeight = FontWeight.SemiBold)
                }
            },
            dismissButton = {
                TextButton(onClick = { productToDelete = null }) {
                    Text("Cancel")
                }
            }
        )
    }
}

// ---------- Small building blocks ----------

@Composable
private fun StatItem(value: String, label: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1
        )
        Text(
            text = label,
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun StatDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(32.dp)
            .background(MaterialTheme.colorScheme.outline)
    )
}

@Composable
private fun ProfileField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    icon: ImageVector,
    singleLine: Boolean = true,
    minLines: Int = 1
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        leadingIcon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
                modifier = Modifier.size(20.dp)
            )
        },
        singleLine = singleLine,
        minLines = minLines,
        modifier = Modifier.fillMaxWidth(),
        colors = craftFieldColors(),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
private fun ImagePlaceholder(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Image,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProductTile(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        color = MaterialTheme.colorScheme.surface,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline)
    ) {
        Column {
            val imageModifier = Modifier
                .fillMaxWidth()
                .height(120.dp)

            if (product.imageUri.isNullOrBlank()) {
                ImagePlaceholder(imageModifier)
            } else {
                ProductImage(
                    path = product.imageUri,
                    emoji = "",
                    modifier = imageModifier
                )
            }

            Column(modifier = Modifier.padding(start = 12.dp, end = 4.dp, top = 10.dp)) {
                Text(
                    text = product.productName,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = product.category,
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.tertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 12.dp, end = 2.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatPrice(product.price),
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = onEdit, modifier = Modifier.size(40.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.Edit,
                        contentDescription = "Edit ${product.productName}",
                        modifier = Modifier.size(19.dp)
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(40.dp)) {
                    Icon(
                        imageVector = Icons.Outlined.DeleteOutline,
                        contentDescription = "Delete ${product.productName}",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(19.dp)
                    )
                }
            }
        }
    }
}

private fun initialsOf(businessName: String, fullName: String): String {
    val source = businessName.ifBlank { fullName }
    val letters = source.trim().split(" ")
        .filter { it.isNotBlank() }
        .take(2)
        .map { it.first().uppercaseChar() }
    return if (letters.isEmpty()) "A" else letters.joinToString("")
}

/** K150, or K149.50 when the price has cents. */
private fun formatPrice(price: Double): String {
    return "K" + formatPlain(price)
}

private fun formatPlain(price: Double): String {
    return if (price % 1.0 == 0.0) {
        String.format(Locale.US, "%,.0f", price)
    } else {
        String.format(Locale.US, "%,.2f", price)
    }
}
