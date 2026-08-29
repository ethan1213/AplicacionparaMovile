# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this repo is

Android app scaffold (Kotlin + Jetpack Compose) built for the course **DSY1105 — Desarrollo de Aplicaciones Móviles** (DuocUC). `README.md` is a living study log ("bitácora de aprendizaje") the student and Claude maintain together — every learning session gets an entry there (what was done, key concepts, open questions). Treat `README.md` as the source of truth for course context and progress; don't duplicate its content here, just know it exists and keep adding to it as sessions continue.

Three independent things live in this one repo:

1. **`app/`** — the actual Android app (Gradle-built).
2. **`kotlin-basico/`** — standalone Kotlin console exercises (Guías 2-5 of the course). **Not part of the Gradle build** — each folder is a single `Main.kt` meant to be opened directly in IntelliJ, or compiled by hand with `kotlinc`.
3. **`pruebas-practica/`** — self-test material (theory Q&A, debugging snippets, coding exercises + solutions) for the course's first evaluation. Also plain `.kt` files outside the Gradle build.

## Commands

### Android app (`app/`)
```bash
./gradlew assembleDebug        # build debug APK
./gradlew testDebugUnitTest    # run unit tests
./gradlew build                # full build: assemble + lint + test (heavier, see gotcha below)
./gradlew --stop               # kill Gradle daemons (fixes the OOM crash below)
```

### `kotlin-basico/` and `pruebas-practica/*/soluciones/` (plain Kotlin, no Gradle)
These are not wired into `settings.gradle.kts`. Compile/run a single file with the Kotlin compiler bundled inside Android Studio (no separate Kotlin install needed):
```bash
KOTLINC="/c/Program Files/Android/Android Studio/plugins/Kotlin/kotlinc/bin/kotlinc-jvm"
"$KOTLINC" Main.kt -include-runtime -d main.jar && java -jar main.jar
```
The `guia5-corrutinas/` and `03-ejercicios-codigo/soluciones/EjercicioC.kt` exercises additionally use `kotlinx-coroutines-core` (not stdlib) — compile with `-cp <path-to-kotlinx-coroutines-core-jvm-*.jar>` and run with the same jar on `-cp` (already present in `~/.gradle/caches/modules-2/files-2.1/org.jetbrains.kotlinx/kotlinx-coroutines-core-jvm/` from the Android build's transitive deps — no network fetch needed). Delete the `.jar`/`.class` build output after verifying — it's gitignored, don't commit it.

## Architecture (`app/`)

- **UI**: 100% Jetpack Compose, no XML layouts. `MainActivity.kt` is the single entry point (`ComponentActivity` + `setContent { }`); new screens are added as `@Composable` functions, not new Activities, unless there's a specific reason to add one.
- **Theming**: `ui/theme/` (`Color.kt`, `Theme.kt`, `Type.kt`) — Material 3 theme wrapper (`AplicacionParaMovileTheme`), standard Android Studio Compose template.
- **Package**: `com.example.aplicacionparamovile` (applicationId matches namespace).
- **Versions**: pinned centrally in `gradle/libs.versions.toml` (version catalog) — always add/bump dependency versions there, not inline in `app/build.gradle.kts`.
- `compileSdk`/`targetSdk` = **37**, `minSdk` = 24. Keep `compileSdk` at or above whatever the AndroidX deps in `libs.versions.toml` require — if `./gradlew build` fails with "requires... compile against version X or later", bump `compileSdk`/`targetSdk` in `app/build.gradle.kts` to X (Gradle auto-downloads the SDK platform, same as it did for build-tools).

## Known environment gotcha

`./gradlew build` (not `assembleDebug`) can crash the Gradle daemon with `OutOfMemoryError` / `Gradle build daemon disappeared unexpectedly` if multiple daemons are running at once (Android Studio's own + one launched from the CLI) alongside other memory-heavy apps. Fix: `./gradlew --stop` first, then rerun. Prefer `assembleDebug` + `testDebugUnitTest` over the full `build` task when just verifying changes compile — it's lighter and skips lint/androidTest.

## Planned future work

Not implemented yet, noted in `README.md`: networking layer (Retrofit/Ktor + coroutines) to connect this app to a separate Spring Boot microservices backend, **[velourbe-platform](https://github.com/ethan1213/velourbe-platform)** (API Gateway on :8080 → BFF → 10 domain services, JWT auth). Don't assume any networking code exists yet.
