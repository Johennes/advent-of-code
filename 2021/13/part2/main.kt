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

    for (fold in folds) {
        if (fold.first == "x") {
            xMax = fold.second - 1
            for (x in 0..xMax) {
                for (y in 0..yMax) {
                    matrix[x][y] = matrix[x][y] || matrix[2 * (xMax + 1) - x][y]
                }
            }
        } else {
            yMax = fold.second - 1
            for (x in 0..xMax) {
                for (y in 0..yMax) {
                    matrix[x][y] = matrix[x][y] || matrix[x][2 * (yMax + 1) - y]
                }
            }
        }
    }

    for (y in 0..yMax) {
        for (x in 0..xMax) {
            print(if (matrix[x][y]) "#" else ".")
        }
        print("\n")
    }

    // RKHFZGUB

    // ###..#..#.#..#.####.####..##..#..#.###..
    // #..#.#.#..#..#.#.......#.#..#.#..#.#..#.
    // #..#.##...####.###....#..#....#..#.###..
    // ###..#.#..#..#.#.....#...#.##.#..#.#..#.
    // #.#..#.#..#..#.#....#....#..#.#..#.#..#.
    // #..#.#..#.#..#.#....####..###..##..###..
}
