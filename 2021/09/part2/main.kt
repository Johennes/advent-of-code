import java.io.File

fun main() {
    var matrix = mutableListOf<MutableList<Int>>()
    File("input").forEachLine {
        matrix.add(it.toCharArray().map { Character.getNumericValue(it) }.toMutableList())
    }

    var basins = mutableListOf<Int>()

    for (i in matrix.indices) {
        for (j in matrix[i].indices) {
            if (matrix[i][j] == 9) {
                continue
            }

            var basin = 0
            var unvisited = hashSetOf(listOf(i, j))

            while (!unvisited.isEmpty()) {
                val (k, l) = unvisited.first().also { unvisited.remove(it) }

                matrix[k][l] = 9
                ++basin

                if (l > 0 && matrix[k][l-1] != 9) {
                    unvisited.add(listOf(k, l-1))
                }
                if (l < matrix[k].count()-1 && matrix[k][l+1] != 9) {
                    unvisited.add(listOf(k, l+1))
                }
                if (k > 0 && matrix[k-1][l] != 9) {
                    unvisited.add(listOf(k-1, l))
                }
                if (k < matrix.count()-1 && matrix[k+1][l] != 9) {
                    unvisited.add(listOf(k+1, l))
                }
            }

            basins.add(basin)
        }
    }

    println(basins.sorted().takeLast(3).reduce { sum, next -> sum * next}) // 1100682
}
