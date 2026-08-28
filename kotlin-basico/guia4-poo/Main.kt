/**
 * Guía 4: Aplicando POO y control de errores
 *
 * Sistema simple de gestión de personal: Persona (clase base) y Empleado
 * (hereda de Persona), aplicando herencia, encapsulamiento y polimorfismo.
 */

// `open` es OBLIGATORIO en Kotlin para permitir que otras clases hereden de esta.
// Por defecto, TODA clase en Kotlin es `final` (nadie puede heredar de ella) —
// justo al revés que Java, donde por defecto se puede heredar de cualquier clase
// salvo que la marques `final` a mano. Esto es una decisión de diseño de Kotlin
// para evitar herencias accidentales y bugs difíciles de rastrear.
open class Persona(val nombre: String, val edad: Int) {

    // También hay que marcar con `open` el método que quieras que las clases
    // hijas puedan sobrescribir (override). Si no le pones `open`, es como si
    // dijeras "esta implementación es definitiva, nadie la puede cambiar".
    open fun presentarse() {
        println("Hola, mi nombre es $nombre.")
    }
}

// `: Persona(nombre, edad)` es la sintaxis de herencia: Empleado ES-UN Persona,
// y le pasa nombre/edad al constructor de la clase padre.
class Empleado(
    nombre: String,
    edad: Int,
    val puesto: String,
    // ENCAPSULAMIENTO: `private` hace que salario solo pueda leerse/escribirse
    // desde DENTRO de esta misma clase. Nadie fuera de Empleado puede hacer
    // `empleado.salario` directamente -> protege datos sensibles y evita que
    // código externo los modifique de forma descontrolada.
    private val salario: Double
) : Persona(nombre, edad) {

    // Método público (no lleva "private") que expone información controlada
    // sobre el salario sin exponer el número directamente.
    fun mostrarPuesto() {
        println("$nombre trabaja como $puesto.")
    }

    // Un método público que SÍ usa el dato privado internamente está permitido:
    // el encapsulamiento protege el acceso desde AFUERA, no desde adentro.
    fun mostrarResumenCompleto() {
        println("$nombre ($puesto) gana $$salario mensuales.")
    }

    // POLIMORFISMO: `override` reemplaza la implementación heredada de Persona.
    // Cuando llames presentarse() sobre un Empleado, se ejecuta ESTA versión,
    // no la de Persona -- aunque la variable esté declarada como tipo Persona.
    override fun presentarse() {
        println("Hola, soy $nombre y mi puesto es $puesto.")
    }
}

fun main() {
    val persona = Persona("Ana", 30)
    val empleado = Empleado("Carlos", 28, "Desarrollador Android", 1_200_000.0)

    persona.presentarse()
    empleado.presentarse()
    empleado.mostrarPuesto()
    empleado.mostrarResumenCompleto()

    // ERROR TÍPICO: si descomentas la siguiente línea, no compila.
    // Kotlin te lo marca en rojo ANTES de correr el programa (a diferencia de
    // Java, donde el "private" también existe pero el error es el mismo tipo).
    // println(empleado.salario) // <- Cannot access 'salario': it is private in 'Empleado'

    println("----------------------------------------")

    // Demostración extra de POLIMORFISMO: una lista de tipo Persona puede
    // contener tanto objetos Persona como Empleado (porque Empleado ES-UN
    // Persona). Al llamar presentarse() sobre cada uno, Kotlin decide en
    // tiempo de ejecución cuál versión del método usar según el tipo REAL
    // del objeto, no según el tipo declarado de la lista.
    val personas: List<Persona> = listOf(persona, empleado)
    for (p in personas) {
        p.presentarse()
    }
}

/*
 * Pregunta de reflexión (Guía 4):
 * ¿Por qué la misma llamada a presentarse() da dos resultados distintos?
 *
 * Porque cada objeto en la lista `personas` es de un tipo REAL distinto
 * (Persona o Empleado), aunque la lista esté "tipada" como List<Persona>.
 * En tiempo de ejecución, Kotlin (como Java) usa el método de la clase REAL
 * del objeto, no el de la clase declarada -- eso es POLIMORFISMO: el mismo
 * mensaje (presentarse) produce comportamientos distintos según quién lo
 * reciba. Es la base de por qué en Android, por ejemplo, puedes tratar
 * distintos tipos de Views o de pantallas de forma uniforme y cada una
 * "sabe" cómo comportarse.
 */
