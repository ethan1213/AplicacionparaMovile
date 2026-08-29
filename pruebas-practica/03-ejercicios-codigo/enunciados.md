# Ejercicios de código — enunciados

Tres ejercicios nuevos (no son los mismos de las guías) para escribir tú mismo en IntelliJ. Las soluciones verificadas están en `soluciones/` — intenta resolverlo solo antes de mirarlas.

---

## Ejercicio A — Sistema de notas de estudiantes (Temas 2 y 3)

Simula una libreta de notas usando `Map<String, Any?>` (cada estudiante es un mapa con `"nombre"` y `"nota"`; la nota puede ser `null` si el estudiante aún no rindió).

1. Crea una `List` con al menos 6 estudiantes de prueba, donde **al menos 2** tengan `"nota" to null`.
2. Función `promedioCurso(estudiantes: List<Map<String, Any?>>): Double` que calcule el promedio **ignorando** a los estudiantes sin nota (usa un ciclo, cast seguro `as?`, y Elvis `?:`).
3. Función `clasificar(nota: Double?): String` que use `when` para devolver:
   - `null` → `"Sin nota"`
   - `nota >= 4.0` → `"Aprobado"`
   - cualquier otro caso → `"Reprobado"`
4. Con `filter`, obtén la lista de estudiantes **aprobados** (nota no nula y >= 4.0).
5. Con `map`, obtén solo los **nombres** de los estudiantes aprobados.
6. Imprime todo: promedio del curso, y la lista de nombres aprobados.

**Pregunta de reflexión:** ¿por qué usar `Map<String, Any?>` (con `?`) en vez de `Map<String, Any>` acá es importante para que el código sea correcto?

---

## Ejercicio B — Sistema de vehículos (Tema 4)

Modela `Vehiculo` (clase base) y dos subclases `Auto` y `Moto`.

1. Clase base `Vehiculo` (marcada para permitir herencia) con propiedades `marca: String` y `modelo: String`, y un método `describir()` que imprima `"[marca] [modelo]"`.
2. `Vehiculo` también debe tener una propiedad `kilometraje` que:
   - Empiece en `0`.
   - **No se pueda modificar directamente desde afuera** de la clase (encapsulamiento).
   - Tenga un método público `avanzar(km: Int)` que sume kilómetros al total (y no permita sumar un número negativo — si `km < 0`, imprime un mensaje de error y no hace nada).
   - Tenga un método público `verKilometraje()` que lo muestre.
3. `Auto` hereda de `Vehiculo`, agrega `numeroPuertas: Int`, y sobrescribe `describir()` para incluir el número de puertas.
4. `Moto` hereda de `Vehiculo`, agrega `cilindrada: Int`, y sobrescribe `describir()` para incluir la cilindrada.
5. En `main()`: crea un `Auto` y una `Moto`, guárdalos en una `List<Vehiculo>`, avanza kilómetros en ambos (incluyendo un intento con número negativo para probar la validación), y recorre la lista llamando `describir()` y `verKilometraje()` en cada uno.

**Pregunta de reflexión:** ¿qué pasaría si `avanzar()` no existiera y `kilometraje` fuera simplemente `public var`? ¿Qué problema de diseño se evita con el encapsulamiento acá?

---

## Ejercicio C — Estado de un pedido (Tema 5)

Simula la consulta del estado de un pedido de delivery, de forma asíncrona.

1. `data class Pedido(val id: Int, val producto: String)`.
2. `sealed class EstadoPedido` con estos subtipos:
   - `object Pendiente` (el pedido aún no se procesa)
   - `data class EnCamino(val minutosRestantes: Int)`
   - `object Entregado`
   - `data class Cancelado(val motivo: String)`
3. `suspend fun consultarEstado(idPedido: Int): EstadoPedido` que:
   - Simule una demora de red con `delay(1500L)`.
   - Devuelva un estado distinto según el `idPedido` (tú decides la lógica, ej. `id == 1` → `EnCamino(15)`, `id == 2` → `Entregado`, `id == 3` → `Cancelado("Local cerrado")`, cualquier otro → `Pendiente`).
4. En `main()` (con `runBlocking`), consulta el estado de al menos 3 pedidos distintos y usa `when` para imprimir un mensaje descriptivo por cada `EstadoPedido` posible (usa `let` en al menos un caso).

**Pregunta de reflexión:** si mañana agregas un quinto estado `object Reembolsado` a la `sealed class`, ¿qué te avisa el compilador en el `when` que ya tenías escrito, y por qué es útil?
