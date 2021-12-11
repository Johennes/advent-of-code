import java.io.File

fun bumpAndRecordFlash(matrix: List<MutableList<Int>>, i: Int, j: Int, flashed: MutableList<Pair<Int,Int>>, skipIfZero: Boolean) {
    if (i < 0 || i >= matrix.count() || j < 0 || j >= matrix[i].count() || skipIfZero && matrix[i][j] == 0) {
        return
    }
    matrix[i][j] = (matrix[i][j] + 1) % 10
    if (matrix[i][j] == 0) {
        flashed.add(Pair(i, j))
    }
}

fun main() {
    var matrix = mutableListOf<MutableList<Int>>()
    File("input").forEachLine {
        matrix.add(it.toCharArray().map { Character.getNumericValue(it) }.toMutableList())
    }

    var count = 0

    (0..99).forEach {
        var flashed = mutableListOf<Pair<Int,Int>>()

        for (i in matrix.indices) {
            for (j in matrix[i].indices) {
                bumpAndRecordFlash(matrix, i, j, flashed, false)
            }
        }

        while (!flashed.isEmpty()) {
            val (i, j) = flashed.removeFirst()
            ++count
            for (k in -1..1) {
                for (l in -1..1) {
                    bumpAndRecordFlash(matrix, i+k, j+l, flashed, true)
                }
            }
        }
    }

    println(count) // 1773
}
