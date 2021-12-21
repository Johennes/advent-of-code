import java.io.File
import kotlin.math.abs
import kotlin.math.max

fun main() {
    val (start1, start2) = File("input").readLines().map { Integer.parseInt(it.split(": ").last()) }

    // Compute ways X to roll a certain sum N with K dice rolls: waysToUseKDiceToRollN[K][N] = X
    var waysToUseKDiceToRollN = Array(4) { mutableMapOf<Int,Int>() }
    for (k in 1 until waysToUseKDiceToRollN.size) {
        for (n in k..3*k) {
            if (k == 1) {
                waysToUseKDiceToRollN[k][n] = 1
            } else {
                waysToUseKDiceToRollN[k][n] = (n-3..n-1).filter { it > 0 }.fold(0) { sum, remainder ->
                    sum + (waysToUseKDiceToRollN[k - 1][remainder] ?: 0)
                }
            }
        }
    }

    // Compute ways X to go from field K to field N with 3 dice rolls: waysToGoFromFieldKToFieldN[K][N] = X
    var waysToGoFromFieldKToFieldN = Array(11) { mutableMapOf<Int,Int>() }
    for (k in 1..10) {
        for (step in 3..9) {
            val n = if (k + step > 10) (k + step) % 10 else k + step
            waysToGoFromFieldKToFieldN[k][n] = waysToUseKDiceToRollN[3][step] ?: 0
        }
    }

    // Before any rolls...

    // Count ways X to reach field N with a total score of S for player 1: waysToReachFieldNWithScoreS1[N][S] = X
    var waysToReachFieldNWithScoreS1 = Array(11) { mutableMapOf<Int,Long>() }
    waysToReachFieldNWithScoreS1[start1][0] = 1

    // Count ways X to reach field N with a total score of S for player 2: waysToReachFieldNWithScoreS2[N][S] = X
    var waysToReachFieldNWithScoreS2 = Array(11) { mutableMapOf<Int,Long>() }
    waysToReachFieldNWithScoreS2[start2][0] = 1

    // Count ways remaining that haven't won yet for each player
    var waysRemaining1 = 1L
    var waysRemaining2 = 1L

    // Let's roll the dice...

    // Count number of wins for each player
    var wins1 = 0L
    var wins2 = 0L

    // Roll the dice as long as there are ways left for either player to win
    while (waysRemaining1 > 0 || waysRemaining2 > 0) {
        var newWaysToReachFieldNWithScoreS1 = Array(11) { mutableMapOf<Int, Long>() }
        var newWaysRemaining1 = 0L

        for (start in waysToReachFieldNWithScoreS1.indices) {
            for ((score, scoreCount) in waysToReachFieldNWithScoreS1[start]) {
                for ((next, nextCount) in waysToGoFromFieldKToFieldN[start]) {
                    if (score + next >= 21) {
                        // Player 1 wins immediately. Multiply ways for player 1 to get here by
                        // remaining ways for player 2 _before_ their turn.
                        wins1 += scoreCount * nextCount * waysRemaining2
                    } else {
                        newWaysToReachFieldNWithScoreS1[next][score + next] = (newWaysToReachFieldNWithScoreS1[next][score + next] ?: 0) + scoreCount * nextCount
                        newWaysRemaining1 += scoreCount * nextCount
                    }
                }
            }
        }

        var newWaysToReachFieldNWithScoreS2 = Array(11) { mutableMapOf<Int, Long>() }
        var newWaysRemaining2 = 0L

        for (start in waysToReachFieldNWithScoreS2.indices) {
            for ((score, scoreCount) in waysToReachFieldNWithScoreS2[start]) {
                for ((next, nextCount) in waysToGoFromFieldKToFieldN[start]) {
                    if (score + next >= 21) {
                        // Player 2 wins. Multiply ways for player 2 to get here by remaining ways
                        // for player 1 _after_ their turn.
                        wins2 += scoreCount * nextCount * newWaysRemaining1
                    } else {
                        newWaysToReachFieldNWithScoreS2[next][score + next] = (newWaysToReachFieldNWithScoreS2[next][score + next] ?: 0) + scoreCount * nextCount
                        newWaysRemaining2 += scoreCount * nextCount
                    }
                }
            }
        }

        waysToReachFieldNWithScoreS1 = newWaysToReachFieldNWithScoreS1
        waysToReachFieldNWithScoreS2 = newWaysToReachFieldNWithScoreS2

        waysRemaining1 = newWaysRemaining1
        waysRemaining2 = newWaysRemaining2
    }

    println(max(wins1, wins2)) // 93726416205179
}
