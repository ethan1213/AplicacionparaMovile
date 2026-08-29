// Ejercicio B — Sistema de vehículos (Tema 4)
// SOLUCIÓN — intenta resolverlo tú antes de mirar esto.

open class Vehiculo(val marca: String, val modelo: String) {

    // Encapsulamiento: private + var mutable solo desde dentro de la clase.
    private var kilometraje: Int = 0

    open fun describir() {
        println("$marca $modelo")
    }

    fun avanzar(km: Int) {
        if (km < 0) {
            println("Error: no se puede avanzar una cantidad negativa de km.")
            return
        }
        kilometraje += km
    }

    fun verKilometraje() {
        println("$marca $modelo tiene $kilometraje km.")
    }
}

class Auto(marca: String, modelo: String, private val numeroPuertas: Int) : Vehiculo(marca, modelo) {
    override fun describir() {
        println("$marca $modelo ($numeroPuertas puertas)")
    }
}

class Moto(marca: String, modelo: String, private val cilindrada: Int) : Vehiculo(marca, modelo) {
    override fun describir() {
        println("$marca $modelo (${cilindrada}cc)")
    }
}

fun main() {
    val auto = Auto("Toyota", "Corolla", 4)
    val moto = Moto("Honda", "CB500", 500)

    auto.avanzar(150)
    moto.avanzar(80)
    moto.avanzar(-20) // Prueba de validación: no debe restar ni romper nada.

    val vehiculos: List<Vehiculo> = listOf(auto, moto)
    for (v in vehiculos) {
        v.describir()       // Polimorfismo: cada uno imprime su versión.
        v.verKilometraje()
    }
}

/*
 * Reflexión: si kilometraje fuera "public var", cualquier parte del código
 * podría hacer vehiculo.kilometraje = -500 o vehiculo.kilometraje = 999999999
 * directamente, saltándose la validación de avanzar() (que impide negativos).
 * El encapsulamiento fuerza a que la ÚNICA forma de cambiar kilometraje sea
 * pasando por avanzar(), que sí controla que el dato quede siempre válido --
 * eso protege la integridad del objeto sin importar quién lo use.
 */
