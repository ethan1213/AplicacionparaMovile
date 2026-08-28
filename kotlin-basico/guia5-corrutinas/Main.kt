/**
 * Guía 5: Aplicando corrutinas y Sintaxis avanzada
 *
 * Simula un login asíncrono contra un servidor, modelando los posibles
 * resultados (Autenticando, Éxito, Error) con una sealed class.
 *
 * NOTA para correr esto en IntelliJ: a diferencia de las guías 2-4, este
 * archivo SÍ necesita una librería externa (kotlinx-coroutines-core), porque
 * las corrutinas no vienen en el Kotlin estándar. Si creas un proyecto nuevo
 * en IntelliJ con Gradle, agrega en build.gradle.kts:
 *   implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
 */

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

// ------------------------------------------------------------------
// data class: clase pensada SOLO para guardar datos. Kotlin le genera gratis
// equals(), hashCode(), toString() y copy() -- en Java tendrías que escribir
// todo eso a mano (o usar un generador de IDE / librería como Lombok).
// ------------------------------------------------------------------
data class PerfilUsuario(val nombreUsuario: String, val email: String)

// ------------------------------------------------------------------
// sealed class: define un conjunto CERRADO y conocido de posibles subtipos.
// A diferencia de una clase `open` normal (donde CUALQUIERA puede crear más
// subclases en cualquier archivo), una sealed class solo permite subclases
// declaradas en el mismo archivo. La ventaja: cuando uses `when` sobre una
// sealed class, el compilador SABE cuáles son todos los casos posibles y te
// obliga (o te avisa) si te olvidaste de cubrir alguno -- ideal para modelar
// "estados" de una operación (cargando/éxito/error) de forma 100% segura.
// ------------------------------------------------------------------
sealed class ResultadoLogin {
    data class Exito(val perfil: PerfilUsuario) : ResultadoLogin()
    data class Error(val mensaje: String) : ResultadoLogin()
    object Autenticando : ResultadoLogin()
}

/**
 * `suspend` marca esta función como "pausable": puede detenerse (por ejemplo
 * en delay()) y devolver el control sin bloquear el hilo (thread) donde corre,
 * y luego retomar donde quedó. Es la base de las corrutinas: hacer trabajo que
 * demora (red, disco) SIN congelar la app mientras se espera la respuesta.
 * Solo se puede llamar una función `suspend` desde otra función `suspend` o
 * desde dentro de una corrutina (por eso más abajo usamos runBlocking).
 */
suspend fun autenticarUsuario(usuario: String, contrasena: String): ResultadoLogin {
    delay(2000L) // Simula 2 segundos de espera de red, sin bloquear el hilo.

    return if (usuario == "admin" && contrasena == "1234") {
        ResultadoLogin.Exito(PerfilUsuario(nombreUsuario = "admin", email = "admin@duoc.cl"))
    } else {
        ResultadoLogin.Error("Credenciales incorrectas")
    }
}

// `runBlocking` crea un CoroutineScope y BLOQUEA el hilo actual hasta que todo
// lo de adentro termine. Se usa para "puentear" el mundo normal (main, que NO
// es suspend) con el mundo de corrutinas. En una app Android real casi nunca
// se usa runBlocking (bloquearía la UI); ahí se usan viewModelScope o
// lifecycleScope, que NO bloquean. runBlocking es sobre todo para pruebas,
// scripts o (como acá) programas de consola simples.
fun main() = runBlocking {
    println("Estado: Autenticando...")

    // --- Intento 1: credenciales correctas ---
    val resultado1 = autenticarUsuario("admin", "1234")
    manejarResultado(resultado1)

    println("----------------------------------------")

    // --- Intento 2: credenciales incorrectas ---
    println("Estado: Autenticando...")
    val resultado2 = autenticarUsuario("usuario", "0000")
    manejarResultado(resultado2)
}

fun manejarResultado(resultado: ResultadoLogin) {
    // `when` sobre una sealed class: el compilador conoce los 3 subtipos
    // posibles (Exito, Error, Autenticando) y por eso NO exige un `else`
    // -- si mañana agregas un 4to subtipo a ResultadoLogin y te olvidas de
    // agregar su caso acá, el compilador te lo marca como error. Esto es
    // justamente la ventaja de sealed class sobre usar, por ejemplo, un
    // simple String para representar el estado.
    when (resultado) {
        is ResultadoLogin.Exito -> {
            // Función de ámbito `let`: ejecuta el bloque { } usando el valor
            // como "it", y solo tiene sentido usarlo aquí porque ya sabemos
            // (por el `is`) que resultado.perfil no es null. Es útil para
            // encadenar operaciones sobre un valor sin repetir su nombre.
            resultado.perfil.let { perfil ->
                println("Éxito. Bienvenido, ${perfil.nombreUsuario} (${perfil.email})")
            }
        }
        is ResultadoLogin.Error -> {
            println("Error al autenticar: ${resultado.mensaje}")
        }
        ResultadoLogin.Autenticando -> {
            println("Todavía autenticando...")
        }
    }
}
