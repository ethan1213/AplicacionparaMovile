// Ejercicio A — Sistema de notas de estudiantes (Temas 2 y 3)
// SOLUCIÓN — intenta resolverlo tú antes de mirar esto.

fun main() {
    val estudiantes: List<Map<String, Any?>> = listOf(
        mapOf("nombre" to "Ana", "nota" to 6.5),
        mapOf("nombre" to "Beto", "nota" to 3.2),
        mapOf("nombre" to "Carla", "nota" to null),
        mapOf("nombre" to "Diego", "nota" to 4.0),
        mapOf("nombre" to "Elena", "nota" to null),
        mapOf("nombre" to "Fabián", "nota" to 5.8)
    )

    val promedio = promedioCurso(estudiantes)
    println("Promedio del curso (sin contar sin nota): $promedio")

    val aprobados = estudiantes.filter {
        val nota = it["nota"] as? Double
        nota != null && nota >= 4.0
    }
    val nombresAprobados = aprobados.map { it["nombre"] }
    println("Aprobados: $nombresAprobados")

    println("----------------------------------------")
    for (estudiante in estudiantes) {
        val nombre = estudiante["nombre"]
        val nota = estudiante["nota"] as? Double
        println("$nombre -> ${clasificar(nota)}")
    }
}

fun promedioCurso(estudiantes: List<Map<String, Any?>>): Double {
    var suma = 0.0
    var contador = 0
    for (estudiante in estudiantes) {
        val nota = estudiante["nota"] as? Double
        if (nota != null) {
            suma += nota
            contador++
        }
    }
    return if (contador == 0) 0.0 else suma / contador
}

fun clasificar(nota: Double?): String {
    return when {
        nota == null -> "Sin nota"
        nota >= 4.0 -> "Aprobado"
        else -> "Reprobado"
    }
}

/*
 * Reflexión: Map<String, Any?> (con el '?' en Any?) le dice a Kotlin que los
 * VALORES del mapa pueden ser null. Si usáramos Map<String, Any> a secas,
 * Kotlin no nos dejaría compilar `mapOf("nota" to null)` -- el compilador
 * directamente prohibiría representar estudiantes sin nota, que es justo
 * el escenario real que queremos modelar (evalúa lo importante: sin esa
 * nulabilidad explícita el programa no podría representar un dato ausente
 * de forma segura).
 */
