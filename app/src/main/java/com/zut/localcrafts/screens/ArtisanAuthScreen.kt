package com.zut.localcrafts.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Visibility
import androidx.compose.material.icons.outlined.VisibilityOff
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.zut.localcrafts.data.Artisan
import com.zut.localcrafts.ui.theme.craftFieldColors
import com.zut.localcrafts.viewmodel.LocalCraftsViewModel
import com.zut.localcrafts.viewmodel.LocalCraftsViewModelFactory
import java.util.UUID

@Composable
fun ArtisanAuthScreen(
    onLoginSuccess: (artisanId: String) -> Unit
) {

    val context = LocalContext.current
    val viewModel: LocalCraftsViewModel = viewModel(
        factory = LocalCraftsViewModelFactory(context)
    )

    var isRegistering by remember { mutableStateOf(false) }

    var fullName by remember { mutableStateOf("") }
    var businessName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var message by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var showPassword by remember { mutableStateOf(false) }

    val passwordTransformation =
        if (showPassword) VisualTransformation.None else PasswordVisualTransformation()
    val visibilityToggle: @Composable () -> Unit = {
        IconButton(onClick = { showPassword = !showPassword }) {
            Icon(
                imageVector = if (showPassword) Icons.Outlined.VisibilityOff
                else Icons.Outlined.Visibility,
                contentDescription = if (showPassword) "Hide password" else "Show password"
            )
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .statusBarsPadding()
            .navigationBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            text = if (isRegistering) "Join LocalCrafts"
            else "Welcome Back, Artisan!",
            fontSize = 27.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = if (isRegistering)
                "Create an account and showcase your handmade products."
            else
                "Log in to manage your artisan profile."
        )

        Spacer(modifier = Modifier.height(28.dp))

        if (isRegistering) {

            OutlinedTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = { Text("Full Name") },
                modifier = Modifier.fillMaxWidth(),
            colors = craftFieldColors(),
            shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = businessName,
                onValueChange = { businessName = it },
                label = { Text("Business Name") },
                modifier = Modifier.fillMaxWidth(),
            colors = craftFieldColors(),
            shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            modifier = Modifier.fillMaxWidth(),
            colors = craftFieldColors(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isRegistering) {

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = { phoneNumber = it },
                label = { Text("Phone / WhatsApp Number") },
                modifier = Modifier.fillMaxWidth(),
            colors = craftFieldColors(),
            shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = location,
                onValueChange = { location = it },
                label = { Text("Location") },
                modifier = Modifier.fillMaxWidth(),
            colors = craftFieldColors(),
            shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedTextField(
                value = description,
                onValueChange = { description = it },
                label = { Text("What do you create?") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(12.dp))
        }

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = passwordTransformation,
            trailingIcon = visibilityToggle,
            modifier = Modifier.fillMaxWidth(),
            colors = craftFieldColors(),
            shape = RoundedCornerShape(12.dp),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(12.dp))

        if (isRegistering) {

            OutlinedTextField(
                value = confirmPassword,
                onValueChange = { confirmPassword = it },
                label = { Text("Confirm Password") },
                visualTransformation = passwordTransformation,
                modifier = Modifier.fillMaxWidth(),
            colors = craftFieldColors(),
            shape = RoundedCornerShape(12.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))
        } else {
            Spacer(modifier = Modifier.height(20.dp))
        }

        Button(
            onClick = {
                val errorMessage = when {
                    email.isBlank() || password.isBlank() ->
                        "Please enter your email and password."

                    !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() ->
                        "Please enter a valid email address."

                    isRegistering && password.length < 8 ->
                        "Password must contain at least 8 characters."

                    isRegistering && (
                            fullName.isBlank() ||
                                    businessName.isBlank() ||
                                    phoneNumber.isBlank() ||
                                    location.isBlank() ||
                                    description.isBlank()
                            ) -> "Please complete all registration fields."

                    isRegistering && password != confirmPassword ->
                        "Your passwords do not match."

                    else -> ""
                }

                message = errorMessage

                if (errorMessage.isEmpty()) {
                    isLoading = true

                    if (isRegistering) {
                        val newArtisan = Artisan(
                            artisanId = UUID.randomUUID().toString(),
                            fullName = fullName.trim(),
                            businessName = businessName.trim(),
                            email = email.trim(),
                            phoneNumber = phoneNumber.trim(),
                            location = location.trim(),
                            description = description.trim()
                        )
                        viewModel.register(newArtisan, password) { created ->
                            isLoading = false
                            if (created != null) {
                                onLoginSuccess(created.artisanId)
                            } else {
                                message = "An account with this email already exists. Please log in."
                            }
                        }
                    } else {
                        viewModel.login(email, password) { artisan ->
                            isLoading = false
                            if (artisan != null) {
                                onLoginSuccess(artisan.artisanId)
                            } else {
                                // Same message for a wrong email or a wrong password
                                message = "Incorrect email or password."
                            }
                        }
                    }
                }
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth(0.75f)
        ) {
            Text(
                text = if (isLoading) "Please wait..."
                else if (isRegistering) "Create Account" else "Login"
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (message.isNotEmpty()) {
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error
            )

            Spacer(modifier = Modifier.height(8.dp))
        }

        TextButton(
            onClick = {
                isRegistering = !isRegistering
                message = ""
            }
        ) {
            Text(
                text = if (isRegistering)
                    "Already have an account? Login"
                else
                    "New artisan? Create an account"
            )
        }
    }
}
