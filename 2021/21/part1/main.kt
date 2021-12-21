import java.io.File
import kotlin.math.min

class Dice {
    private var state = 0
    var rolls = 0

    fun roll(): Int {
        state = (state % 100) + 1
        rolls += 1
        return state
    }
}

fun main() {
    var positions = File("input").readLines().map { Integer.parseInt(it.split(": ").last()) }.toMutableList()
    var scores = positions.map { 0 }.toMutableList()

    val dice = Dice()

    loop@ while (true) {
        for (player in positions.indices) {
            positions[player] += (dice.roll() + dice.roll() + dice.roll()) % 10
            if (positions[player] > 10) {
                positions[player] = positions[player] % 10
            }

            scores[player] += positions[player]
            if (scores[player] >= 1000) {
                break@loop
            }
        }
    }

    println(dice.rolls * scores.reduce { minimum, next -> min(minimum, next) }) // 742257
}
