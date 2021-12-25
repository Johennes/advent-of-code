import java.io.File

data class Move(val fromX: Int, val fromY: Int, val toX: Int, val toY: Int)

const val RIGHT_MOVER = '>'
const val DOWN_MOVER = 'v'
const val EMPTY_SPACE = '.'

fun main() {
    val map = File("input").readLines().map { line -> line.map { it }.toMutableList() }

    val sizeY = map.count()
    val sizeX = map[0].count()

    var steps = 0

    while (true) {
        steps += 1

        val moves = mutableListOf<Move>()

        for (y in 0 until sizeY) {
            for (x in 0 until sizeX) {
                if (map[y][x] != EMPTY_SPACE) {
                    continue
                }

                val xLeft = (x - 1 + sizeX) % sizeX
                val yTop = (y - 1 + sizeY) % sizeY

                if (map[y][xLeft] == RIGHT_MOVER) {
                    moves.add(Move(xLeft, y, x, y))
                    if (map[yTop][xLeft] == DOWN_MOVER) {
                        moves.add(Move(xLeft, yTop, xLeft, y))
                    }
                } else if (map[yTop][x] == DOWN_MOVER) {
                    moves.add(Move(x, yTop, x, y))
                }
            }
        }

        if (moves.isEmpty()) {
            break
        }

        for (move in moves) {
            map[move.toY][move.toX] = map[move.fromY][move.fromX] // Save because moves are ordered left to right and top to bottom
            map[move.fromY][move.fromX] = EMPTY_SPACE
        }
    }

    print(steps) // 582
}
