package com.zut.localcrafts.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class ArtisanProduct(
    val name: String,
    val description: String,
    val price: String
)

@Composable
fun ArtisanProfileScreen(
    onExploreClick: () -> Unit
) {

    var selectedSection by remember { mutableStateOf("My Profile") }

    // Artisan information
    var artisanName by remember { mutableStateOf("Grace Banda") }
    var businessName by remember { mutableStateOf("Grace Crafts") }
    var location by remember { mutableStateOf("Lusaka") }
    var speciality by remember { mutableStateOf("Handmade Jewellery") }
    var description by remember {
        mutableStateOf("I create beautiful handmade jewellery inspired by local creativity.")
    }

    var profileMessage by remember { mutableStateOf("") }

    // Sample products
    val products = remember {
        mutableStateListOf(
            ArtisanProduct(
                "Beaded Necklace",
                "Colourful handmade necklace.",
                "120"
            ),
            ArtisanProduct(
                "Bracelet Set",
                "Handcrafted beaded bracelets.",
                "80"
            )
        )
    }

    // Product dialog information
    var showProductDialog by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableIntStateOf(-1) }

    var productName by remember { mutableStateOf("") }
    var productDescription by remember { mutableStateOf("") }
    var productPrice by remember { mutableStateOf("") }
    var productMessage by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {

        Text(
            text = "Artisan Dashboard",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Manage your business and handmade products."
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = onExploreClick,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Explore Crafts")
        }

        Spacer(modifier = Modifier.height(24.dp)

        )

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

        // Dynamic content

        if (selectedSection == "My Profile") {

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
                    profileMessage = "Profile updated successfully!"
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

            Text(
                text = "My Products",
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Add and manage the products you showcase."
            )

            Spacer(modifier = Modifier.height(18.dp))

            Button(
                onClick = {
                    editingIndex = -1
                    productName = ""
                    productDescription = ""
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
                            text = product.name,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(product.description)

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

                            OutlinedButton(
                                onClick = {
                                    editingIndex = index
                                    productName = product.name
                                    productDescription = product.description
                                    productPrice = product.price
                                    productMessage = ""
                                    showProductDialog = true
                                }
                            ) {
                                Text("Edit")
                            }

                            OutlinedButton(
                                onClick = {
                                    products.removeAt(index)
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

    // Add and edit product dialog

    if (showProductDialog) {

        AlertDialog(
            onDismissRequest = {
                showProductDialog = false
            },
            title = {
                Text(
                    if (editingIndex == -1) "Add Product"
                    else "Edit Product"
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
                        value = productPrice,
                        onValueChange = { productPrice = it },
                        label = { Text("Price (K)") },
                        singleLine = true
                    )
                }
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (
                            productName.isNotBlank() &&
                            productDescription.isNotBlank() &&
                            productPrice.isNotBlank()
                        ) {

                            val newProduct = ArtisanProduct(
                                productName,
                                productDescription,
                                productPrice
                            )

                            if (editingIndex == -1) {
                                products.add(newProduct)
                                productMessage = "Product added successfully!"
                            } else {
                                products[editingIndex] = newProduct
                                productMessage = "Product updated successfully!"
                            }

                            showProductDialog = false
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
