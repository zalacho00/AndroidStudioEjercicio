package com.danidev.apprickmorty.ui.auth

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Base64
import android.util.Patterns
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.FirebaseNetworkException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream

class AuthViewModel : ViewModel() {

    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()

    var isLoggedIn by mutableStateOf(auth.currentUser != null); private set
    var loading by mutableStateOf(false); private set
    var error by mutableStateOf<String?>(null); private set
    var info by mutableStateOf<String?>(null); private set

    // Datos del perfil
    var username by mutableStateOf(""); private set
    var email by mutableStateOf(""); private set
    var photo by mutableStateOf<Bitmap?>(null); private set          // foto guardada
    var pendingPhoto by mutableStateOf<Bitmap?>(null); private set   // foto elegida sin guardar

    init {
        if (auth.currentUser != null) loadProfile()
    }

    fun clearMessages() {
        error = null
        info = null
    }

    // ---------- LOGIN ----------
    fun login(userOrEmail: String, password: String) {
        clearMessages()
        val id = userOrEmail.trim()
        if (id.isBlank() || password.isBlank()) {
            error = "Completa usuario y contraseña"; return
        }
        loading = true
        if (Patterns.EMAIL_ADDRESS.matcher(id).matches()) {
            signIn(id, password)
        } else {
            db.collection("usuarios")
                .whereEqualTo("usernameLower", id.lowercase())
                .limit(1)
                .get()
                .addOnSuccessListener { snap ->
                    val mail = snap.documents.firstOrNull()?.getString("email")
                    if (mail == null) fail("Usuario no encontrado") else signIn(mail, password)
                }
                .addOnFailureListener { fail(mapError(it)) }
        }
    }

    private fun signIn(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                loadProfile {
                    loading = false
                    isLoggedIn = true
                }
            }
            .addOnFailureListener { fail(mapError(it)) }
    }

    // ---------- REGISTRO ----------
    fun register(user: String, password: String, mail: String) {
        clearMessages()
        val u = user.trim()
        val e = mail.trim()
        when {
            u.isBlank() || password.isBlank() || e.isBlank() -> {
                error = "Completa todos los campos"; return
            }
            u.length < 3 -> { error = "El usuario debe tener al menos 3 caracteres"; return }
            u.contains("@") || u.contains(" ") -> {
                error = "El usuario no puede tener espacios ni @"; return
            }
            !Patterns.EMAIL_ADDRESS.matcher(e).matches() -> {
                error = "Correo electrónico no válido"; return
            }
            password.length < 6 -> {
                error = "La contraseña debe tener al menos 6 caracteres"; return
            }
        }
        loading = true
        // 1) Verificar que el usuario no exista
        db.collection("usuarios")
            .whereEqualTo("usernameLower", u.lowercase())
            .limit(1)
            .get()
            .addOnSuccessListener { snap ->
                if (!snap.isEmpty) {
                    fail("Ese usuario ya existe"); return@addOnSuccessListener
                }
                // 2) Crear la cuenta
                auth.createUserWithEmailAndPassword(e, password)
                    .addOnSuccessListener { result ->
                        val uid = result.user?.uid ?: return@addOnSuccessListener fail("Error al crear la cuenta")
                        val data = mapOf(
                            "username" to u,
                            "usernameLower" to u.lowercase(),
                            "email" to e
                        )
                        // 3) Guardar el perfil
                        db.collection("usuarios").document(uid).set(data)
                            .addOnSuccessListener {
                                username = u
                                email = e
                                photo = null
                                loading = false
                                isLoggedIn = true
                            }
                            .addOnFailureListener { fail(mapError(it)) }
                    }
                    .addOnFailureListener { fail(mapError(it)) }
            }
            .addOnFailureListener { fail(mapError(it)) }
    }

    // ---------- PERFIL ----------
    private fun loadProfile(onDone: () -> Unit = {}) {
        val uid = auth.currentUser?.uid ?: return onDone()
        db.collection("usuarios").document(uid).get()
            .addOnSuccessListener { doc ->
                username = doc.getString("username") ?: ""
                email = doc.getString("email") ?: auth.currentUser?.email.orEmpty()
                photo = doc.getString("photoBase64")?.let { decodeBase64(it) }
                onDone()
            }
            .addOnFailureListener { onDone() }
    }

    /** Se llama cuando el usuario elige una imagen en la galería. */
    fun onPhotoPicked(resolver: ContentResolver, uri: Uri) {
        clearMessages()
        viewModelScope.launch {
            val bmp = withContext(Dispatchers.IO) { decodeScaled(resolver, uri, 256) }
            if (bmp == null) error = "No se pudo leer la imagen" else pendingPhoto = bmp
        }
    }

    fun savePhoto() {
        clearMessages()
        val bmp = pendingPhoto
        val uid = auth.currentUser?.uid
        if (bmp == null || uid == null) {
            error = "Elige una foto primero"; return
        }
        loading = true
        val out = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.JPEG, 80, out)
        val b64 = Base64.encodeToString(out.toByteArray(), Base64.NO_WRAP)

        db.collection("usuarios").document(uid)
            .set(mapOf("photoBase64" to b64), SetOptions.merge())
            .addOnSuccessListener {
                photo = bmp
                pendingPhoto = null
                loading = false
                info = "Foto actualizada"
            }
            .addOnFailureListener { fail(mapError(it)) }
    }

    fun logout() {
        auth.signOut()
        username = ""
        email = ""
        photo = null
        pendingPhoto = null
        clearMessages()
        isLoggedIn = false
    }

    // ---------- Utilidades ----------
    private fun fail(msg: String) {
        loading = false
        error = msg
    }

    private fun mapError(e: Exception): String = when (e) {
        is FirebaseAuthUserCollisionException -> "Ese correo ya está registrado"
        is FirebaseAuthWeakPasswordException -> "La contraseña es muy débil"
        is FirebaseAuthInvalidUserException,
        is FirebaseAuthInvalidCredentialsException -> "Usuario o contraseña incorrectos"
        is FirebaseNetworkException -> "Sin conexión a internet"
        is FirebaseFirestoreException ->
            if (e.code == FirebaseFirestoreException.Code.PERMISSION_DENIED)
                "Sin permiso en Firestore: revisa las reglas"
            else "Error de base de datos: ${e.message}"
        else -> e.message ?: "Error desconocido"
    }

    private fun decodeBase64(b64: String): Bitmap? = try {
        val bytes = Base64.decode(b64, Base64.NO_WRAP)
        BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    } catch (e: Exception) {
        null
    }

    private fun decodeScaled(resolver: ContentResolver, uri: Uri, maxSide: Int): Bitmap? {
        return try {
            val bounds = BitmapFactory.Options().apply { inJustDecodeBounds = true }
            resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, bounds) }
            var sample = 1
            while (bounds.outWidth / sample > maxSide * 2 || bounds.outHeight / sample > maxSide * 2) {
                sample *= 2
            }
            val opts = BitmapFactory.Options().apply { inSampleSize = sample }
            val raw = resolver.openInputStream(uri)?.use { BitmapFactory.decodeStream(it, null, opts) }
                ?: return null
            val scale = maxSide.toFloat() / maxOf(raw.width, raw.height)
            if (scale >= 1f) raw
            else Bitmap.createScaledBitmap(
                raw, (raw.width * scale).toInt(), (raw.height * scale).toInt(), true
            )
        } catch (e: Exception) {
            null
        }
    }
}