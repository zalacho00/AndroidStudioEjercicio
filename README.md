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



