import java.io.File

fun main() {
    var matrix = mutableListOf<List<Int>>()
    File("input").forEachLine {
        matrix.add(it.toCharArray().map { Character.getNumericValue(it) })
    }

    var riskLevel = 0

    for (i in matrix.indices) {
        for (j in matrix[i].indices) {
            if (j > 0 && matrix[i][j] >= matrix[i][j-1]) {
                continue
            }
            if (j < matrix[i].count()-1 && matrix[i][j] >= matrix[i][j+1]) {
                continue
            }
            if (i > 0 && matrix[i][j] >= matrix[i-1][j]) {
                continue
            }
            if (i < matrix.count()-1 && matrix[i][j] >= matrix[i+1][j]) {
                continue
            }
            riskLevel += matrix[i][j] + 1
        }
    }

    println(riskLevel) // 550
}
