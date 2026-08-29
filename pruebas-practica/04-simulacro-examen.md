# Simulacro de examen — Evaluación Parcial 1

**Tiempo sugerido: 90 minutos, sin apuntes ni IA.** Formato pensado para parecerse al real: teoría rápida + debugging + un problema de código integrador.

Respuestas en [`04-simulacro-respuestas.md`](./04-simulacro-respuestas.md) — no las mires hasta terminar (o hasta que se acabe el tiempo).

---

## Parte 1 — Teoría rápida (15 min, 8 preguntas)

**1.** (V/F) En Kotlin, todas las clases se pueden heredar por defecto, igual que en Java.

**2.** ¿Qué tipo de dato usarías para representar el estado de una operación con un número fijo de resultados posibles (cargando/éxito/error)?

**3.** ¿Qué devuelve `listOf(1,2,3).filter { it > 5 }`?

**4.** (Opción múltiple) ¿Cuál palabra clave permite que una función se pause sin bloquear el hilo?
a) `open`  b) `suspend`  c) `private`  d) `sealed`

**5.** ¿Qué diferencia hay entre `as` y `as?`?

**6.** Nombra un framework multiplataforma y su lenguaje.

**7.** ¿Qué genera automáticamente una `data class` que no genera una clase normal?

**8.** ¿Qué hace `@Preview` en un proyecto Compose?

---

## Parte 2 — Debugging (20 min, 2 snippets)

Para cada uno: ¿compilación o ejecución?, causa, y arreglo.

### Snippet 7

```kotlin
class Usuario(var nombre: String?)

fun saludar(usuario: Usuario) {
    if (usuario.nombre != null) {
        println("Hola, ${usuario.nombre.length}")
    }
}

fun main() { saludar(Usuario("Ana")) }
```

### Snippet 8

```kotlin
fun contarHasta(n: Int) {
    var i = 0
    while (i < n) {
        println(i)
    }
}

fun main() { contarHasta(5) }
```

---

## Parte 3 — Problema de código integrador (45 min)

**Sistema de préstamos de biblioteca.**

Modela un sistema simple para prestar y devolver libros.

1. Clase `Libro` con `titulo: String`, `autor: String`, y una propiedad `prestado: Boolean` que **no se pueda modificar directamente desde afuera** (encapsulamiento) — empieza en `false`.
2. Métodos públicos en `Libro`:
   - `prestar()`: si el libro ya está prestado, imprime un mensaje de error y no hace nada; si no, lo marca como prestado e imprime confirmación.
   - `devolver()`: si el libro NO está prestado, imprime un mensaje de error; si sí, lo marca como disponible e imprime confirmación.
3. Clase base abierta `MaterialBiblioteca` con `titulo: String` y un método abierto `describir()`. `Libro` debe heredar de `MaterialBiblioteca` (ajusta el punto 1 para que `titulo` venga del padre).
4. Crea también `Revista : MaterialBiblioteca` con una propiedad extra `numeroEdicion: Int`, y sobrescribe `describir()`.
5. En `main()`:
   - Crea una lista de al menos 4 `Libro` y 2 `Revista` (como `List<MaterialBiblioteca>`).
   - Presta 2 libros (incluyendo un intento de prestar el mismo libro dos veces, para probar la validación).
   - Con `filter` + un cast seguro, obtén cuántos libros (`Libro`, no `Revista`) siguen disponibles.
   - Recorre toda la lista llamando `describir()` en cada material (polimorfismo).

**Bonus (si te sobra tiempo):** convierte el préstamo en asíncrono: una `suspend fun prestarConValidacionRemota(libro: Libro): Boolean` que simule `delay(1000L)` como si consultara un servidor antes de confirmar el préstamo, usando `runBlocking` en `main()`.

---

⏱ Cuando termines o se acabe el tiempo, corrige con [`04-simulacro-respuestas.md`](./04-simulacro-respuestas.md).
