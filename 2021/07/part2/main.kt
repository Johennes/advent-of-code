import java.io.File
import kotlin.math.abs

fun main() {
    val crabs = File("input").bufferedReader().readLine().split(",").map { it.toInt() }.toList()

    val min = crabs.fold(Int.MAX_VALUE) { min, n -> if (n < min) n else min }
    val max = crabs.fold(Int.MIN_VALUE) { max, n -> if (n > max) n else max }

    var lowestFuel = Int.MAX_VALUE
    var lowestPos = Int.MAX_VALUE

    for (pos in min..max) {
        val fuel = crabs.fold(0) { sum, crab ->
            val n = abs(pos - crab)
            sum + (n * n + n) / 2
        }
        if (fuel < lowestFuel) {
            lowestFuel = fuel
            lowestPos = pos
        }
    }

    println(lowestFuel) // 94862124
    println(lowestPos) // 482
}
