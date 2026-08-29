// Ejercicio C — Estado de un pedido (Tema 5)
// SOLUCIÓN — intenta resolverlo tú antes de mirar esto.
// Necesita kotlinx-coroutines-core (igual que guia5-corrutinas, ver kotlin-basico/README.md).

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking

data class Pedido(val id: Int, val producto: String)

sealed class EstadoPedido {
    object Pendiente : EstadoPedido()
    data class EnCamino(val minutosRestantes: Int) : EstadoPedido()
    object Entregado : EstadoPedido()
    data class Cancelado(val motivo: String) : EstadoPedido()
}

suspend fun consultarEstado(idPedido: Int): EstadoPedido {
    delay(1500L) // Simula la demora de red.
    return when (idPedido) {
        1 -> EstadoPedido.EnCamino(minutosRestantes = 15)
        2 -> EstadoPedido.Entregado
        3 -> EstadoPedido.Cancelado(motivo = "Local cerrado")
        else -> EstadoPedido.Pendiente
    }
}

fun main() = runBlocking {
    val pedidos = listOf(
        Pedido(1, "Pizza"),
        Pedido(2, "Sushi"),
        Pedido(3, "Hamburguesa"),
        Pedido(4, "Helado")
    )

    for (pedido in pedidos) {
        println("Consultando pedido #${pedido.id} (${pedido.producto})...")
        val estado = consultarEstado(pedido.id)
        mostrarEstado(pedido, estado)
        println("----------------------------------------")
    }
}

fun mostrarEstado(pedido: Pedido, estado: EstadoPedido) {
    when (estado) {
        is EstadoPedido.Pendiente ->
            println("Tu pedido de ${pedido.producto} todavía está pendiente.")

        is EstadoPedido.EnCamino ->
            estado.let {
                println("Tu ${pedido.producto} está en camino: llega en ${it.minutosRestantes} min.")
            }

        EstadoPedido.Entregado ->
            println("Tu ${pedido.producto} ya fue entregado. ¡Buen provecho!")

        is EstadoPedido.Cancelado ->
            println("Tu pedido de ${pedido.producto} fue cancelado. Motivo: ${estado.motivo}")
    }
}

/*
 * Reflexión: si agregas `object Reembolsado : EstadoPedido()` a la sealed
 * class y NO agregas su caso en el `when` de mostrarEstado(), el compilador
 * marca un ERROR en ese when ("'when' expression must be exhaustive, add
 * necessary 'is Reembolsado' branch") -- no un bug silencioso en producción.
 * Esa es la gran ventaja de sealed class + when frente a modelar el estado
 * con un String o un Int: el compilador te obliga a no olvidar ningún caso
 * nuevo en NINGÚN lugar donde estés manejando ese estado.
 */
