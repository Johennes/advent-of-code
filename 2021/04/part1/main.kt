import java.io.File

fun computeScore(board: List<MutableList<String>>, n: String): Int {
    var sum = 0
    for (i in board.indices) {
        for (j in board[i].indices) {
            if (board[i][j].length == 0) {
                continue
            }
            sum += board[i][j].toInt()
        }
    }
    return sum * n.toInt()
}

fun main() {
    val reader = File("input").bufferedReader()
    val numbers = reader.readLine().split(',')

    var boards = mutableListOf<List<MutableList<String>>>()
    var board = mutableListOf<MutableList<String>>()

    while (true) {
        val line = reader.readLine()?.let { it.trim() }
        if (line == null) {
            break
        }

        if (line.length == 0) {
            continue
        }

        board.add(line.split("\\s+".toRegex()).toMutableList())

        if (board.count() == board[0].count()) {
            boards.add(board)
            board = mutableListOf<MutableList<String>>()
        }
    }

    for (n in numbers) {
        for (i in boards.indices) {
            for (j in boards[i].indices) {
                for (k in boards[i][j].indices) {
                    if (boards[i][j][k] == n) {
                        boards[i][j][k] = ""
                    }
                }
            }
        }

        for (i in boards.indices) {
            for (j in boards[i].indices) {
                for (k in boards[i][j].indices) {
                    if (boards[i][j][k].length != 0) {
                        break
                    }
                    if (k == boards[i][j].count() - 1) {
                        println(computeScore(boards[i], n))
                        return
                    }
                }
                for (k in boards[i][j].indices) {
                    if (boards[i][k][j].length != 0) {
                        break
                    }
                    if (k == boards[i][k].count() - 1) {
                        println(computeScore(boards[i], n))
                        return
                    }
                }
            }
        }
    }
}

// 12796
