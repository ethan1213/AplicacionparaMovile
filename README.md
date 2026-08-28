# Aplicación para Móvil

Proyecto de la clase de desarrollo móvil. App Android nativa hecha en **Kotlin** con **Jetpack Compose** (UI declarativa, sin XML de layouts).

Este README también funciona como **bitácora de aprendizaje**: cada vez que avancemos en una clase, agregamos una entrada abajo con qué se hizo y qué se aprendió. La idea es que sirva de repaso antes de la prueba.

## Stack

| Herramienta | Versión | Para qué sirve |
|---|---|---|
| Kotlin | 2.2.10 | Lenguaje principal |
| Jetpack Compose | BOM 2026.02.01 | Construir la UI con funciones (`@Composable`) en vez de XML |
| Android Gradle Plugin (AGP) | 9.2.1 | Plugin de Gradle que sabe compilar proyectos Android |
| Gradle | 9.4.1 (via wrapper) | Sistema de build |
| compileSdk / targetSdk | 37 | Versión de Android API contra la que compila/apunta la app |
| minSdk | 24 | Versión mínima de Android donde corre la app (Android 7.0) |

## Cómo correr el proyecto

1. Abrir la carpeta en **Android Studio**.
2. Dejar que sincronice Gradle (icono del elefante 🐘, o pasa solo al abrir).
3. Elegir un emulador o conectar un celular con depuración USB.
4. Botón ▶️ Run, o `Shift+F10`.

Desde terminal (sin abrir Android Studio):

```bash
./gradlew assembleDebug        # compila el APK debug
./gradlew testDebugUnitTest    # corre los tests unitarios
./gradlew --stop               # mata los daemons de Gradle (útil si se cuelga por RAM)
```

## Estructura del proyecto

```
app/src/main/java/.../MainActivity.kt   -> pantalla principal (Activity + Composables)
app/src/main/java/.../ui/theme/         -> colores, tipografía y tema (Material 3)
app/src/main/res/                       -> recursos: strings, íconos, imágenes
app/src/main/AndroidManifest.xml        -> declara la Activity, permisos, etc.
app/build.gradle.kts                    -> dependencias y config del módulo app
gradle/libs.versions.toml               -> catálogo de versiones (un solo lugar para todas las versiones de librerías)
```

## Ruta de aprendizaje — rumbo a la Prueba 1 (Evaluación Parcial 1)

Según las guías del ramo (DSY1105), la primera evaluación cubre estos 6 temas en orden. Los ejercicios de código de las guías 2 a 5 son Kotlin "puro" (sin Android, se corren en IntelliJ) y viven en la carpeta [`kotlin-basico/`](./kotlin-basico); la guía 6 ya es este mismo proyecto Android.

- [x] **1. Ecosistema móvil** — Nativo vs Multiplataforma, por qué Kotlin
- [x] **2. Kotlin básico** — variables, tipos, operadores, null safety (`?.`, `?:`), `when`
- [x] **3. Colecciones** — `List`/`Map`, ciclos `for`/`while`, funciones de orden superior (`filter`, `map`)
- [x] **4. POO** — clases, `open`/herencia, encapsulamiento (`private`), polimorfismo (`override`)
- [x] **5. Corrutinas y sintaxis avanzada** — `suspend`, `delay`, `sealed class`, `data class`, funciones de ámbito (`let`)
- [x] **6. Primer app en Android Studio** — Activity, Compose, emulador (ya lo tenemos corriendo)

Vamos marcando cada tema como hecho en la bitácora de abajo a medida que lo repasamos.

## Meta futura: conectar con el backend VeloUrbe (Spring Boot)

