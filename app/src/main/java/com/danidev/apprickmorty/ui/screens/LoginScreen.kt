package com.danidev.apprickmorty.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danidev.apprickmorty.R
import com.danidev.apprickmorty.ui.auth.AuthViewModel

// ---------- Componentes compartidos (también los usa ProfileScreen) ----------

@Composable
fun AuthBackground(content: @Composable BoxScope.() -> Unit) {
    Box(Modifier.fillMaxSize().background(DarkBackground)) {
        Image(
            painter = painterResource(R.drawable.splash_background),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().blur(16.dp),
            contentScale = ContentScale.Crop
        )
        Box(Modifier.fillMaxSize().background(Color.Black.copy(alpha = 0.72f)))
        content()
    }
}

@Composable
fun GlassCard(modifier: Modifier = Modifier, content: @Composable ColumnScope.() -> Unit) {
    val shape = RoundedCornerShape(20.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(Color.Black.copy(alpha = 0.45f))
            .border(1.dp, Color.White.copy(alpha = 0.15f), shape)
            .padding(20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        content = content
    )
}

@Composable
fun AuthField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    isPassword: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text
) {
    var visible by remember { mutableStateOf(false) }
    Column(Modifier.fillMaxWidth()) {
        Text(label, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            keyboardOptions = KeyboardOptions(keyboardType = if (isPassword) KeyboardType.Password else keyboardType),
            visualTransformation = if (isPassword && !visible) PasswordVisualTransformation() else VisualTransformation.None,
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { visible = !visible }) {
                        Icon(
                            if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                            contentDescription = "Mostrar u ocultar contraseña",
                            tint = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }
            } else null,
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.White,
                unfocusedTextColor = Color.White,
                cursorColor = PortalGreen,
                focusedBorderColor = PortalGreen,
                unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                focusedContainerColor = Color.White.copy(alpha = 0.06f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.06f)
            )
        )
    }
}

@Composable
fun PortalButton(text: String, enabled: Boolean = true, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = Modifier.fillMaxWidth().height(48.dp),
        shape = RoundedCornerShape(10.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PortalGreen,
            contentColor = Color.White,
            disabledContainerColor = PortalGreen.copy(alpha = 0.35f),
            disabledContentColor = Color.White.copy(alpha = 0.6f)
        )
    ) {
        Text(text, fontSize = 16.sp, fontWeight = FontWeight.Medium)
    }
}

// ---------- Pantalla ----------

@Composable
fun LoginScreen(viewModel: AuthViewModel, onLoggedIn: () -> Unit) {
    var loginUser by remember { mutableStateOf("") }
    var loginPass by remember { mutableStateOf("") }
    var regUser by remember { mutableStateOf("") }
    var regPass by remember { mutableStateOf("") }
    var regMail by remember { mutableStateOf("") }

    LaunchedEffect(Unit) { viewModel.clearMessages() }
    LaunchedEffect(viewModel.isLoggedIn) {
        if (viewModel.isLoggedIn) onLoggedIn()
    }

    AuthBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .imePadding()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            GlassCard(Modifier.fillMaxWidth()) {
                Text("Iniciar Sesion", color = Color.White, fontSize = 24.sp)
                Spacer(Modifier.height(16.dp))
                AuthField("Usuario", loginUser, { loginUser = it })
                Spacer(Modifier.height(12.dp))
                AuthField("Contraseña", loginPass, { loginPass = it }, isPassword = true)
                Spacer(Modifier.height(16.dp))
                PortalButton("Iniciar Sesion", enabled = !viewModel.loading) {
                    viewModel.login(loginUser, loginPass)
                }

                Spacer(Modifier.height(12.dp))
                Text("O", color = Color.White, fontSize = 16.sp)
                Text("Registrarse", color = Color.White, fontSize = 24.sp)
                Spacer(Modifier.height(12.dp))

                AuthField("Usuario", regUser, { regUser = it })
                Spacer(Modifier.height(12.dp))
                AuthField("Contraseña", regPass, { regPass = it }, isPassword = true)
                Spacer(Modifier.height(12.dp))
                AuthField("Correo Electronico", regMail, { regMail = it }, keyboardType = KeyboardType.Email)
                Spacer(Modifier.height(16.dp))
                PortalButton("Crear Cuenta", enabled = !viewModel.loading) {
                    viewModel.register(regUser, regPass, regMail)
                }

                if (viewModel.loading) {
                    Spacer(Modifier.height(12.dp))
                    CircularProgressIndicator(color = PortalGreen, modifier = Modifier.size(28.dp))
                }
                viewModel.error?.let {
                    Spacer(Modifier.height(12.dp))
                    Text(it, color = Color(0xFFFF6B6B), fontSize = 14.sp, textAlign = TextAlign.Center)
                }
            }
        }
    }
}

