# Prueba de teoría — Respuestas y explicaciones

## Tema 1 — Ecosistema móvil

**1.** b) **Swift**. (Objective-C es el lenguaje legado; Swift es el estándar actual).

**2.** **Falso**. Flutter usa **Dart** (de Google). Kotlin Multiplatform (KMP) sí usa Kotlin, pero Flutter no.

**3.** Ejemplos válidos: máximo rendimiento (acceso directo al hardware), mejor experiencia de usuario (UI 100% nativa del sistema), acceso inmediato a funciones nuevas del SO apenas se lanzan.

**4.** Ejemplos: **Flutter** → Dart, **React Native** → JavaScript/TypeScript, **Kotlin Multiplatform (KMP)** → Kotlin.

**5.** Idea esperada: es el lenguaje oficial recomendado por Google para Android desde 2019, es más conciso/seguro que Java (null safety), interopera con Java, y se puede reusar en backend/multiplataforma (un lenguaje para varias capas del stack).

## Tema 2 — Kotlin básico

**6.** `val` = no se puede reasignar después de la primera asignación (inmutable). `var` = sí se puede reasignar.

**7.** Imprime **`-1`**.
> `texto?.length` sobre `null` da `null`; el operador Elvis `?:` reemplaza ese `null` por `-1`.

**8.** b) `?.`
> `!!` es "non-null assertion" (fuerza y puede lanzar NPE), `?:` es el operador Elvis (valor por defecto), `::` es referencia a función/propiedad.

**9.** **Falso**, con matiz: si el `when` se usa como **expresión** (devuelve un valor, como `val x = when(...) {...}`) y NO cubre todos los casos posibles del tipo (ej. un `Int` cualquiera), el compilador SÍ exige `else`. Pero si el `when` cubre todos los casos posibles de una `sealed class` o un `enum`, o si se usa como **sentencia** (sin devolver valor), el `else` no es obligatorio. Buena práctica igual: ponerlo siempre que el conjunto de casos no esté 100% cerrado.

**10.** `?.` (safe call) evita el `NullPointerException` devolviendo `null` en vez de reventar si la variable es `null`. `?:` (Elvis) se usa DESPUÉS de una expresión que puede dar `null`, para reemplazarlo por un valor por defecto. Muchas veces se combinan: `texto?.length ?: 0`.

## Tema 3 — Colecciones

**11.** `filter` **selecciona** elementos que cumplen una condición (la lista resultante puede ser más chica, mismo tipo de elemento). `map` **transforma** cada elemento en otra cosa (la lista resultante tiene el mismo tamaño, pero puede cambiar el tipo).

**12.** Imprime **`[20, 40]`**.
> `filter { it % 2 == 0 }` deja `[2, 4]` (los pares), `map { it * 10 }` los convierte en `[20, 40]`.

**13.** `listOf(...)` crea una lista **inmutable** (no se puede agregar/quitar/reemplazar elementos después de creada). `mutableListOf(...)` sí permite `.add()`, `.remove()`, etc.

**14.** `as?` es un cast **seguro**: si el valor no es del tipo esperado, devuelve `null` en vez de lanzar una excepción. `as` (cast forzado) revienta el programa si el tipo no coincide.

**15.** `ClassCastException`.

## Tema 4 — POO

**16.** Porque en Kotlin **todas las clases son `final` por defecto** (no se puede heredar de ellas) salvo que se marquen explícitamente `open`. Es al revés que Java, donde por defecto se puede heredar de cualquier clase salvo que la marques `final`. Es una decisión de diseño para evitar herencias accidentales.

**17.** No compila porque `Persona` **no está marcada `open`**. Por defecto es `final`, así que `Empleado : Persona(nombre)` falla con un error como *"This type is final, so it cannot be inherited from"*. Arreglo: `open class Persona(val nombre: String)`.

**18.** Encapsulamiento = restringir el acceso a los datos internos de una clase para protegerlos de modificaciones no controladas desde afuera. Ejemplo: `private val salario: Double` dentro de `Empleado` — solo el propio `Empleado` puede leer/usar `salario` directamente; el resto del código debe pasar por un método público que lo exponga de forma controlada.

**19.** Polimorfismo = un mismo método (mismo nombre) se comporta distinto según el objeto real que lo ejecuta. Ejemplo: `Persona.presentarse()` imprime un saludo genérico, pero `Empleado` hace `override fun presentarse()` y lo hace más específico — la misma llamada `objeto.presentarse()` da resultados distintos según si `objeto` es realmente una `Persona` o un `Empleado`.

**20.** Se ejecuta la versión de **`Empleado`** (la sobrescrita). Kotlin (como Java) decide en **tiempo de ejecución** cuál implementación usar según el tipo **real** del objeto, no según el tipo con el que está declarada la variable/lista (`List<Persona>`). Eso es justamente el polimorfismo.

## Tema 5 — Corrutinas y sintaxis avanzada

**21.** Marca la función como "pausable": puede detenerse (ej. en un `delay()` o una llamada de red) y devolver el control **sin bloquear el hilo**, retomando después donde quedó. Solo se puede llamar desde otra `suspend fun` o desde dentro de una corrutina.

**22.** `runBlocking` crea una corrutina y **bloquea** el hilo actual hasta que termina todo lo de adentro — sirve para "puentear" código normal (no-suspend) con código `suspend`, típicamente en `main()` o tests. No se usa dentro de una app Android real porque si se llama en el hilo de UI, **congela la pantalla** mientras espera (ANR). Ahí se usan `viewModelScope`/`lifecycleScope`, que NO bloquean.

**23.** Una `sealed class` define un conjunto **cerrado y conocido** de subtipos (declarados en el mismo archivo). Ventaja sobre un `String`: el compilador conoce TODOS los casos posibles, así que un `when` sobre esa sealed class puede detectar si te olvidaste de cubrir algún caso. Con un `String` cualquiera podrías escribir mal el texto (`"exito"` vs `"Exito"`) y el compilador jamás te avisaría — el error aparecería recién en tiempo de ejecución (o nunca).

**24.** Genera automáticamente `equals()`, `hashCode()`, `toString()` y `copy()`. Con una clase normal (`class` sin `data`) tendrías que escribir todo eso a mano.

## Tema 6 — Android Studio / Compose

**25.** Con XML, la UI se describe en un archivo separado (`.xml`) y en Kotlin se conecta con `findViewById()` para manipular cada vista. Con Compose, la UI se describe directo en Kotlin con funciones `@Composable`, sin archivo separado ni `findViewById` — cuando cambian los datos, Compose vuelve a ejecutar (recompone) la función y la pantalla se actualiza sola.

**26.** `@Preview` permite ver cómo se ve un `@Composable` directamente en el panel de diseño de Android Studio, **sin tener que correr la app completa en un emulador/celular** — acelera mucho el ciclo de prueba visual.

---

### Puntaje orientativo

- 22-26 correctas: dominas el contenido, listo para la prueba.
- 15-21: bien, pero repasa los temas donde fallaste antes de la prueba.
- Menos de 15: vuelve a la bitácora del README principal y a los ejercicios de `kotlin-basico/` antes de seguir.
