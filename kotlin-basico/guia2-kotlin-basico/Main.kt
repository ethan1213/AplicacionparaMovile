/**
 * Guía 2: Aplicando Kotlin Básico
 *
 * Cómo correr esto en IntelliJ IDEA:
 * 1. File > Open... y selecciona esta carpeta (kotlin-basico), o crea un proyecto
 *    Kotlin nuevo y pega este código en un archivo Main.kt.
 * 2. Click en el triángulo ▶️ verde que aparece al lado de `fun main()`.
 *
 * También se puede correr por terminal con el compilador que trae Android Studio:
 *   kotlinc Main.kt -include-runtime -d main.jar
 *   java -jar main.jar
 */

fun main() {
    // ------------------------------------------------------------------
    // PARTE 1: Variables y operadores aritméticos
    // ------------------------------------------------------------------
    // `val` = valor que NO cambia después de asignado (constante/inmutable).
    // `var` = variable que SÍ puede cambiar.
    // En Kotlin casi siempre se prefiere `val` salvo que de verdad necesites reasignar.
    // A diferencia de Java, no escribimos el tipo a mano (Int, Double...): Kotlin lo
    // infiere solo a partir del valor. Igual se puede escribir explícito: val a: Int = 10
    val numeroEntero: Int = 10
    val numeroDecimal: Double = 4.5

    val suma = numeroEntero + numeroDecimal
    val resta = numeroEntero - numeroDecimal
    val multiplicacion = numeroEntero * numeroDecimal
    val division = numeroEntero / numeroDecimal

    // Kotlin permite meter variables directo dentro de un String con "$variable"
    // (interpolación de strings) — en Java tocaría concatenar con +.
    println("La suma es: $suma")
    println("La resta es: $resta")
    println("La multiplicación es: $multiplicacion")
    println("La división es: $division")

    println("----------------------------------------")

    // ------------------------------------------------------------------
    // PARTE 2: Seguridad ante nulos (Null Safety)
    // ------------------------------------------------------------------
    // El `?` después del tipo (String?) le dice a Kotlin "esta variable PUEDE
    // llegar a valer null". Sin el `?`, el compilador NUNCA te deja asignarle null
    // a una variable — así se evita en tiempo de compilación (antes de correr el
    // programa) el clásico NullPointerException (NPE) que revienta apps Java.
    var texto: String? = "Hola Kotlin"

    // El operador `?.` (safe call / llamada segura) significa:
    // "si texto NO es null, ejecuta .length; si texto ES null, no revientes,
    // devuelve null directamente".
    println("Longitud del texto: ${texto?.length}")

    texto = null
    // Como texto ahora es null, `texto?.length` no lanza error: simplemente
    // el resultado completo de la expresión es null, y println imprime "null".
    println("Longitud del texto: ${texto?.length}")

    // Pregunta de reflexión (Guía 2): ¿cómo se haría el equivalente en Java?
    // En Java, String no tiene la noción de "nullable" en el propio tipo, así que
    // hay que verificar a mano ANTES de usar la variable, o el programa revienta
    // en tiempo de ejecución con NullPointerException:
    //
    //   String texto = "Hola";
    //   if (texto != null) {
    //       System.out.println("Longitud: " + texto.length());
    //   } else {
    //       System.out.println("Longitud: null");
    //   }
    //
    // (Java moderno también ofrece Optional<String>, pero sigue siendo algo que el
    // programador debe acordarse de usar; en Kotlin el compilador te OBLIGA a
    // manejarlo si declaraste el tipo como nullable con `?`.)

    println("----------------------------------------")

    // ------------------------------------------------------------------
    // PARTE 3: Lógica condicional con `when`
    // ------------------------------------------------------------------
    // `when` es el reemplazo de Kotlin para el `switch` de Java, pero más potente
    // (más adelante se puede usar con rangos, tipos, condiciones múltiples, etc.)
    val diaNumero = 3

    val nombreDia = when (diaNumero) {
        1 -> "Lunes"
        2 -> "Martes"
        3 -> "Miércoles"
        4 -> "Jueves"
        5 -> "Viernes"
        6 -> "Sábado"
        7 -> "Domingo"
        else -> "Número inválido (debe ser entre 1 y 7)"
    }
    println("El día $diaNumero corresponde a: $nombreDia")
}

/*
 * Preguntas de reflexión (Guía 2) — para responder con tus palabras:
 *
 * 1. ¿Qué diferencias y similitudes clave observaste entre Kotlin y Java?
 *    - Pista: sintaxis más corta, inferencia de tipos, null safety integrado al
 *      sistema de tipos, `when` vs `switch`, interpolación de strings con "$".
 *
 * 2. ¿Cómo podrías aplicar Null Safety y `when` en futuros proyectos móviles?
 *    - Pista: piensa en datos que vienen de una red/API (pueden no llegar =
 *      null) o de un formulario vacío, y en pantallas que cambian de estado
 *      (cargando / éxito / error) usando when.
 */
