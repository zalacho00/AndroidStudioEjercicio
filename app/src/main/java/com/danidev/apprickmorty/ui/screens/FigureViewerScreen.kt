package com.danidev.apprickmorty.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.SearchOff
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

// Backward compatibility constants
val CardBackground = DarkSurface
val CardBorderGreen = Color(0xFF4CAF50)

// ==========================================
// 1. PANTALLA DE CARGA (SPLASH)
// ==========================================
@Composable
fun SplashAppScreen(onStartClick: () -> Unit = {}) {
    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        // Fondo con arte oficial de Rick y Morty
        Image(
            painter = painterResource(id = R.drawable.splash_background),
            contentDescription = "Fondo Splash Rick y Morty",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Degradado oscuro para legibilidad Material 3
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.35f),
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.85f)
                        )
                    )
                )
        )

        // Contenido superior con insignia
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
                .padding(top = 36.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Surface(
                color = DarkSurface.copy(alpha = 0.85f),
                shape = RoundedCornerShape(24.dp),
                border = BorderStroke(1.dp, PortalGreen.copy(alpha = 0.6f)),
                shadowElevation = 8.dp
            ) {
                Row(
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .clip(CircleShape)
                            .background(PortalGreen)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "COLECCIÓN INTERDIMENSIONAL",
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }
        }

        // Sección inferior con llamada a la acción
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(24.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Rick & Morty Cards",
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Explora los personajes del multiverso y colecciona cartas",
                color = Color.White.copy(alpha = 0.8f),
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = onStartClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = PortalGreen,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
            ) {
                Text(
                    text = "Iniciar Exploración",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}

// ==========================================
// 2. PANTALLA HOME (GRILLA DE TARJETAS CON BÚSQUEDA)
// ==========================================
@Composable
fun HomeScreen(
    characters: List<RickCharacter> = emptyList(),
    onCharacterClick: (RickCharacter) -> Unit = {},
    onNavigateToCartas: () -> Unit = {},
    onNavigateToPerfil: () -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    // Filtrado interactivo en tiempo real
    val filteredCharacters = remember(characters, searchQuery) {
        if (searchQuery.isBlank()) {
            characters
        } else {
            characters.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                    it.species.contains(searchQuery, ignoreCase = true) ||
                    it.status.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavigationBar(
                currentRoute = "home",
                onHomeClick = {},
                onCartasClick = onNavigateToCartas,
                onPerfilClick = onNavigateToPerfil
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .statusBarsPadding()
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            // Cabecera superior con saludo y contador
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Tarjetas del Multiverso",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "${filteredCharacters.size} personajes disponibles",
                        color = DimensionCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Surface(
                    color = PortalGreenContainer,
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(CircleShape)
                                .background(PortalGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "C-137",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Barra de búsqueda con Material 3 Design
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = {
                    Text("Busca por nombre, especie o estado...", color = Color.Gray, fontSize = 14.sp)
                },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Buscar",
                        tint = PortalGreen
                    )
                },
                trailingIcon = {
                    if (searchQuery.isNotEmpty()) {
                        IconButton(onClick = { searchQuery = "" }) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "Limpiar",
                                tint = Color.LightGray
                            )
                        }
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    focusedContainerColor = DarkSurface,
                    unfocusedContainerColor = DarkSurface,
                    focusedBorderColor = PortalGreen,
                    unfocusedBorderColor = Color.White.copy(alpha = 0.15f),
                    cursorColor = PortalGreen
                ),
                shape = RoundedCornerShape(20.dp),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Grilla de tarjetas o estado vacío si no hay coincidencias
            if (filteredCharacters.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.SearchOff,
                            contentDescription = null,
                            tint = Color.Gray,
                            modifier = Modifier.size(56.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No se encontraron personajes",
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Intenta con otro nombre o especie",
                            color = Color.LightGray,
                            fontSize = 13.sp
                        )
                        if (searchQuery.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            FilledTonalButton(
                                onClick = { searchQuery = "" },
                                colors = ButtonDefaults.filledTonalButtonColors(
                                    containerColor = PortalGreenContainer,
                                    contentColor = Color.White
                                )
                            ) {
                                Text("Ver todos")
                            }
                        }
                    }
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(filteredCharacters, key = { it.id }) { character ->
                        CharacterCardItem(
                            character = character,
                            onClick = { onCharacterClick(character) }
                        )
                    }

                    item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                        // Banner informativo al final de la lista
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 12.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = DarkSurfaceVariant,
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.12f))
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(42.dp)
                                        .clip(CircleShape)
                                        .background(PortalGreenContainer),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.AutoAwesome,
                                        contentDescription = null,
                                        tint = PortalGreen,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(14.dp))
                                Column {
                                    Text(
                                        text = "Multiverso en Expansión",
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp
                                    )
                                    Text(
                                        text = "Abre sobres de cartas para desbloquear más dimensiones.",
                                        color = Color.LightGray,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

// Elemento de tarjeta de personaje optimizado con Material 3
@Composable
fun CharacterCardItem(
    character: RickCharacter,
    onClick: () -> Unit
) {
    val statusColor = when (character.status.lowercase()) {
        "alive", "vivo" -> StatusAlive
        "dead", "muerto" -> StatusDead
        else -> StatusUnknown
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column {
            // Imagen del personaje con proporción estándar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            ) {
                AsyncImage(
                    model = character.image,
                    contentDescription = character.name,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // Chip de estado sobre la imagen
                Surface(
                    color = Color.Black.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .padding(8.dp)
                        .align(Alignment.TopEnd)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 6.dp, vertical = 3.dp),
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
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Datos del personaje
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = character.name,
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = character.species,
                    color = DimensionCyan,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

// ==========================================
// 3. PANTALLA DETALLE DE TARJETA (MATERIAL 3)
// ==========================================
@Composable
fun CardDetailScreen(
    character: RickCharacter,
    onBack: () -> Unit = {}
) {
    val statusColor = when (character.status.lowercase()) {
        "alive", "vivo" -> StatusAlive
        "dead", "muerto" -> StatusDead
        else -> StatusUnknown
    }

    Scaffold(
        containerColor = DarkBackground,
        topBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
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
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Detalle del Personaje",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Marco de exhibición de carta coleccionable
            Card(
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                border = BorderStroke(2.dp, PortalGreen.copy(alpha = 0.8f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Retrato del personaje
                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(2.dp, DimensionCyan.copy(alpha = 0.7f)),
                        shadowElevation = 6.dp
                    ) {
                        AsyncImage(
                            model = character.image,
                            contentDescription = character.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(210.dp)
                                .clip(RoundedCornerShape(16.dp))
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = character.name,
                        color = Color.White,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.ExtraBold,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // Etiquetas / Chips de estado y especie
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
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(7.dp)
                                        .clip(CircleShape)
                                        .background(statusColor)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = character.status,
                                    color = Color.White,
                                    fontSize = 12.sp,
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
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(20.dp))

                    // Tarjeta de información adicional
                    Surface(
                        color = DarkSurfaceVariant,
                        shape = RoundedCornerShape(16.dp),
                        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp)
                        ) {
                            Text(
                                text = "DATOS DE LA DIMENSIÓN",
                                color = PortalGreen,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            DetailInfoRow(label = "Identificador", value = "#${character.id}")
                            Spacer(modifier = Modifier.height(8.dp))
                            DetailInfoRow(label = "Especie", value = character.species)
                            Spacer(modifier = Modifier.height(8.dp))
                            DetailInfoRow(label = "Estatus actual", value = character.status)
                            Spacer(modifier = Modifier.height(8.dp))
                            DetailInfoRow(
                                label = "Lugar de Origen",
                                value = character.origin?.name ?: "Desconocido"
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun DetailInfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = Color.Gray, fontSize = 13.sp)
        Text(
            text = value,
            color = Color.White,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

// ==========================================
// 4. BARRA DE NAVEGACIÓN INFERIOR (MATERIAL 3)
// ==========================================
@Composable
fun BottomNavigationBar(
    currentRoute: String = "home",
    onHomeClick: () -> Unit = {},
    onCartasClick: () -> Unit = {},
    onPerfilClick: () -> Unit = {}
) {
    NavigationBar(
        containerColor = DarkSurface,
        tonalElevation = 8.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = PortalGreen,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = PortalGreenContainer
            )
        )
        NavigationBarItem(
            selected = currentRoute == "cartas",
            onClick = onCartasClick,
            icon = { Icon(Icons.Default.Style, contentDescription = "Cartas") },
            label = { Text("Cartas") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = DimensionCyan,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = DimensionCyan.copy(alpha = 0.25f)
            )
        )
        NavigationBarItem(
            selected = currentRoute == "perfil",
            onClick = onPerfilClick,
            icon = { Icon(Icons.Default.Person, contentDescription = "Perfil") },
            label = { Text("Perfil") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = Color.White,
                selectedTextColor = PortalGreen,
                unselectedIconColor = Color.Gray,
                unselectedTextColor = Color.Gray,
                indicatorColor = PortalGreenContainer
            )
        )
    }
}
