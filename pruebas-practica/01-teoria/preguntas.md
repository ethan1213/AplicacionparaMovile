# Prueba de teoría — Preguntas

No mires `respuestas.md` hasta responder todo. Anota tus respuestas aparte.

## Tema 1 — Ecosistema móvil

**1.** (Opción múltiple) ¿Cuál es el lenguaje estándar actual para desarrollo nativo en iOS?
a) Kotlin  b) Swift  c) Dart  d) Java

**2.** (Verdadero/Falso) Flutter usa Kotlin como lenguaje principal.

**3.** Nombra **dos** ventajas del desarrollo **nativo** frente al multiplataforma.

**4.** Nombra **dos** frameworks multiplataforma y el lenguaje que usa cada uno.

**5.** En un par de líneas: ¿por qué se dice que Kotlin es "estratégico" en el mercado móvil actual?

## Tema 2 — Kotlin básico

**6.** ¿Cuál es la diferencia entre `val` y `var`?

**7.** (Qué imprime) ¿Qué imprime este código?
```kotlin
var texto: String? = "Duoc"
texto = null
println(texto?.length ?: -1)
```

**8.** (Opción múltiple) ¿Cuál operador es el "safe call"?
a) `!!`  b) `?.`  c) `?:`  d) `::`

**9.** (Verdadero/Falso) Un `when` en Kotlin siempre necesita un caso `else`, sin excepción.

**10.** Explica con tus palabras la diferencia entre `?.` y `?:` (Elvis).

## Tema 3 — Colecciones

**11.** ¿Cuál es la diferencia entre `filter` y `map`?

**12.** (Qué imprime) Dado `val numeros = listOf(1, 2, 3, 4, 5)`, ¿qué imprime esto?
```kotlin
val resultado = numeros.filter { it % 2 == 0 }.map { it * 10 }
println(resultado)
```

**13.** ¿Cuál es la diferencia entre `listOf(...)` y `mutableListOf(...)`?

**14.** ¿Qué hace el operador `as?` que `as` no hace?

**15.** ¿Qué excepción se produce si haces un cast forzado (`as`) hacia un tipo incorrecto?

## Tema 4 — POO

**16.** ¿Por qué en Kotlin hay que escribir `open class Persona` para que otra clase pueda heredar de ella, mientras que en Java cualquier clase se puede heredar por defecto?

**17.** (Identifica el problema) ¿Por qué este código no compila?
```kotlin
class Persona(val nombre: String)

class Empleado(nombre: String) : Persona(nombre) {
    fun saludar() = println("Hola $nombre")
}
```

**18.** ¿Qué es el encapsulamiento? Da un ejemplo con `private`.

**19.** ¿Qué es el polimorfismo? Explícalo con un ejemplo de `override`.

**20.** Si `Empleado` hereda de `Persona` y tienes `val lista: List<Persona> = listOf(persona1, empleado1)`, y llamas `.presentarse()` en cada elemento del `for`, ¿qué versión del método se ejecuta para `empleado1`: la de `Persona` o la de `Empleado`? ¿Por qué?

## Tema 5 — Corrutinas y sintaxis avanzada

**21.** ¿Qué hace la palabra clave `suspend` en una función?

**22.** ¿Para qué sirve `runBlocking` y por qué casi no se usa dentro de una app Android real (solo en `main()`/tests)?

**23.** ¿Qué ventaja tiene una `sealed class` sobre usar un simple `String` para representar el estado de una operación (ej. "cargando"/"éxito"/"error")?

**24.** ¿Qué genera automáticamente Kotlin cuando declaras una `data class` que no tendrías gratis con una clase normal?

## Tema 6 — Android Studio / Compose

**25.** ¿Cuál es la diferencia principal entre construir la UI con XML (`findViewById`) y con Jetpack Compose (`@Composable`)?

**26.** ¿Para qué sirve la anotación `@Preview`?
