package com.danidev.apprickmorty.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.danidev.apprickmorty.ui.theme.DarkBackground
import com.danidev.apprickmorty.ui.theme.DarkSurface
import com.danidev.apprickmorty.ui.theme.DimensionCyan
import com.danidev.apprickmorty.ui.theme.PortalGreen
import com.danidev.apprickmorty.ui.theme.PortalGreenContainer

// ---------- Componentes compartidos (también usados por ProfileScreen) ----------

@Composable
fun AuthBackground(content: @Composable BoxScope.() -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Image(
            painter = painterResource(R.drawable.splash_background),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(20.dp),
            contentScale = ContentScale.Crop
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.75f))
        )
        content()
    }
}

@Composable
fun GlassCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    val shape = RoundedCornerShape(24.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(DarkSurface.copy(alpha = 0.85f))
            .border(1.dp, Color.White.copy(alpha = 0.15f), shape)
            .padding(24.dp),
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
    keyboardType: KeyboardType = KeyboardType.Text,
    leadingIcon: ImageVector? = null
) {
    var visible by remember { mutableStateOf(false) }

    Column(Modifier.fillMaxWidth()) {
        Text(
            text = label,
            color = Color.White.copy(alpha = 0.9f),
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        )
        Spacer(Modifier.height(6.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(12.dp),
            keyboardOptions = KeyboardOptions(
                keyboardType = if (isPassword) KeyboardType.Password else keyboardType
            ),
            visualTransformation = if (isPassword && !visible) PasswordVisualTransformation() else VisualTransformation.None,
            leadingIcon = leadingIcon?.let { icon ->
                {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = PortalGreen
                    )
                }
            },
            trailingIcon = if (isPassword) {
                {
                    IconButton(onClick = { visible = !visible }) {
                        Icon(
                            imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
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
                unfocusedBorderColor = Color.White.copy(alpha = 0.25f),
                focusedContainerColor = Color.White.copy(alpha = 0.05f),
                unfocusedContainerColor = Color.White.copy(alpha = 0.05f)
            )
        )
    }
}

@Composable
fun PortalButton(
    text: String,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        enabled = enabled && !isLoading,
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp),
        shape = RoundedCornerShape(14.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = PortalGreen,
            contentColor = Color.White,
            disabledContainerColor = PortalGreen.copy(alpha = 0.35f),
            disabledContentColor = Color.White.copy(alpha = 0.6f)
        ),
        elevation = ButtonDefaults.buttonElevation(defaultElevation = 4.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(24.dp),
                strokeWidth = 2.5.dp
            )
        } else {
            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

// ---------- Pantalla Principal de Login / Registro ----------

@Composable
fun LoginScreen(
    viewModel: AuthViewModel,
    onLoggedIn: () -> Unit
) {
    // 0 = Iniciar Sesión, 1 = Registrarse
    var selectedTab by remember { mutableIntStateOf(0) }

    var loginUser by remember { mutableStateOf("") }
    var loginPass by remember { mutableStateOf("") }
    var regUser by remember { mutableStateOf("") }
    var regPass by remember { mutableStateOf("") }
    var regMail by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.clearMessages()
    }

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
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Logo / Título superior
            Text(
                text = "RICK & MORTY",
                color = PortalGreen,
                fontSize = 28.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = 2.sp
            )
            Text(
                text = "Portal de Acceso Interdimensional",
                color = DimensionCyan,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(Modifier.height(20.dp))

            GlassCard(Modifier.fillMaxWidth()) {
                PrimaryTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = Color.Transparent,
                    contentColor = PortalGreen,
                    divider = {}
                ) {
                    Tab(
                        selected = selectedTab == 0,
                        onClick = {
                            selectedTab = 0
                            viewModel.clearMessages()
                        },
                        text = {
                            Text(
                                text = "Iniciar Sesión",
                                fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 0) Color.White else Color.Gray,
                                fontSize = 15.sp
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Login,
                                contentDescription = null,
                                tint = if (selectedTab == 0) PortalGreen else Color.Gray
                            )
                        }
                    )
                    Tab(
                        selected = selectedTab == 1,
                        onClick = {
                            selectedTab = 1
                            viewModel.clearMessages()
                        },
                        text = {
                            Text(
                                text = "Registrarse",
                                fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Normal,
                                color = if (selectedTab == 1) Color.White else Color.Gray,
                                fontSize = 15.sp
                            )
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = null,
                                tint = if (selectedTab == 1) DimensionCyan else Color.Gray
                            )
                        }
                    )
                }

                Spacer(Modifier.height(24.dp))

                // Contenido de la pestaña activa
                if (selectedTab == 0) {
                    // Pestaña INICIAR SESIÓN
                    AuthField(
                        label = "Usuario o Correo",
                        value = loginUser,
                        onValueChange = { loginUser = it },
                        leadingIcon = Icons.Default.Person
                    )
                    Spacer(Modifier.height(14.dp))
                    AuthField(
                        label = "Contraseña",
                        value = loginPass,
                        onValueChange = { loginPass = it },
                        isPassword = true,
                        leadingIcon = Icons.Default.Lock
                    )
                    Spacer(Modifier.height(24.dp))

                    PortalButton(
                        text = "Iniciar Sesión",
                        enabled = !viewModel.loading,
                        isLoading = viewModel.loading
                    ) {
                        viewModel.login(loginUser, loginPass)
                    }

                    Spacer(Modifier.height(12.dp))
                    TextButton(onClick = {
                        selectedTab = 1
                        viewModel.clearMessages()
                    }) {
                        Text(
                            text = "¿No tienes cuenta? Regístrate aquí",
                            color = DimensionCyan,
                            fontSize = 13.sp
                        )
                    }
                } else {
                    // Pestaña REGISTRARSE
                    AuthField(
                        label = "Nombre de Usuario",
                        value = regUser,
                        onValueChange = { regUser = it },
                        leadingIcon = Icons.Default.Person
                    )
                    Spacer(Modifier.height(12.dp))
                    AuthField(
                        label = "Correo Electrónico",
                        value = regMail,
                        onValueChange = { regMail = it },
                        keyboardType = KeyboardType.Email,
                        leadingIcon = Icons.Default.Email
                    )
                    Spacer(Modifier.height(12.dp))
                    AuthField(
                        label = "Contraseña (mínimo 6 caracteres)",
                        value = regPass,
                        onValueChange = { regPass = it },
                        isPassword = true,
                        leadingIcon = Icons.Default.Lock
                    )
                    Spacer(Modifier.height(24.dp))

                    PortalButton(
                        text = "Crear Cuenta",
                        enabled = !viewModel.loading,
                        isLoading = viewModel.loading
                    ) {
                        viewModel.register(regUser, regPass, regMail)
                    }

                    Spacer(Modifier.height(12.dp))
                    TextButton(onClick = {
                        selectedTab = 0
                        viewModel.clearMessages()
                    }) {
                        Text(
                            text = "¿Ya tienes cuenta? Inicia sesión",
                            color = PortalGreen,
                            fontSize = 13.sp
                        )
                    }
                }

                // Banner de error Material 3
                AnimatedVisibility(
                    visible = viewModel.error != null,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    viewModel.error?.let { err ->
                        Spacer(Modifier.height(16.dp))
                        Surface(
                            color = MaterialTheme.colorScheme.error.copy(alpha = 0.15f),
                            shape = RoundedCornerShape(12.dp),
                            border = androidx.compose.foundation.BorderStroke(
                                1.dp,
                                MaterialTheme.colorScheme.error.copy(alpha = 0.5f)
                            ),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = err,
                                color = Color(0xFFFF6B6B),
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.padding(12.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}
