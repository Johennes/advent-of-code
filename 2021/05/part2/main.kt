import java.io.File
import kotlin.math.abs

fun main() {
    var grid = Array(1000) { Array(1000) { 0 } }

    File("input").forEachLine {
        val match = Regex("(\\d+),(\\d+) -> (\\d+),(\\d+)").find(it)
        if (match == null) {
            return@forEachLine
        }

        val (x1Str, y1Str, x2Str, y2Str) = match.destructured
        val x1 = x1Str.toInt()
        val y1 = y1Str.toInt()
        val x2 = x2Str.toInt()
        val y2 = y2Str.toInt()

        if (x1 == x2) {
            for (y in if (y1 < y2) y1..y2 else y2..y1) {
                grid[x1][y] += 1
            }
        } else if (y1 == y2) {
            for (x in if (x1 < x2) x1..x2 else x2..x1) {
                grid[x][y1] += 1
            }
        } else {
            val xStep = if (x1 < x2) 1 else -1
            val yStep = if (y1 < y2) 1 else -1
            var x = x1
            while (if (xStep > 0) x <= x2 else x >= x2) {
                grid[x][y1 + yStep * abs(x - x1)] += 1
                x += xStep
            }
        }
    }

    val count = grid.fold(0) { sum, line ->
        line.fold(sum) { lineSum, el ->
            if (el > 1) lineSum + 1 else lineSum
        }
    }

    println(count) // 20012
}
