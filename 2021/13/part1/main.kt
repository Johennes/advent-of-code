import java.io.File
import kotlin.math.max

fun main() {
    var folds = mutableListOf<Pair<String,Int>>()
    var coordinates = mutableListOf<Pair<Int,Int>>()
    var xMax = Int.MIN_VALUE
    var yMax = Int.MIN_VALUE

    File("input").forEachLine {
        if (it.isEmpty()) {
            return@forEachLine
        }
        if (it.startsWith("fold along")) {
            val (axis, location) = Regex(".*(.)=(\\d+)").find(it)!!.destructured
            folds.add(Pair(axis, location.toInt()))
            return@forEachLine
        }
        val (x, y) = it.split(",").map { it.toInt() }
        xMax = max(xMax, x)
        yMax = max(yMax, y)
        coordinates.add(Pair(x, y))
    }

    var matrix = Array(xMax + 1) { Array(yMax + 1) { false } }
    coordinates.forEach { matrix[it.first][it.second] = true }

    var count = 0

    for (fold in folds.take(1)) {
        if (fold.first == "x") {
            for (x in 0..fold.second-1) {
                for (y in 0..yMax) {
                    if (matrix[x][y] || matrix[2 * fold.second - x][y]) {
                        ++count
                    }
                }
            }
        } else {
            for (x in 0..xMax) {
                for (y in 0..fold.second-1) {
                    if (matrix[x][y] || matrix[x][2 * fold.second - y]) {
                        ++count
                    }
                }
            }
        }
    }

    println(count) // 802
}