Después de la Prueba 1 hay que integrar esta app con el backend de otro ramo: **[velourbe-platform](https://github.com/ethan1213/velourbe-platform)** — plataforma de arriendo de patinetas eléctricas.

- Stack: Spring Boot 3.5, Java 21, Docker/Docker Compose, PostgreSQL (una BD por servicio).
- Arquitectura: `Cliente → API Gateway (:8080) → BFF (:8083) → 10 microservicios` (auth, rental, payment, notification, analytics, logistics, maintenance, support, station, review), cada uno en capas Controller → Service → Repository.
- Auth vía JWT (`user-auth-service`).
- **Lo que implica para el proyecto móvil** (adelanto, aún no lo hacemos): agregar una capa de red con corrutinas (Guía 5) — típicamente `Retrofit` u `Ktor Client` — que le hable al API Gateway, guardar el JWT tras el login, y mostrar los datos (patinetas, arriendos, etc.) en pantallas Compose.

## Errores típicos de Kotlin y cómo arreglarlos

Dos categorías: **errores de compilación** (el código ni siquiera corre, IntelliJ te lo marca en rojo) y **errores de ejecución** (compila, pero revienta o se comporta mal al correr).

### Errores de compilación (los ve el IDE antes de correr nada)

| Mensaje | Qué significa | Cómo arreglarlo |
|---|---|---|
| `Val cannot be reassigned` | Intentaste hacer `variable = otroValor` sobre algo declarado con `val` | Si de verdad necesitas reasignarla, cámbiala a `var`. Si no, es una pista de que estabas usando la variable mal. |
| `Cannot access '...': it is private in '...'` | Intentaste usar desde afuera una propiedad/método marcado `private` | Si necesitas leerlo desde afuera, agrega un método público que lo exponga controladamente (como `mostrarPuesto()` en la Guía 4) — no lo hagas `public` a la fuerza salvo que tenga sentido. |
| `Unresolved reference: nombreDeAlgo` | Kotlin no encuentra esa variable/función/clase | Typo en el nombre (revisa mayúsculas/minúsculas), o te falta un `import`, o la variable está declarada más abajo/en otra función (fuera de alcance). |
| `Type mismatch: inferred type is X but Y was expected` | Estás pasando un dato de un tipo donde se esperaba otro | Revisa la firma de la función/variable. A veces falta convertir explícito: `numeroTexto.toInt()`, `numeroInt.toDouble()`, etc. |
| `Smart cast to 'X' is impossible` | Kotlin no puede garantizar que una variable nullable siga sin ser null en ese punto (ej. es un `var` que otro hilo podría cambiar) | Usa `?.`, guarda el valor en un `val` local primero (`val local = variable ?: return`), o usa `!!` solo si estás 100% seguro (ver tabla de abajo por qué evitarlo). |
| `This class is final, so it cannot be extended` (o el IDE no te deja escribir `: ClaseBase()`) | Olvidaste poner `open` en la clase/método padre | Kotlin es "final por defecto" al revés que Java — agrega `open class` y `open fun` en la clase base (ver Guía 4). |

### Errores de ejecución (compila, pero falla al correr)

| Excepción | Causa típica | Cómo prevenirla |
|---|---|---|
| `NullPointerException` (NPE) | Usaste `!!` (non-null assertion) sobre algo que en verdad era `null`, o el código viene de Java/una API externa que no respeta null-safety | Evita `!!` salvo casos muy puntuales; prefiere `?.`, `?:` (Elvis) o un `if (x != null)`. Este es justo el error que Kotlin fue diseñado para prevenir en tiempo de compilación — si aparece, casi siempre es porque se usó `!!` para "silenciar" al compilador en vez de manejar el caso null de verdad. |
| `ClassCastException` | Un `as TipoX` forzado sobre un valor que en realidad no es de ese tipo (ver Guía 3, función `precioPromedio`) | Usa `as?` (cast seguro) en vez de `as`, y maneja el `null` resultante con `?:`. |
| `IndexOutOfBoundsException` | Accediste a `lista[i]` con un índice que no existe (ej. lista vacía, o `i == lista.size` en vez de `lista.size - 1`) | Usa `lista.getOrNull(i)`, revisa `lista.isEmpty()` antes, o usa funciones como `firstOrNull()`/`lastOrNull()` en vez de `[0]`/`[size-1]` a mano. |
| La app se congela / no responde (ANR en Android) | Se hizo una operación lenta (red, disco) en el hilo principal en vez de en una corrutina | Todo lo que demore (llamadas a internet, lectura de archivos grandes) debe ir en una `suspend fun` lanzada desde `viewModelScope`/`lifecycleScope`, nunca directo en el hilo de UI. |
| `Gradle build daemon disappeared` / `OutOfMemoryError` | Poca RAM libre, varios daemons de Gradle duplicados corriendo a la vez | Ver sección de abajo ("Problemas conocidos"). |

**Regla general para leer cualquier error:** el compilador/consola SIEMPRE dice el **archivo**, la **línea** y a veces la **columna exacta** (con `^^^^` apuntando al culpable, como vimos en vivo con el ejemplo de `salario private` en la Guía 4). Léelo de atrás para adelante: primero el mensaje de la última línea (la causa raíz), no te asustes con el resto del stack trace.

## Problemas conocidos / soluciones

- **`compileSdk` desalineado con las dependencias**: si `./gradlew build` falla con "requires libraries and applications that depend on it to compile against version X or later", hay que subir `compileSdk`/`targetSdk` en `app/build.gradle.kts` a esa versión.
- **Gradle daemon se cae por falta de memoria** (`Gradle build daemon disappeared unexpectedly`, JVM `OutOfMemory`): pasa si hay varios daemons duplicados + Android Studio + Chrome abiertos a la vez. Solución: `./gradlew --stop` y volver a compilar.

---

## Bitácora de clases

> Formato de cada entrada: fecha, qué se hizo, qué se aprendió. Se va agregando una por clase.

### 2026-08-28 — Setup inicial del proyecto

**Qué se hizo:**
- Se creó el proyecto en Android Studio (template por defecto: Empty Activity + Compose).
- Se detectó que no compilaba: las dependencias (`core-ktx 1.19.0`, `lifecycle 2.11.0`) pedían `compileSdk 37` y el proyecto estaba en `36` → se subió `compileSdk` y `targetSdk` a `37`.
- Se inicializó git, se configuró `.gitignore`, y se subió el primer commit a GitHub (`origin/main`).

**Conceptos clave:**
- **`compileSdk`**: contra qué versión de las APIs de Android compila el código (afecta qué métodos/clases nuevas puedes usar). No es lo mismo que el Android que corre en el celular.
- **`targetSdk`**: le dice al sistema Android para qué versión "diseñaste" la app; activa comportamientos nuevos del sistema operativo.
- **`minSdk`**: la versión más vieja de Android donde tu app puede instalarse. Bajarlo = más dispositivos compatibles, pero menos APIs modernas disponibles.
- **Gradle**: el sistema que compila el proyecto (descarga dependencias, corre tareas como `assembleDebug`, `test`, `lint`). `gradlew` es un wrapper: no necesitas tener Gradle instalado, el proyecto trae su propia versión.
- **`libs.versions.toml`**: catálogo de versiones — en vez de escribir la versión de cada librería en cada `build.gradle.kts`, se define una sola vez ahí y se referencia (`libs.androidx.core.ktx`). Evita tener 3 versiones distintas de la misma librería por error.
- **`@Composable`**: función que "describe" un pedazo de UI. Jetpack Compose la vuelve a ejecutar (recomponer) cuando cambian los datos que usa, y así actualiza la pantalla — no manipulas Views a mano como en el sistema viejo (XML + `findViewById`).
- **`git`**: se diferenció `.gitignore` de raíz (ignora `local.properties`, carpetas `.idea` sensibles, logs de crash) del `.gitignore` dentro de `app/` (ignora `app/build`, la carpeta donde Gradle deja los archivos compilados — nunca se sube a git, se regenera siempre).

**Dudas / para repasar antes de la prueba:**
- _(vamos agregando acá lo que cueste más)_

---

### 2026-08-28 — Tema 1: Ecosistema móvil (Nativo vs Multiplataforma)

**Qué se hizo:**
- Repaso guiado de los dos enfoques para construir apps móviles, en preparación para la Guía 1.

**Conceptos clave:**
- **Desarrollo nativo**: se programa por separado para cada sistema operativo, usando el lenguaje y las herramientas "oficiales" de cada uno.
  - **Android** → Kotlin (moderno, recomendado por Google) o Java (más antiguo). Se programa en Android Studio.
  - **iOS** → Swift (moderno, estándar actual) o Objective-C (legado). Se programa en Xcode.
  - Ventajas: máximo rendimiento y acceso total a las funciones del hardware (cámara, sensores, Bluetooth) apenas Apple/Google las lanzan; la UI se siente 100% "como debe verse" en ese sistema.
  - Desventaja principal: hay que escribir (y mantener) **dos apps distintas** — una en Kotlin, otra en Swift — lo que duplica tiempo y costo de desarrollo.
- **Desarrollo multiplataforma**: se escribe el código **una sola vez** y se compila para varios sistemas operativos a la vez.
  - Frameworks populares: **Flutter** (lenguaje Dart, de Google), **React Native** (JavaScript/TypeScript, de Meta), **Kotlin Multiplatform / KMP** (Kotlin, de JetBrains — permite compartir lógica y a veces UI entre Android/iOS).
  - Ventajas: menos tiempo y costo (un solo equipo/código base), lanzamiento más rápido a ambas tiendas.
  - Desventajas: rendimiento algo menor que el nativo en apps muy exigentes (juegos, edición de video), y a veces hay que esperar a que el framework "traduzca" el acceso a funciones nuevas del hardware.
- **¿Por qué Kotlin es estratégico?** Es el lenguaje oficial recomendado por Google para Android desde 2019, es más conciso y seguro que Java (menos código repetitivo, previene errores de `null` en tiempo de compilación con **null safety**), interopera 100% con librerías Java existentes, y además se puede reutilizar para lógica de servidor (backend) o multiplataforma (KMP) — un mismo lenguaje sirve para varias partes del stack.

**Dudas / para repasar antes de la prueba:**
- _(vamos agregando acá lo que cueste más)_

---

### 2026-08-28 — Tema 2: Kotlin básico (variables, null safety, when)

**Qué se hizo:**
- Se creó [`kotlin-basico/guia2-kotlin-basico/Main.kt`](./kotlin-basico/guia2-kotlin-basico/Main.kt) resolviendo el ejercicio de la Guía 2: variables y operadores aritméticos, null safety con `?.`, y `when` para días de la semana.
- Se verificó que compila y corre bien con el `kotlinc` que trae Android Studio.

**Conceptos clave:**
- **`val` vs `var`**: `val` = no se puede reasignar (preferir siempre que se pueda); `var` = sí se puede reasignar. No existe `final`/mutable por separado como en Java, es esto directamente en la declaración.
- **Inferencia de tipos**: Kotlin detecta el tipo solo (`val x = 10` ya es `Int`), no hace falta escribirlo siempre.
- **Interpolación de strings**: `"La suma es: $suma"` en vez de concatenar con `+` como en Java.
- **Null safety**: un tipo normal (`String`) *nunca* puede ser `null` — el compilador lo prohíbe. Si necesitas que sí pueda serlo, lo marcas explícitamente con `?` (`String?`). El operador `?.` (safe call) evita el `NullPointerException`: si la variable es `null`, la expresión completa da `null` en vez de reventar el programa.
- **`when`**: el reemplazo de Kotlin para `switch`. Se puede usar como expresión (devuelve un valor directo, como se hizo aquí con `nombreDia = when(...) {...}`) y siempre conviene poner `else` para el caso no contemplado.

**Dudas / para repasar antes de la prueba:**
- _(vamos agregando acá lo que cueste más)_

---

### 2026-08-28 — Tema 3: Colecciones (List, Map, filter, map)

**Qué se hizo:**
- Se creó [`kotlin-basico/guia3-colecciones/Main.kt`](./kotlin-basico/guia3-colecciones/Main.kt): inventario como `List<Map<String, Any>>`, función de búsqueda con `for`, función de precio promedio con cast seguro, y `filter`/`map` sobre la colección. Verificado con `kotlinc`.

**Conceptos clave:**
- **`listOf`/`mapOf`** crean colecciones **inmutables** (no se puede agregar/quitar después); si necesitas modificarlas, existen `mutableListOf`/`mutableMapOf`.
- **`Any`** es el equivalente Kotlin de `Object` en Java: el tipo padre de todo. Cuando un `Map` tiene valores de distinto tipo (`String`, `Double`...), termina siendo `Map<String, Any>` y hay que castear al leer.
- **Cast seguro `as?`** vs **cast forzado `as`**: `as?` devuelve `null` si el cast falla en vez de reventar con `ClassCastException`. Regla: usar `as?` siempre que el tipo real del dato no esté 100% garantizado (ej. datos externos).
- **`filter`** devuelve una lista con los elementos que cumplen una condición; **`map`** transforma cada elemento en otra cosa. Ambas son "funciones de orden superior" (reciben otra función/lambda como parámetro) y son más declarativas que escribir el `for` a mano.
- **`it`**: nombre implícito del parámetro cuando una lambda tiene un solo argumento (`inventario.map { it["nombre"] }`).

**Dudas / para repasar antes de la prueba:**
- _(vamos agregando acá lo que cueste más)_

---

### 2026-08-28 — Tema 4: POO (clases, herencia, encapsulamiento, polimorfismo)

**Qué se hizo:**
- Se creó [`kotlin-basico/guia4-poo/Main.kt`](./kotlin-basico/guia4-poo/Main.kt): `Persona` (clase base `open`) y `Empleado` (hereda, con `salario` privado y `presentarse()` sobrescrito). Verificado con `kotlinc`, incluyendo ver en vivo el error real del compilador al intentar acceder a `salario` desde afuera.

**Conceptos clave:**
- **Kotlin es "final" por defecto**: al revés que Java, ninguna clase/método se puede heredar/sobrescribir salvo que lo marques explícito con **`open`**. Es una decisión de diseño para evitar herencias accidentales.
- **Herencia**: `class Empleado(...) : Persona(nombre, edad)` — Empleado "ES-UN" Persona y le pasa los datos al constructor padre.
- **Encapsulamiento**: `private` restringe el acceso a una propiedad/método a solo dentro de la misma clase. Protege datos sensibles (como un salario) de ser leídos/modificados desde cualquier parte del código.
- **Polimorfismo**: `override fun presentarse()` reemplaza el comportamiento heredado. Lo interesante: si guardas objetos de distintas subclases en una lista del tipo padre (`List<Persona>`), Kotlin igual ejecuta la versión correcta de cada uno según su tipo **real**, no según el tipo declarado de la lista.

**Dudas / para repasar antes de la prueba:**
- _(vamos agregando acá lo que cueste más)_

---

### 2026-08-28 — Tema 5: Corrutinas y sintaxis avanzada

**Qué se hizo:**
- Se creó [`kotlin-basico/guia5-corrutinas/Main.kt`](./kotlin-basico/guia5-corrutinas/Main.kt): login simulado con `suspend fun` + `delay(2000L)`, estados modelados con `sealed class ResultadoLogin` (`Exito`/`Error`/`Autenticando`), datos con `data class PerfilUsuario`, y manejo con `when` + función de ámbito `let`. Verificado con `kotlinc` agregando la librería `kotlinx-coroutines-core` al classpath (ya la teníamos en caché de Gradle por las dependencias de Compose).

**Conceptos clave:**
- **`suspend fun`**: función que se puede "pausar" (ej. en `delay()`) sin bloquear el hilo donde corre. Solo se puede llamar desde otra `suspend fun` o desde dentro de una corrutina.
- **`runBlocking`**: crea una corrutina y bloquea el hilo actual hasta que termina. Sirve para "puentear" código normal con código `suspend` (útil en `main()`/tests); en una app Android real casi no se usa porque bloquear el hilo de UI = app congelada — ahí se usan `viewModelScope`/`lifecycleScope`.
- **`data class`**: pensada solo para guardar datos; Kotlin genera gratis `equals()`, `hashCode()`, `toString()` y `copy()`.
- **`sealed class`**: define un conjunto **cerrado** de subtipos conocidos (declarados en el mismo archivo). Ideal para modelar "estados" de una operación — el `when` sobre una sealed class no necesita (ni debería necesitar) `else`, porque el compilador conoce todos los casos posibles y avisa si falta cubrir uno.
- **Función de ámbito `let`**: ejecuta un bloque usando el valor como `it` (o un nombre custom, como `perfil` en el ejemplo). Útil para encadenar operaciones sobre un valor sin repetir su nombre.

**Dudas / para repasar antes de la prueba:**
- _(vamos agregando acá lo que cueste más)_

---

### 2026-08-28 — Tema 6: Primer app en Android Studio (XML vs Compose) — Ruta a Prueba 1 completa ✅

**Qué se hizo:**
- Repaso conceptual: este mismo proyecto (`AplicacionparaMovile`) ES el entregable de la Guía 6. Ya estaba armado y corriendo desde el setup inicial (ver primera entrada de esta bitácora).
- Con esto, los 6 temas de la Evaluación Parcial 1 quedan cubiertos.

**Conceptos clave:**
- **XML (sistema "clásico")**: cada pantalla se describe en un archivo `.xml` separado del código (`res/layout/activity_main.xml`), y en Kotlin se "conectan" las vistas con `findViewById()`. Es el sistema que usaban casi todas las apps Android hasta ~2021.
- **Jetpack Compose (lo que usa este proyecto)**: la UI se describe directo en Kotlin con funciones `@Composable` (ver `MainActivity.kt` → `Greeting(...)`). No hay XML de layout ni `findViewById`: cuando cambian los datos, Compose vuelve a ejecutar (recompone) la función y la pantalla se actualiza sola.
- **¿Por qué Compose y no XML?** Es el estándar moderno recomendado por Google: menos código repetitivo, la UI y la lógica que la afecta quedan en el mismo lenguaje (Kotlin), y es más fácil de testear/previsualizar (`@Preview`, como `GreetingPreview()` en `MainActivity.kt`).
- **Estructura de un proyecto Android**: `AndroidManifest.xml` (declara qué Activities/permisos tiene la app), `MainActivity.kt` (punto de entrada, `onCreate()`), `ui/theme/` (colores/tipografía), `res/` (recursos: strings, íconos).

**Ruta de la Prueba 1: completa.** Repasar la bitácora completa (Temas 1-6) + correr los ejercicios de `kotlin-basico/` de nuevo sin mirar la solución es el mejor ensayo antes de la evaluación.

**Dudas / para repasar antes de la prueba:**
- _(vamos agregando acá lo que cueste más)_

---

<!--
Plantilla para la próxima entrada — copiar y pegar arriba de esta línea:

### YYYY-MM-DD — Tema de la clase

**Qué se hizo:**
-

**Conceptos clave:**
-

**Dudas / para repasar antes de la prueba:**
-
-->
