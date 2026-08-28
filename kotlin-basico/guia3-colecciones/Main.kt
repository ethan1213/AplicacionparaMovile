/**
 * Guía 3: Aplicando Colecciones
 *
 * Simula un inventario de productos usando Map<String, Any> (cada producto es un
 * mapa clave->valor) guardados dentro de una List inmutable.
 */

fun main() {
    // ------------------------------------------------------------------
    // Datos de prueba
    // ------------------------------------------------------------------
    // mapOf(...) crea un Map inmutable. Las claves son String, los valores son
    // de distintos tipos (String, Double) -> por eso el mapa es Map<String, Any>.
    // "Any" en Kotlin es como "Object" en Java: el tipo padre de todos los tipos.
    val producto1 = mapOf("nombre" to "Laptop Gamer", "precio" to 1250.50, "categoria" to "Tecnologia")
    val producto2 = mapOf("nombre" to "Libro de Kotlin", "precio" to 45.99, "categoria" to "Libros")
    val producto3 = mapOf("nombre" to "Mouse Inalambrico", "precio" to 25.00, "categoria" to "Tecnologia")
    val producto4 = mapOf("nombre" to "Cuaderno", "precio" to 3.50, "categoria" to "Oficina")
    val producto5 = mapOf("nombre" to "Monitor 24 pulgadas", "precio" to 189.99, "categoria" to "Tecnologia")
    val producto6 = mapOf("nombre" to "Lapiz Grafito", "precio" to 1.20, "categoria" to "Oficina")

    // listOf(...) crea una List inmutable (no se puede agregar/quitar elementos
    // después). Si necesitaras una lista modificable usarías mutableListOf(...).
    val inventario = listOf(producto1, producto2, producto3, producto4, producto5, producto6)

    println("Inventario cargado con ${inventario.size} productos.")
    println("----------------------------------------")

    // ------------------------------------------------------------------
    // Función de búsqueda con ciclo for
    // ------------------------------------------------------------------
    val encontrado = buscarProducto("Mouse Inalambrico", inventario)
    println("Búsqueda 'Mouse Inalambrico': $encontrado")

    val noEncontrado = buscarProducto("Teclado", inventario)
    println("Búsqueda 'Teclado': $noEncontrado")

    println("----------------------------------------")

    // ------------------------------------------------------------------
    // Función de cálculo con ciclo
    // ------------------------------------------------------------------
    val promedio = precioPromedio(inventario)
    println("Precio promedio del inventario: $promedio")

    println("----------------------------------------")

    // ------------------------------------------------------------------
    // Operaciones funcionales: filter y map
    // ------------------------------------------------------------------
    // filter: recorre la lista y se queda SOLO con los elementos que cumplen la
    // condición (la lambda { ... } debe devolver true/false). "it" es el nombre
    // implícito del elemento actual cuando la lambda tiene un solo parámetro.
    val productosTecnologia = inventario.filter { producto -> producto["categoria"] == "Tecnologia" }
    println("Productos de categoría Tecnologia (${productosTecnologia.size}):")
    productosTecnologia.forEach { println("  - ${it["nombre"]}") }

    // map: recorre la lista y TRANSFORMA cada elemento en otra cosa (aquí, de
    // Map<String, Any> a solo el nombre en String). El resultado tiene el mismo
    // tamaño que la lista original, pero elementos distintos.
    val nombresProductos = inventario.map { it["nombre"] }
    println("Nombres de todos los productos: $nombresProductos")
}

/**
 * Busca un producto por nombre dentro del inventario usando un ciclo for clásico.
 * Devuelve el mapa del producto si lo encuentra, o null si no existe.
 */
fun buscarProducto(nombre: String, inventario: List<Map<String, Any>>): Map<String, Any>? {
    for (producto in inventario) {
        if (producto["nombre"] == nombre) {
            return producto
        }
    }
    return null
}

/**
 * Calcula el precio promedio de todos los productos del inventario.
 */
fun precioPromedio(inventario: List<Map<String, Any>>): Double {
    var suma = 0.0
    for (producto in inventario) {
        // producto["precio"] devuelve tipo Any? (puede no existir la clave -> null).
        // `as? Double` es un "cast seguro": si el valor SÍ es Double, lo convierte;
        // si NO lo es (o es null), devuelve null en vez de reventar el programa.
        // El `?: 0.0` de al lado es el operador Elvis: "si lo de la izquierda es
        // null, usa este valor por defecto".
        //
        // ERROR TÍPICO a evitar: usar `as Double` (cast forzado, sin el '?') acá
        // reventaría con un ClassCastException/NPE si el mapa no trajera "precio"
        // o trajera otro tipo. SIEMPRE usa `as?` cuando no estés 100% seguro del
        // tipo real del dato (por ejemplo, datos que vienen de una API externa).
        val precio = (producto["precio"] as? Double) ?: 0.0
        suma += precio
    }
    return suma / inventario.size
}
