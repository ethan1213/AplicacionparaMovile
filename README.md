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
- [ ] **2. Kotlin básico** — variables, tipos, operadores, null safety (`?.`, `?:`), `when`
- [ ] **3. Colecciones** — `List`/`Map`, ciclos `for`/`while`, funciones de orden superior (`filter`, `map`)
- [ ] **4. POO** — clases, `open`/herencia, encapsulamiento (`private`), polimorfismo (`override`)
- [ ] **5. Corrutinas y sintaxis avanzada** — `suspend`, `delay`, `sealed class`, `data class`, funciones de ámbito (`let`)
- [ ] **6. Primer app en Android Studio** — Activity, Compose, emulador (ya lo tenemos corriendo)

Vamos marcando cada tema como hecho en la bitácora de abajo a medida que lo repasamos.

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
