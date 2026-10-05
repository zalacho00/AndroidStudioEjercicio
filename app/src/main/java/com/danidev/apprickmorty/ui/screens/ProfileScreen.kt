package com.danidev.apprickmorty.ui.screens

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.danidev.apprickmorty.ui.auth.AuthViewModel

@Composable
fun ProfileScreen(
    viewModel: AuthViewModel,
    onBack: () -> Unit,
    onLogout: () -> Unit
) {
    val context = LocalContext.current
    val picker = rememberLauncherForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) { uri ->
        if (uri != null) viewModel.onPhotoPicked(context.contentResolver, uri)
    }
    val shown = viewModel.pendingPhoto ?: viewModel.photo
    val shape = RoundedCornerShape(16.dp)

    AuthBackground {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .systemBarsPadding()
                .padding(20.dp)
        ) {
            // Barra superior
            Box(Modifier.fillMaxWidth()) {
                IconButton(onClick = onBack, modifier = Modifier.align(Alignment.CenterStart)) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver", tint = Color.White)
                }
                Text(
                    "Perfil", color = Color.White, fontSize = 22.sp,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Spacer(Modifier.height(24.dp))
            Text("Foto de Perfil", color = Color.White, fontSize = 22.sp)
            Spacer(Modifier.height(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth().weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                        .clip(shape)
                        .background(Color.White.copy(alpha = 0.08f))
                        .border(1.dp, Color.White.copy(alpha = 0.25f), shape),
                    contentAlignment = Alignment.Center
                ) {
                    if (shown != null) {
                        Image(
                            bitmap = shown.asImageBitmap(),
                            contentDescription = "Foto de perfil",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Icon(
                            Icons.Default.Person, null,
                            tint = Color.White.copy(alpha = 0.5f),
                            modifier = Modifier.size(96.dp)
                        )
                    }
                }

                Spacer(Modifier.height(20.dp))
                IconButton(
                    onClick = {
                        picker.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
                    },
                    modifier = Modifier.size(64.dp)
                ) {
                    Icon(
                        Icons.Default.AddPhotoAlternate, "Elegir foto",
                        tint = PortalGreen, modifier = Modifier.size(56.dp)
                    )
                }
                Text("Toca para elegir una foto", color = Color.White.copy(alpha = 0.7f), fontSize = 13.sp)

                Spacer(Modifier.height(24.dp))
                if (viewModel.username.isNotBlank()) {
                    Text(viewModel.username, color = Color.White, fontSize = 20.sp)
                }
                if (viewModel.email.isNotBlank()) {
                    Text(viewModel.email, color = Color.White.copy(alpha = 0.7f), fontSize = 14.sp)
                }
            }

            viewModel.error?.let {
                Text(it, color = Color(0xFFFF6B6B), fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
            }
            viewModel.info?.let {
                Text(it, color = PortalGreen, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
            }

            PortalButton(
                text = if (viewModel.loading) "Guardando..." else "Guarda Cambios",
                enabled = viewModel.pendingPhoto != null && !viewModel.loading
            ) { viewModel.savePhoto() }

            TextButton(onClick = onLogout, modifier = Modifier.align(Alignment.CenterHorizontally)) {
                Text("Cerrar sesión", color = Color.White.copy(alpha = 0.8f))
            }
        }
    }
}

