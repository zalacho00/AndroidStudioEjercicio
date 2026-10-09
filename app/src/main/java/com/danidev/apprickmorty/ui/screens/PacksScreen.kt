package com.danidev.apprickmorty.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.fadeIn
import androidx.compose.animation.scaleIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CardGiftcard
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.danidev.apprickmorty.R
import com.danidev.apprickmorty.data.model.RickCharacter
import com.danidev.apprickmorty.ui.theme.DarkBackground
import com.danidev.apprickmorty.ui.theme.DarkSurface
import com.danidev.apprickmorty.ui.theme.DarkSurfaceElevated
import com.danidev.apprickmorty.ui.theme.DarkSurfaceVariant
import com.danidev.apprickmorty.ui.theme.DimensionCyan
import com.danidev.apprickmorty.ui.theme.PortalGreen
import com.danidev.apprickmorty.ui.theme.PortalGreenContainer
import com.danidev.apprickmorty.ui.theme.StatusAlive
import com.danidev.apprickmorty.ui.theme.StatusDead
import com.danidev.apprickmorty.ui.theme.StatusUnknown

// Compatibilidad
val CyanButton = DimensionCyan

@Composable
fun PacksScreen(
    characters: List<RickCharacter> = emptyList(),
    onNavigateToHome: () -> Unit = {},
    onNavigateToCartas: () -> Unit = {},
    onNavigateToPerfil: () -> Unit = {}
) {
    var selectedCharacter by remember { mutableStateOf<RickCharacter?>(null) }
    var showRewardDialog by remember { mutableStateOf(false) }
    var packsAvailable by remember { mutableIntStateOf(3) }
    var packOpenedCount by remember { mutableIntStateOf(0) }

    // Animación de rebote al abrir sobre
    var isPackPressed by remember { mutableStateOf(false) }
    val packScale by animateFloatAsState(
        targetValue = if (isPackPressed) 0.92f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "packScale"
    )

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "cartas",
                onHomeClick = onNavigateToHome,
                onCartasClick = onNavigateToCartas,
                onPerfilClick = onNavigateToPerfil
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Fondo con arte oficial
            Image(
                painter = painterResource(id = R.drawable.splash_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Velo oscuro para contraste Material 3
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.65f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // SECCIÓN SUPERIOR: SOBRE INDIVIDUAL Y APERTURA
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1.2f)
                ) {
                    // Insignia Épica
                    Surface(
                        color = PortalGreenContainer,
                        shape = RoundedCornerShape(20.dp),
                        border = BorderStroke(1.dp, PortalGreen.copy(alpha = 0.7f))
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = PortalGreen,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "SOBRE DIMENSIONAL",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Text(
                        text = "¡ABRE Y DESCUBRE!",
                        color = Color.White,
                        fontSize = 30.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Imagen del sobre individual interactivo
                    Box(
                        modifier = Modifier
                            .scale(packScale)
                            .clickable {
                                if (characters.isNotEmpty()) {
                                    isPackPressed = true
                                    selectedCharacter = characters.random()
                                    showRewardDialog = true
                                    packOpenedCount += 1
                                    if (packsAvailable > 0) packsAvailable -= 1
                                    isPackPressed = false
                                }
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.sobre_individual),
                            contentDescription = "Sobre para abrir",
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .height(170.dp),
                            contentScale = ContentScale.Fit
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Botón para abrir sobre
                    Button(
                        onClick = {
                            if (characters.isNotEmpty()) {
                                selectedCharacter = characters.random()
                                showRewardDialog = true
                                packOpenedCount += 1
                                if (packsAvailable > 0) packsAvailable -= 1
                            }
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = DimensionCyan,
                            contentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(14.dp),
                        elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.75f)
                            .height(50.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.CardGiftcard,
                            contentDescription = null,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (packsAvailable > 0) "Abrir Sobre" else "Abrir de Nuevo",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // SECCIÓN INFERIOR: SOBRES RESTANTES Y RECARGA
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.85f)),
                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.15f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Sobres Disponibles",
                                color = Color.White,
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "x$packsAvailable",
                                    color = DimensionCyan,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "• Cartas abiertas: $packOpenedCount",
                                    color = Color.LightGray,
                                    fontSize = 12.sp
                                )
                            }

                            if (packsAvailable == 0) {
                                Spacer(modifier = Modifier.height(8.dp))
                                FilledTonalButton(
                                    onClick = { packsAvailable += 3 },
                                    colors = ButtonDefaults.filledTonalButtonColors(
                                        containerColor = PortalGreenContainer,
                                        contentColor = Color.White
                                    ),
                                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Replay,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text("Recargar Sobres (+3)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        Image(
                            painter = painterResource(id = R.drawable.sobres_apilados),
                            contentDescription = "Sobres disponibles",
                            modifier = Modifier
                                .size(90.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            // DIÁLOGO / MODAL DE CARTA REVELADA (MATERIAL 3)
            if (showRewardDialog && selectedCharacter != null) {
                val character = selectedCharacter!!
                val statusColor = when (character.status.lowercase()) {
                    "alive", "vivo" -> StatusAlive
                    "dead", "muerto" -> StatusDead
                    else -> StatusUnknown
                }

                Dialog(
                    onDismissRequest = { showRewardDialog = false },
                    properties = DialogProperties(usePlatformDefaultWidth = false)
                ) {
                    Card(
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkSurfaceElevated),
                        border = BorderStroke(2.dp, PortalGreen),
                        elevation = CardDefaults.cardElevation(defaultElevation = 12.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.9f)
                            .padding(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            // Cabecera del diálogo con título y botón cerrar
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = PortalGreen,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "¡NUEVA CARTA!",
                                        color = PortalGreen,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        letterSpacing = 1.sp
                                    )
                                }
                                IconButton(
                                    onClick = { showRewardDialog = false },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Cerrar",
                                        tint = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // Imagen de la carta obtenida
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                border = BorderStroke(2.dp, DimensionCyan.copy(alpha = 0.8f)),
                                shadowElevation = 8.dp
                            ) {
                                AsyncImage(
                                    model = character.image,
                                    contentDescription = character.name,
                                    modifier = Modifier
                                        .size(170.dp)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop
                                )
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = character.name,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Etiquetas de Estado y Especie
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    color = statusColor.copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.6f))
                                ) {
                                    Row(
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .size(6.dp)
                                                .clip(CircleShape)
                                                .background(statusColor)
                                        )
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text(
                                            text = character.status,
                                            color = Color.White,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }

                                Surface(
                                    color = DarkSurfaceVariant,
                                    shape = RoundedCornerShape(8.dp),
                                    border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
                                ) {
                                    Text(
                                        text = character.species,
                                        color = DimensionCyan,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            // Botones de acción
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        if (characters.isNotEmpty()) {
                                            selectedCharacter = characters.random()
                                            packOpenedCount += 1
                                            if (packsAvailable > 0) packsAvailable -= 1
                                        }
                                    },
                                    modifier = Modifier.weight(1f),
                                    shape = RoundedCornerShape(12.dp),
                                    border = BorderStroke(1.dp, DimensionCyan.copy(alpha = 0.7f))
                                ) {
                                    Text("Abrir Otro", color = DimensionCyan, fontSize = 13.sp)
                                }

                                Button(
                                    onClick = { showRewardDialog = false },
                                    colors = ButtonDefaults.buttonColors(containerColor = PortalGreen),
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text("Guardar", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}