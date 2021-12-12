import java.io.File

private fun countUniquePaths(prefix: List<String>, connections: Map<String,List<String>>, didVisitSmallCaveTwice: Boolean): Int {
    val destinations = prefix.last()?.let { connections[it] }?.filter { destination ->
        // Can never return to start
        if (destination == "start") {
            return@filter false
        }
        // Can visit large caves any number of time
        if (destination.first()?.isUpperCase() == true) {
            return@filter true
        }
        // Can visit a single small cave twice, all others only once
        if (didVisitSmallCaveTwice) {
            return@filter !prefix.contains(destination)
        }
        return@filter prefix.count { it == destination } < 2
    }
    if (destinations?.isEmpty() != false) {
        return 0 // Nowhere to go
    }
    return destinations.fold(0) { sum, destination ->
        if (destination == "end") {
            return@fold sum + 1
        }
        val didVisitTwice = didVisitSmallCaveTwice || destination.first()?.isUpperCase() == false && prefix.count { it == destination } == 1
        return@fold sum + countUniquePaths(prefix.plus(destination), connections, didVisitTwice)
    }
}

fun main() {
    var connections = hashMapOf<String,MutableList<String>>()

    File("input").forEachLine {
        val (left, right) = it.split("-")
        connections[left]?.let { it.add(right) } ?: connections.put(left, mutableListOf(right))
        connections[right]?.let { it.add(left) } ?: connections.put(right, mutableListOf(left))
    }

    println(countUniquePaths(listOf("start"), connections, false)) // 131254
}
