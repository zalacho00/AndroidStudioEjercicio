package com.danidev.apprickmorty.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.AlternateEmail
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danidev.apprickmorty.ui.auth.AuthViewModel
import com.danidev.apprickmorty.ui.theme.DarkBackground
import com.danidev.apprickmorty.ui.theme.DarkSurface
import com.danidev.apprickmorty.ui.theme.DarkSurfaceElevated
import com.danidev.apprickmorty.ui.theme.DarkSurfaceVariant
import com.danidev.apprickmorty.ui.theme.DimensionCyan
import com.danidev.apprickmorty.ui.theme.PortalGreen
import com.danidev.apprickmorty.ui.theme.PortalGreenContainer
import com.danidev.apprickmorty.ui.theme.StatusDead

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    var showLogoutDialog by remember { mutableStateOf(false) }

    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) viewModel.onPhotoPicked(context.contentResolver, uri)
    }

    val shown = viewModel.pendingPhoto ?: viewModel.photo

    AuthBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .verticalScroll(rememberScrollState())
                .padding(20.dp)
        ) {
            // Barra superior
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onBack,
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = DarkSurfaceElevated,
                        contentColor = Color.White
                    )
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Volver"
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Perfil del Explorador",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(Modifier.height(28.dp))

            // Sección de Avatar (sin superposición defectuosa)
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier.size(150.dp),
                    contentAlignment = Alignment.BottomEnd
                ) {
                    // Contenedor principal de la foto de perfil
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(CircleShape)
                            .clickable {
                                picker.launch(
                                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                                )
                            },
                        shape = CircleShape,
                        color = DarkSurfaceVariant,
                        border = BorderStroke(3.dp, PortalGreen)
                    ) {
                        if (shown != null) {
                            Image(
                                bitmap = shown.asImageBitmap(),
                                contentDescription = "Foto de perfil",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = Color.White.copy(alpha = 0.6f),
                                    modifier = Modifier.size(80.dp)
                                )
                            }
                        }
                    }

                    // Botón flotante para seleccionar foto
                    SmallFloatingActionButton(
                        onClick = {
                            picker.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        },
                        containerColor = DimensionCyan,
                        contentColor = Color.Black,
                        shape = CircleShape,
                        modifier = Modifier
                            .size(42.dp)
                            .offset(x = 4.dp, y = 4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "Elegir foto",
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(Modifier.height(12.dp))
                Text(
                    text = if (viewModel.pendingPhoto != null) "Foto seleccionada (sin guardar)" else "Toca para cambiar foto",
                    color = if (viewModel.pendingPhoto != null) DimensionCyan else Color.White.copy(alpha = 0.7f),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(Modifier.height(24.dp))

            // Tarjeta de datos de usuario (Material 3)
            Card(
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.9f)),
                border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp)
                ) {
                    Text(
                        text = "INFORMACIÓN DE LA CUENTA",
                        color = PortalGreen,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )

                    Spacer(Modifier.height(16.dp))

                    // Fila de Usuario
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = PortalGreenContainer,
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = PortalGreen,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Usuario",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                            Text(
                                text = if (viewModel.username.isNotBlank()) viewModel.username else "Viajero Anónimo",
                                color = Color.White,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                    Spacer(Modifier.height(14.dp))

                    // Fila de Correo
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = DimensionCyan.copy(alpha = 0.2f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.AlternateEmail,
                                    contentDescription = null,
                                    tint = DimensionCyan,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Correo Electrónico",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                            Text(
                                text = if (viewModel.email.isNotBlank()) viewModel.email else "No registrado",
                                color = Color.White,
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    Spacer(Modifier.height(14.dp))
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                    Spacer(Modifier.height(14.dp))

                    // Fila de Credencial Interdimensional
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            shape = CircleShape,
                            color = Color(0xFFD4E157).copy(alpha = 0.2f),
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = Color(0xFFD4E157),
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(Modifier.width(14.dp))
                        Column {
                            Text(
                                text = "Licencia Multiversal",
                                color = Color.Gray,
                                fontSize = 12.sp
                            )
                            Text(
                                text = "Dimensión C-137 (Autorizado)",
                                color = Color.White,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            // Notificación de Éxito
            AnimatedVisibility(visible = viewModel.info != null) {
                viewModel.info?.let { msg ->
                    Surface(
                        color = PortalGreenContainer,
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, PortalGreen.copy(alpha = 0.6f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = PortalGreen,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Text(
                                text = msg,
                                color = Color.White,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // Notificación de Error
            AnimatedVisibility(visible = viewModel.error != null) {
                viewModel.error?.let { err ->
                    Surface(
                        color = StatusDead.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, StatusDead.copy(alpha = 0.5f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 12.dp)
                    ) {
                        Text(
                            text = err,
                            color = Color(0xFFFF6B6B),
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.padding(14.dp)
                        )
                    }
                }
            }

            // Botón Guardar Cambios
            PortalButton(
                text = if (viewModel.loading) "Guardando..." else "Guardar Cambios de Foto",
                enabled = viewModel.pendingPhoto != null && !viewModel.loading,
                isLoading = viewModel.loading
            ) {
                viewModel.savePhoto()
            }

            Spacer(Modifier.height(16.dp))

            // Botón Cerrar Sesión con estilo Material 3
            OutlinedButton(
                onClick = { showLogoutDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, StatusDead.copy(alpha = 0.6f)),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = StatusDead
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    text = "Cerrar Sesión",
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
        }

        // Diálogo de confirmación para cerrar sesión
        if (showLogoutDialog) {
            AlertDialog(
                onDismissRequest = { showLogoutDialog = false },
                containerColor = DarkSurface,
                title = {
                    Text(
                        text = "¿Cerrar Sesión?",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                },
                text = {
                    Text(
                        text = "¿Estás seguro de que deseas salir del portal interdimensional?",
                        color = Color.LightGray
                    )
                },
                confirmButton = {
                    Button(
                        onClick = {
                            showLogoutDialog = false
                            onLogout()
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = StatusDead)
                    ) {
                        Text("Sí, Cerrar", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showLogoutDialog = false }) {
                        Text("Cancelar", color = Color.White)
                    }
                }
            )
        }
    }
}
