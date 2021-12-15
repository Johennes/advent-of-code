import java.io.File
import kotlin.math.min
import java.util.PriorityQueue

fun main() {
    var levels = mutableListOf<MutableList<Int>>()
    File("input").forEachLine {
        levels.add(it.toCharArray().map { Character.getNumericValue(it) }.toMutableList())
    }

    val size = levels.count()

    var tentativeRisks = Array(size) { Array(size) { Int.MAX_VALUE } }
    tentativeRisks[0][0] = 0

    var visited = Array(size) { Array(size) { false } }

    var unvisited = PriorityQueue<Pair<Int,Int>>(compareBy { tentativeRisks[it.first][it.second] })
    unvisited.add(Pair(0,0))

    while (!visited[size-1][size-1]) {
        val (i, j) = unvisited.remove()

        if (visited[i][j]) {
            continue // Ignore dupes added because PriorityQueue doesn't allow updating
        }

        if (i > 0) {
            tentativeRisks[i-1][j] = min(tentativeRisks[i-1][j], tentativeRisks[i][j] + levels[i-1][j])
            unvisited.add(Pair(i-1,j))
        }
        if (i < size - 1) {
            tentativeRisks[i+1][j] = min(tentativeRisks[i+1][j], tentativeRisks[i][j] + levels[i+1][j])
            unvisited.add(Pair(i+1,j))
        }
        if (j > 0) {
            tentativeRisks[i][j-1] = min(tentativeRisks[i][j-1], tentativeRisks[i][j] + levels[i][j-1])
            unvisited.add(Pair(i,j-1))
        }
        if (j < size - 1) {
            tentativeRisks[i][j+1] = min(tentativeRisks[i][j+1], tentativeRisks[i][j] + levels[i][j+1])
            unvisited.add(Pair(i,j+1))
        }

        visited[i][j] = true
    }

    println(tentativeRisks[size-1][size-1]) // 403
}
