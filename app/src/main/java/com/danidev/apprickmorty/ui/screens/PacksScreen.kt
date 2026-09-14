package com.danidev.apprickmorty.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.danidev.apprickmorty.R
import com.danidev.apprickmorty.data.model.RickCharacter

val CyanButton = Color(0xFF53C2EC)

@Composable
fun PacksScreen(
    characters: List<RickCharacter> = emptyList(),
    onNavigateToHome: () -> Unit = {},
    onNavigateToCartas: () -> Unit = {}
) {
    // Estado para guardar el personaje obtenido al azar
    var selectedCharacter by remember { mutableStateOf<RickCharacter?>(null) }
    var showRewardDialog by remember { mutableStateOf(false) }

    Scaffold(
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                NavigationBarItem(
                    selected = false,
                    onClick = onNavigateToHome,
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = true,
                    onClick = onNavigateToCartas,
                    icon = { Icon(Icons.Default.Style, contentDescription = "Cartas") },
                    label = { Text("Cartas") }
                )
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Fondo
            Image(
                painter = painterResource(id = R.drawable.splash_background),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.45f))
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // SECCIÓN SUPERIOR
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "EPIC!",
                        color = Color.White,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Image(
                        painter = painterResource(id = R.drawable.sobre_individual),
                        contentDescription = "Sobre para abrir",
                        modifier = Modifier
                            .fillMaxWidth(0.85f)
                            .height(160.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    // Evento al presionar "Abrir Sobre"
                    Button(
                        onClick = {
                            if (characters.isNotEmpty()) {
                                selectedCharacter = characters.random() // Selecciona personaje al azar
                                showRewardDialog = true
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = CyanButton),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier
                            .fillMaxWidth(0.65f)
                            .height(48.dp)
                    ) {
                        Text(
                            text = "Abrir Sobre",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                // SECCIÓN INFERIOR
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(Color.White.copy(alpha = 0.15f))
                        .border(1.dp, Color.White.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
                        .padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "Sobres disponibles\nX3",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Image(
                            painter = painterResource(id = R.drawable.sobres_apilados),
                            contentDescription = "Sobres disponibles",
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .height(140.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            // MODAL / DIÁLOGO DE CARTA OBTENIDA
            if (showRewardDialog && selectedCharacter != null) {
                Dialog(onDismissRequest = { showRewardDialog = false }) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = DarkBackground),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(3.dp, CardBorderGreen, RoundedCornerShape(16.dp))
                            .padding(4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "¡NUEVA CARTA!",
                                    color = PortalGreen,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                IconButton(onClick = { showRewardDialog = false }) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Cerrar",
                                        tint = Color.White
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            AsyncImage(
                                model = selectedCharacter!!.image,
                                contentDescription = selectedCharacter!!.name,
                                modifier = Modifier
                                    .size(160.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .border(2.dp, CardBorderGreen, RoundedCornerShape(8.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Text(
                                text = selectedCharacter!!.name,
                                color = Color.White,
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Especie: ${selectedCharacter!!.species}",
                                color = Color.LightGray,
                                fontSize = 14.sp
                            )

                            Text(
                                text = "Estatus: ${selectedCharacter!!.status}",
                                color = Color.LightGray,
                                fontSize = 14.sp
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { showRewardDialog = false },
                                colors = ButtonDefaults.buttonColors(containerColor = PortalGreen),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "Guardar en Álbum", color = Color.White)
                            }
                        }
                    }
                }
            }
        }
    }
}