# Encuentra y arregla el error — Respuestas

Todos los mensajes de abajo son **reales**, verificados compilando/corriendo cada snippet.

---

## Snippet 1 — error de **ejecución**

```
Exception in thread "main" java.lang.NullPointerException
	at S1Kt.main(s1.kt:4)
```

**Causa:** `nombre!!` fuerza a Kotlin a asumir que `nombre` nunca es `null` — pero la lista SÍ contiene un `null` ("Ana", `null`, "Luis"), así que revienta apenas llega a ese elemento (imprimió "ANA" y ahí murió).

**Arreglo** (con `?.` + Elvis, o filtrando antes):
```kotlin
for (nombre in nombres) {
    println(nombre?.uppercase() ?: "(sin nombre)")
}
// o bien:
for (nombre in nombres.filterNotNull()) {
    println(nombre.uppercase())
}
```

---

## Snippet 2 — error de **ejecución**

```
Exception in thread "main" java.lang.ClassCastException: class java.lang.Integer
cannot be cast to class java.lang.Double
	at S2Kt.obtenerPrecio(s2.kt:2)
```

**Causa:** en el mapa, `"precio" to 10` guarda un **Int** (10, sin punto decimal), no un Double. El cast forzado `as Double` revienta porque el valor real no es de ese tipo.

**Arreglo** (cast seguro + valor por defecto):
```kotlin
fun obtenerPrecio(datos: Map<String, Any>): Double {
    return (datos["precio"] as? Number)?.toDouble() ?: 0.0
}
```
(`as? Number` cubre tanto si viene como `Int` o `Double`, y `.toDouble()` normaliza.)

---

## Snippet 3 — error de **compilación** (dos errores, en cadena)

```
s3.kt:4:31: error: this type is final, so it cannot be extended.
class Perro(nombre: String) : Animal(nombre) {

s3.kt:5:5: error: 'sonido' in 'Animal' is final and cannot be overridden.
    override fun sonido() = println("Guau")
```

**Causa:** `Animal` y su método `sonido()` no están marcados `open`. Kotlin es "final por defecto" — ninguna clase/método se puede heredar/sobrescribir salvo que lo declares explícitamente `open`.

**Arreglo:**
```kotlin
open class Animal(val nombre: String) {
    open fun sonido() = println("Sonido genérico")
}
class Perro(nombre: String) : Animal(nombre) {
    override fun sonido() = println("Guau")
}
```

---

## Snippet 4 — error de **compilación**

```
s4.kt:4:9: error: 'val' cannot be reassigned.
        total = total + precio
```

**Causa:** `total` se declaró con `val` (inmutable) pero el `for` intenta reasignarlo en cada vuelta (`total = total + precio`).

**Arreglo:** cambiar `val total` por `var total`:
```kotlin
fun calcularTotal(precios: List<Double>): Double {
    var total = 0.0
    for (precio in precios) {
        total = total + precio
    }
    return total
}
```
(Bonus: la forma más idiomática en Kotlin sería directamente `precios.sum()`.)

---

## Snippet 5 — error de **ejecución**

```
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException:
Index 3 out of bounds for length 3
	at S5Kt.ultimoElemento(s5.kt:2)
```

**Causa:** clásico error "off-by-one". Una lista de tamaño 3 tiene índices válidos `0, 1, 2` — el último es `size - 1`, no `size`. `lista[lista.size]` apunta a una posición que no existe.

**Arreglo:**
```kotlin
fun ultimoElemento(lista: List<Int>): Int {
    return lista[lista.size - 1]
    // o mejor, más idiomático y sin riesgo de off-by-one:
    // return lista.last()
}
```

---

## Snippet 6 — error de **compilación**

```
s6.kt:2:12: error: 'when' expression must be exhaustive. Add an 'else' branch.
    return when {
```

**Causa:** el `when` se usa como **expresión** (su resultado se hace `return`), así que Kotlin exige que cubra TODOS los casos posibles de `Int` — y `edad < 65` no cubre `edad >= 65` (por ejemplo, 70). Como no hay `else`, el compilador no puede garantizar que la función siempre devuelva algo.

**Arreglo:**
```kotlin
fun clasificarEdad(edad: Int): String {
    return when {
        edad < 13 -> "Niño"
        edad < 18 -> "Adolescente"
        edad < 65 -> "Adulto"
        else -> "Adulto mayor"
    }
}
```
