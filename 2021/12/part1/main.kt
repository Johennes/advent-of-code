import java.io.File

private fun countUniquePaths(prefix: List<String>, connections: Map<String,List<String>>): Int {
    val destinations = prefix.last()?.let { connections[it] }?.filter { it.first()?.isUpperCase() == true || !prefix.contains(it) }
    if (destinations?.isEmpty() != false) {
        return 0
    }
    return destinations.fold(0) { sum, destination ->
        sum + if (destination == "end") 1 else countUniquePaths(prefix.plus(destination), connections)
    }
}

fun main() {
    var connections = hashMapOf<String,MutableList<String>>()

    File("input").forEachLine {
        val (left, right) = it.split("-")
        connections[left]?.let { it.add(right) } ?: connections.put(left, mutableListOf(right))
        connections[right]?.let { it.add(left) } ?: connections.put(right, mutableListOf(left))
    }

    println(countUniquePaths(listOf("start"), connections)) // 4495
}
