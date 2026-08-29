# Simulacro de examen — Respuestas

## Parte 1 — Teoría rápida

**1.** Falso. En Kotlin toda clase es `final` por defecto; hay que marcarla `open` a mano para permitir herencia (al revés que Java).

**2.** Una `sealed class` (conjunto cerrado y conocido de subtipos).

**3.** Una lista vacía: `[]` (ningún elemento de `[1,2,3]` es mayor a 5).

**4.** b) `suspend`.

**5.** `as` es un cast forzado (lanza `ClassCastException` si el tipo no coincide); `as?` es un cast seguro (devuelve `null` en vez de lanzar excepción).

**6.** Ejemplos válidos: Flutter (Dart), React Native (JavaScript/TypeScript), Kotlin Multiplatform (Kotlin).

**7.** `equals()`, `hashCode()`, `toString()` y `copy()`.

**8.** Muestra una vista previa del `@Composable` directo en el panel de diseño de Android Studio, sin correr la app completa en emulador/celular.

---

## Parte 2 — Debugging

### Snippet 7 — error de **compilación**

```
error: smart cast to 'String' is impossible, because 'nombre' is a mutable
property that could be mutated concurrently.
        println("Hola, ${usuario.nombre.length}")
```

**Causa:** aunque se verificó `usuario.nombre != null` justo antes, `nombre` es una propiedad `var` de una **clase** (no una variable local) — Kotlin no puede garantizar que otro hilo no la haya cambiado a `null` justo entre el `if` y el uso. Por eso NO aplica el "smart cast" automático que sí funciona con variables locales.

**Arreglo** (guardar en una variable local primero, o usar `?.`):
```kotlin
fun saludar(usuario: Usuario) {
    val nombre = usuario.nombre
    if (nombre != null) {
        println("Hola, ${nombre.length}")
    }
}
// o más simple:
fun saludar(usuario: Usuario) {
    println("Hola, ${usuario.nombre?.length}")
}
```

### Snippet 8 — error de **ejecución** (cuelga el programa, no lanza excepción)

**Causa:** dentro del `while (i < n)` nunca se incrementa `i` — el bucle se repite **para siempre** imprimiendo `0` infinitas veces. Este tipo de error no lanza ninguna excepción ni mensaje: el programa simplemente nunca termina (en una app real, esto congela la UI = ANR).

**Arreglo:**
```kotlin
fun contarHasta(n: Int) {
    var i = 0
    while (i < n) {
        println(i)
        i++ // <- esto faltaba
    }
}
```

---

## Parte 3 — Sistema de préstamos de biblioteca (solución completa y verificada)

```kotlin
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

open class MaterialBiblioteca(val titulo: String) {
    open fun describir() {
        println("Material: $titulo")
    }
}

class Libro(titulo: String, val autor: String) : MaterialBiblioteca(titulo) {
    private var prestado: Boolean = false

    fun prestar() {
        if (prestado) {
            println("Error: '$titulo' ya está prestado.")
            return
        }
        prestado = true
        println("'$titulo' prestado con éxito.")
    }

    fun devolver() {
        if (!prestado) {
            println("Error: '$titulo' no estaba prestado.")
            return
        }
        prestado = false
        println("'$titulo' devuelto con éxito.")
    }

    fun estaDisponible(): Boolean = !prestado

    override fun describir() {
        println("Libro: $titulo, de $autor (${if (prestado) "prestado" else "disponible"})")
    }
}

class Revista(titulo: String, val numeroEdicion: Int) : MaterialBiblioteca(titulo) {
    override fun describir() {
        println("Revista: $titulo, edición #$numeroEdicion")
    }
}

suspend fun prestarConValidacionRemota(libro: Libro): Boolean {
    println("Consultando disponibilidad remota de '${libro.titulo}'...")
    delay(1000L)
    if (!libro.estaDisponible()) {
        println("Servidor rechazó el préstamo: '${libro.titulo}' no disponible.")
        return false
    }
    libro.prestar()
    return true
}

// OJO con esto -> ver nota "Bug real que encontramos" más abajo.
fun main() = runBlocking<Unit> {
    val libro1 = Libro("Kotlin in Action", "Jemerov & Isakova")
    val libro2 = Libro("Clean Code", "Robert C. Martin")
    val libro3 = Libro("El Principito", "Antoine de Saint-Exupery")
    val libro4 = Libro("1984", "George Orwell")
    val revista1 = Revista("National Geographic", 245)
    val revista2 = Revista("Wired", 88)

    val materiales: List<MaterialBiblioteca> =
        listOf(libro1, libro2, libro3, libro4, revista1, revista2)

    libro1.prestar()
    libro1.prestar() // Debe fallar: ya está prestado.
    libro2.prestar()

    println("----------------------------------------")

    val disponibles = materiales.filter { (it as? Libro)?.estaDisponible() == true }
    println("Libros disponibles: ${disponibles.size}")

    println("----------------------------------------")

    for (m in materiales) {
        m.describir()
    }

    println("----------------------------------------")
    println("Bonus: préstamo con validación remota")
    prestarConValidacionRemota(libro3)
    prestarConValidacionRemota(libro1) // libro1 ya prestado -> debe rechazar
}
```

Salida esperada (resumen): `libro1` queda prestado, el segundo intento de prestarlo falla con mensaje de error, `libro2` se presta bien, "Libros disponibles: 2" (libro3 y libro4), cada material imprime su propia versión de `describir()` (polimorfismo), y el bonus muestra un préstamo remoto exitoso para `libro3` y uno rechazado para `libro1` (ya estaba prestado).

### 🐛 Bug real que encontramos armando esta solución (vale la pena leerlo)

La primera versión tenía `fun main() = runBlocking { ... }` (sin el `<Unit>`). Como la ÚLTIMA línea dentro del bloque era `prestarConValidacionRemota(libro1)` — que devuelve `Boolean` y no se usa — Kotlin infirió que **todo el bloque `runBlocking` devuelve `Boolean`**, y por lo tanto `main()` también. Eso hace que el `.jar` compile bien, ¡pero al ejecutarlo Java dice `"Error: Main method not found"` porque un `main` de verdad debe ser `void`, no `Boolean`!

**Arreglo:** o se fuerza el tipo con `runBlocking<Unit> { ... }`, o se escribe `main` con llaves (cuerpo de bloque, que siempre es `Unit`):
```kotlin
fun main() {
    runBlocking {
        // ...
    }
}
```

**Moraleja:** cuando uses `fun nombre() = algoQueDevuelveUnBloque { ... }` (cuerpo de expresión), el tipo de retorno de tu función queda determinado por la ÚLTIMA expresión de ese bloque — presta atención a qué devuelve esa última línea, sobre todo en `main()`.
