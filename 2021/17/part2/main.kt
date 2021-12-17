import java.io.File
import kotlin.math.floor
import kotlin.math.min
import kotlin.math.max
import kotlin.math.sqrt

fun main() {
    val line = File("input").bufferedReader().readLine()
    val regex = Regex("x=(-?\\d+)\\.\\.(-?\\d+),\\s*y=(-?\\d+)\\.\\.(-?\\d+)")
    val (targetX1, targetX2, targetY1, targetY2) = regex.find(line)!!.groupValues.takeLast(4).map { it.toInt() }

    if (targetX1 <= 0 || targetX2 <= 0 || targetX2 <= targetX1 || targetY1 >= 0 || targetY2 >= 0 || targetY2 <= targetY1) {
        throw Exception("Assumptions about input data not met, cannot execute algorithm")
    }

    var velocities = mutableSetOf<Pair<Int,Int>>()

    // If vx is non-positive, we'll never reach the target area
    val vxMin = 1

    // If vx is greater than the target end, the first step will overshoot
    val vxMax = targetX2

    // If vy is lower than the (negative) target end, the first step will overshoot
    val vyMin = targetY1

    // If vy is greater than the (positive) target end, the first step after crossing y=0 will overshoot
    val vyMax = -targetY1

    for (vx in vxMin..vxMax) {
        // The maximum reachable x value is the sum of all positive integers up to vx
        val xMax = (vx * vx + vx) / 2

        if (xMax < targetX1) {
            continue // Will never reach target
        }

        // The x position won't change anymore after vx steps
        val txMax = vx

        var tx1 = Int.MIN_VALUE // Smallest time at which the probe is inside the horizontal target area
        var tx2 = Int.MAX_VALUE // Largest time at which the probe is inside the horizontal target area

        for (t in 1..txMax) {
            val x = t * vx - (t * t - t) / 2

            if (x > targetX2) {
                break // We overshot the target
            }

            if (x >= targetX1 && x <= targetX2) {
                if (tx1 == Int.MIN_VALUE) {
                    tx1 = t
                }
                if (xMax > targetX2) {
                    tx2 = t
                }
            }
        }

        if (tx1 == Int.MIN_VALUE) {
            continue // Never enters target area horizontally
        }

        for (vy in vyMin..vyMax) {
            for (t in tx1..tx2) {
                val y = t * vy - (t * t - t) / 2

                if (y < targetY1) {
                    break // We overshot the target
                }

                if (y >= targetY1 && y <= targetY2) {
                    velocities.add(Pair(vx, vy))
                    break
                }
            }
        }

    }

    println(velocities.count()) // 1908
}
