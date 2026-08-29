# Encuentra y arregla el error

Cada fragmento tiene **un error real** (de compilación o de ejecución). Para cada uno responde:
1. ¿Es error de **compilación** o de **ejecución**?
2. ¿Cuál es la causa exacta?
3. ¿Cómo lo arreglarías? (escribe el código corregido)

No mires `respuestas.md` hasta intentarlo. Si tienes dudas, pégalo en IntelliJ y mira qué te dice de verdad.

---

## Snippet 1

```kotlin
fun main() {
    val nombres: MutableList<String?> = mutableListOf("Ana", null, "Luis")
    for (nombre in nombres) {
        println(nombre!!.uppercase())
    }
}
```

## Snippet 2

```kotlin
fun obtenerPrecio(datos: Map<String, Any>): Double {
    return datos["precio"] as Double
}

fun main() {
    val producto = mapOf("nombre" to "Mouse", "precio" to 10)
    println(obtenerPrecio(producto))
}
```

## Snippet 3

```kotlin
class Animal(val nombre: String) {
    fun sonido() = println("Sonido genérico")
}

class Perro(nombre: String) : Animal(nombre) {
    override fun sonido() = println("Guau")
}
```

## Snippet 4

```kotlin
fun calcularTotal(precios: List<Double>): Double {
    val total = 0.0
    for (precio in precios) {
        total = total + precio
    }
    return total
}
```

## Snippet 5

```kotlin
fun ultimoElemento(lista: List<Int>): Int {
    return lista[lista.size]
}

fun main() {
    println(ultimoElemento(listOf(10, 20, 30)))
}
```

## Snippet 6

```kotlin
fun clasificarEdad(edad: Int): String {
    return when {
        edad < 13 -> "Niño"
        edad < 18 -> "Adolescente"
        edad < 65 -> "Adulto"
    }
}

fun main() {
    println(clasificarEdad(70))
}
```
