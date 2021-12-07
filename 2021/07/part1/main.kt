import java.io.File
import kotlin.math.abs

fun main() {
    val crabs = File("input").bufferedReader().readLine().split(",").map { it.toInt() }.sorted()
    val pos = crabs[(crabs.size - 1) / 2]
    val fuel = crabs.fold(0) { sum, crab -> sum + abs(pos - crab)}
}
