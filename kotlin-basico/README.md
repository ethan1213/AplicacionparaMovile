# Kotlin básico — ejercicios sueltos (Guías 2 a 5)

Estos ejercicios son Kotlin "puro" (sin Android): se abren y corren directo en **IntelliJ IDEA**, no en Android Studio, aunque técnicamente Android Studio también puede correrlos porque comparte el mismo motor.

## Cómo abrir cada ejercicio en IntelliJ

1. Abre IntelliJ IDEA → **File > Open...** → selecciona la carpeta del ejercicio (ej. `guia2-kotlin-basico`).
2. Si IntelliJ pregunta por un proyecto, elige **"Create Project from Existing Sources"** o simplemente abre el archivo `Main.kt` suelto — igual reconoce que es Kotlin y muestra el botón ▶️ verde al lado de `fun main()`.
3. Click en el triángulo ▶️ verde (o `Ctrl+Shift+F10`) para compilar y correr.

## Carpetas

| Carpeta | Guía | Requiere librería extra |
|---|---|---|
| `guia2-kotlin-basico/` | Guía 2 — variables, null safety, `when` | No |
| `guia3-colecciones/` | Guía 3 — `List`/`Map`, `filter`, `map` | No |
| `guia4-poo/` | Guía 4 — clases, herencia, encapsulamiento, polimorfismo | No |
| `guia5-corrutinas/` | Guía 5 — corrutinas, `sealed class`, `data class` | **Sí**: `kotlinx-coroutines-core` |

### ⚠️ Guía 5 necesita una dependencia

`guia5-corrutinas/Main.kt` usa `delay()` y `runBlocking()`, que **no** vienen en el Kotlin estándar — son parte de la librería `kotlinx-coroutines-core`. Si al abrirlo en IntelliJ ves el archivo en rojo con "Unresolved reference: coroutines", es justamente por esto (no es un error tuyo).

Para que compile en IntelliJ, crea el proyecto como uno de **Gradle** (no "Kotlin simple") y agrega esto a su `build.gradle.kts`:

```kotlin
plugins {
    kotlin("jvm") version "2.2.10"
    application
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
}

application {
    mainClass.set("MainKt")
}
```

(Verificado por línea de comandos con el `kotlinc` que trae Android Studio — el código en sí está probado y funciona.)
