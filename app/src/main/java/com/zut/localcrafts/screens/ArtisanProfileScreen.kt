package com.zut.localcrafts.screens

import com.zut.localcrafts.data.Artisan
import com.zut.localcrafts.data.Product
import com.zut.localcrafts.viewmodel.LocalCraftsViewModel
import com.zut.localcrafts.viewmodel.LocalCraftsViewModelFactory
import java.util.UUID

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ArtisanProfileScreen(
    onExploreClick: () -> Unit
) {

    // Connect to the database through the ViewModel
    val context = LocalContext.current

    val viewModel: LocalCraftsViewModel = viewModel(
        factory = LocalCraftsViewModelFactory(context)
    )

    // Get products and artisans from Room Database
    val products by viewModel.products.collectAsState()
    val artisans by viewModel.artisans.collectAsState()

    val activeArtisan = artisans.lastOrNull()

    // Artisan information
    var artisanName by remember {
        mutableStateOf("")
    }

    var businessName by remember {
        mutableStateOf("")
    }

    var phoneNumber by remember {
        mutableStateOf("")
    }

    var location by remember {
        mutableStateOf("")
    }

    var speciality by remember {
        mutableStateOf("Handmade Jewellery")
    }

    var description by remember {
        mutableStateOf("")
    }

    LaunchedEffect(activeArtisan) {
        if (activeArtisan != null) {
            artisanName = activeArtisan.fullName
            businessName = activeArtisan.businessName
            phoneNumber = activeArtisan.phoneNumber
            location = activeArtisan.location
            description = activeArtisan.description
        } else {
            artisanName = "Grace Banda"
            businessName = "Grace Crafts"
            phoneNumber = "+260 971 234567"
            location = "Lusaka"
            description = "I create beautiful handmade jewellery inspired by local creativity."
        }
    }

    var selectedSection by remember {
        mutableStateOf("My Profile")
    }

    var profileMessage by remember {
        mutableStateOf("")
    }

    // Product dialog information
    var showProductDialog by remember {
        mutableStateOf(false)
    }

    var editingIndex by remember {
        mutableIntStateOf(-1)
    }

    var productName by remember {
        mutableStateOf("")
    }

    var productDescription by remember {
        mutableStateOf("")
    }

    var productCategory by remember {
        mutableStateOf("")
    }

    var productPrice by remember {
        mutableStateOf("")
    }

    var productMessage by remember {
        mutableStateOf("")
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
            text = "Artisan Dashboard",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text("Manage your business and handmade products.")

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onExploreClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Explore Crafts")
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Section navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {

            listOf("My Profile", "My Products").forEach { section ->

                Button(
                    onClick = {
                        selectedSection = section
                        profileMessage = ""
                        productMessage = ""
                    },
                    modifier = Modifier.weight(1f),
                    colors = if (selectedSection == section) {
                        ButtonDefaults.buttonColors()
                    } else {
                        ButtonDefaults.outlinedButtonColors()
                    },
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Text(section)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (selectedSection == "My Profile") {

            // PROFILE SECTION

            Text(
                text = "Business Information",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = artisanName,
                onValueChange = { artisanName = it },
                label = { Text("Artisan Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = businessName,
                onValueChange = { businessName = it },
                label = { Text("Business Name") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone / WhatsApp Number") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = speciality,
                onValueChange = { speciality = it },
                label = { Text("Craft Speciality") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("Business Description") },
                modifier = Modifier.fillMaxWidth(),
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
                        val artisanIdToUse = activeArtisan?.artisanId ?: UUID.randomUUID().toString()
                        val emailToUse = activeArtisan?.email ?: "artisan@localcrafts.com"

                        val updatedArtisan = Artisan(
                            artisanId = artisanIdToUse,
                            fullName = artisanName,
                            businessName = businessName,
                            email = emailToUse,
                            phoneNumber = phoneNumber,
                            location = location,
                            description = description
                        )

                        viewModel.saveArtisan(updatedArtisan)
                        profileMessage = "Profile updated successfully!"
                    }
                },
                modifier = Modifier.fillMaxWidth(0.75f)
            ) {
                Text("Save Profile")
            }

            if (profileMessage.isNotEmpty()) {
                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = profileMessage,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        } else {

            // PRODUCTS SECTION

            Text(
                text = "My Products",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text("Add and manage the products you showcase.")

            Spacer(modifier = Modifier.height(18.dp))

            // Add product button
            Button(
                onClick = {
                    editingIndex = -1
                    productName = ""
                    productDescription = ""
                    productCategory = ""
                    productPrice = ""
                    productMessage = ""
                    showProductDialog = true
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("+ Add New Product")
            }

            Spacer(modifier = Modifier.height(18.dp))

            if (products.isEmpty()) {
                Text("You haven't added any products yet.")
            }

            // Display products from database
            products.forEachIndexed { index, product ->

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 14.dp),
                    shape = RoundedCornerShape(14.dp)
                ) {

                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            text = product.productName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(product.description)

                        Spacer(modifier = Modifier.height(6.dp))

                        Text("Category: ${product.category}")

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "K${product.price}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {

                            // Edit product
                            OutlinedButton(
                                onClick = {
                                    editingIndex = index
                                    productName = product.productName
                                    productDescription = product.description
                                    productCategory = product.category
                                    productPrice = product.price.toString()
                                    productMessage = ""
                                    showProductDialog = true
                                }
                            ) {
                                Text("Edit")
                            }

                            // Delete product
                            OutlinedButton(
                                onClick = {
                                    viewModel.deleteProduct(product)
                                    productMessage = "Product deleted."
                                }
                            ) {
                                Text("Delete")
                            }
                        }
                    }
                }
            }

            if (productMessage.isNotEmpty()) {
                Text(
                    text = productMessage,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }

    // ADD AND EDIT PRODUCT DIALOG

    if (showProductDialog) {

        AlertDialog(

            onDismissRequest = {
                showProductDialog = false
            },

            title = {
                Text(
                    if (editingIndex == -1) {
                        "Add Product"
                    } else {
                        "Edit Product"
                    }
                )
            },

            text = {

                Column(
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    OutlinedTextField(
                        value = productName,
                        onValueChange = { productName = it },
                        label = { Text("Product Name") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = productDescription,
                        onValueChange = { productDescription = it },
                        label = { Text("Description") }
                    )

                    OutlinedTextField(
                        value = productCategory,
                        onValueChange = { productCategory = it },
                        label = { Text("Category") },
                        singleLine = true
                    )

                    OutlinedTextField(
                        value = productPrice,
                        onValueChange = { productPrice = it },
                        label = { Text("Price (K)") },
                        singleLine = true
                    )
                    if (productMessage.isNotEmpty()) {
                        Text(
                            text = productMessage,
                            color = MaterialTheme.colorScheme.error
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

                            val newProduct = Product(

                                productId = if (editingIndex == -1) {
                                    UUID.randomUUID().toString()
                                } else {
                                    products[editingIndex].productId
                                },

                                artisanId = if (editingIndex == -1) {
                                    activeArtisan?.artisanId ?: "ART001"
                                } else {
                                    products[editingIndex].artisanId
                                },

                                productName = productName,
                                description = productDescription,
                                category = productCategory,
                                price = priceValue
                            )

                            if (editingIndex == -1) {

                                viewModel.saveProduct(newProduct)

                                productMessage =
                                    "Product added successfully!"

                            } else {

                                viewModel.updateProduct(newProduct)

                                productMessage =
                                    "Product updated successfully!"
                            }

                            showProductDialog = false

                        } else {

                            productMessage =
                                "Please enter all details and a valid price."
                        }
                    }
                ) {
                    Text("Save")
                }
            },

            dismissButton = {

                TextButton(
                    onClick = {
                        showProductDialog = false
                    }
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}