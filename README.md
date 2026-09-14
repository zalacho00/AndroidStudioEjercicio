# App rick y morty

## carpetas usadas

<img width="437" height="645" alt="image" src="https://github.com/user-attachments/assets/66aff49b-ca6b-4819-a1b1-e9de4fbc4b60" />
Esta es la estructura limpia para una aplicacion movil
<br><br>

### 1. Capa de Datos (`data`)

Maneja la lógica de negocio, la obtención de datos y su transformación.

<br>

1.1: `model` (`Character.kt`): Define las entidades o estructuras de datos que maneja la app.
Es una clase Kotlin (generalmente un `data class`) que representa a un personaje con sus propiedades 
(por ejemplo: id, name, status, image).
<img width="366" height="469" alt="image" src="https://github.com/user-attachments/assets/777df7c4-58ce-49f2-a0e2-23370f4509df" />

<br>

1.2: `remote` (`RickAndMortyApi.kt`): Se encarga de la comunicación directa con el servidor/API externo.
Aquí se definen las peticiones HTTP (normalmente usando librerías como `Retrofit`)
para consumir los datos de la API de Rick and Morty.
<img width="556" height="667" alt="image" src="https://github.com/user-attachments/assets/2e9d158e-2dae-4c06-b802-4e5be99eced7" />

<br>

1.3: `repository` (`CharacterRepository.kt`): Actúa como intermediario y única fuente de verdad para el resto de la app.
Pide los datos a `remote` (o a una base de datos local si la hubiera), procesa la respuesta y se la entrega a la interfaz.
Aisla a la `UI` de los detalles de implementación de la red.
<img width="649" height="434" alt="image" src="https://github.com/user-attachments/assets/c323953a-1ac1-4e93-bfdc-ec0103bceba7" />


<br><br>
### 2. Capa de Interfaz (`ui`)
Maneja todo lo que el usuario ve y escucha en la pantalla.
<br>

`ui`: Paquete raíz de la interfaz visual.
<br>

2.1: `screens` (`CharacterScreen.kt`): Contiene los componentes de vista (usualmente composables de Jetpack Compose)
que arman la pantalla completa donde se muestran los personajes y se gestiona la interacción del usuario.
<br>

2.2: `theme` (`Color.kt`, `Theme.kt`,` Type.kt`): Configura la identidad visual global de la app.
<br>

- `Color.kt`: Define la paleta de colores.

- `Type.kt`: Define los estilos tipográficos (fuentes, tamaños, pesos).

- `Theme.kt`: Une los colores, tipografías y formas para aplicar un tema unificado (modo claro/oscuro) a todos los componentes visuales.



## Aplicacion Rick Morty

<img width="572" height="1280" alt="fondeSplash" src="https://github.com/user-attachments/assets/ab439d0d-38b4-4183-bdb0-ed024c2ee2b2" />
<br>
<img width="310" height="697" alt="Captura de pantalla 2026-09-11 201933" src="https://github.com/user-attachments/assets/fd5bd273-023c-4dbf-882f-2a150f7297a5" />
<br>
<img width="720" height="1610" alt="WhatsApp Image 2026-09-14 at 6 33 56 PM" src="https://github.com/user-attachments/assets/5ef0265c-27c8-4c36-a19a-e58eb4393c7d" />
<br>
<img width="572" height="1280" alt="sobres" src="https://github.com/user-attachments/assets/13791a53-7dba-4a94-b003-c225de40b634" />
<br>
<img width="572" height="1280" alt="cartaSobre" src="https://github.com/user-attachments/assets/c32304be-afd7-4ea4-b034-631118dab14a" />


CODIGO EN JETPACK COMPOSE:
````
package com.danidev.apprickmorty.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.danidev.apprickmorty.data.model.RickCharacter

// Paleta de colores exacta de Figma
val PortalGreen = Color(0xFF55B354)
val DarkBackground = Color(0xFF1B1B1B)
val CardBackground = Color(0xFF2B2B2B)
val CardBorderGreen = Color(0xFF4CAF50)

// 1. PANTALLA DE CARGA (SPLASH)
@Composable
fun SplashAppScreen(onStartClick: () -> Unit = {}) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Rick and Morty",
                    color = Color.Cyan,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Las Tarjetas Perdidas",
                    color = Color.White,
                    fontSize = 18.sp
                )
            }

            Button(
                onClick = onStartClick,
                colors = ButtonDefaults.buttonColors(containerColor = PortalGreen),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Iniciar Aplicación", color = Color.White, fontSize = 16.sp)
            }
        }
    }
}

// 2. PANTALLA HOME (GRILLA DE TARJETAS)
@Composable
fun HomeScreen(
    characters: List<RickCharacter> = emptyList(),
    onCharacterClick: (RickCharacter) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        bottomBar = { BottomNavigationBar() },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Busca tu Personaje", color = Color.Gray) },
                trailingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.Gray) },
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = CardBackground,
                    focusedContainerColor = CardBackground,
                    focusedBorderColor = PortalGreen,
                    unfocusedBorderColor = Color.Transparent
                ),
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Tarjetas",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(characters) { character ->
                    Card(
                        modifier = Modifier
                            .aspectRatio(1f)
                            .clickable { onCharacterClick(character) },
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        AsyncImage(
                            model = character.image,
                            contentDescription = character.name,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Más personajes\nPróximamente",
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// 3. PANTALLA DETALLE DE TARJETA
@Composable
fun CardDetailScreen(character: RickCharacter) {
    Scaffold(
        bottomBar = { BottomNavigationBar() },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .border(4.dp, CardBorderGreen, RoundedCornerShape(8.dp))
                    .background(CardBorderGreen)
                    .padding(16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.White)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = character.image,
                        contentDescription = character.name,
                        modifier = Modifier
                            .size(180.dp)
                            .border(2.dp, CardBorderGreen)
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = character.name,
                        color = Color.Black,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Especie: ${character.species}",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(CardBorderGreen)
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "INFORMACIÓN DEL PERSONAJE:\nEstatus: ${character.status}\nOrigen: ${character.origin?.name ?: "Desconocido"}",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}

// BARRA DE NAVEGACIÓN INFERIOR
@Composable
fun BottomNavigationBar() {
    NavigationBar(containerColor = Color.White) {
        NavigationBarItem(
            selected = true,
            onClick = {},
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") }
        )
        NavigationBarItem(
            selected = false,
            onClick = {},
            icon = { Icon(Icons.Default.Style, contentDescription = "Cartas") },
            label = { Text("Cartas") }
        )
    }
}
````

### codigo de pantalla de sobres

````
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

````
