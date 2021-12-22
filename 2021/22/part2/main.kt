import java.io.File
import kotlin.math.max
import kotlin.math.min

data class Cuboid(val on: Boolean, val xMin: Int, val xMax: Int, val yMin: Int, val yMax: Int, val zMin: Int, val zMax: Int) {
    private fun intersect(other: Cuboid): Cuboid? {
        if (xMax < other.xMin || xMin > other.xMax || yMax < other.yMin || yMin > other.yMax || zMax < other.zMin || zMin > other.zMax) {
            return null
        }
        return Cuboid(on, max(xMin, other.xMin), min(xMax, other.xMax), max(yMin, other.yMin), min(yMax, other.yMax), max(zMin, other.zMin), min(zMax, other.zMax))
    }

    fun minus(other: Cuboid): List<Cuboid> {
        return intersect(other)?.let { intersection ->
            var result = mutableListOf<Cuboid>()
            if (intersection.xMin > xMin) {
                result.add(Cuboid(on, xMin, intersection.xMin - 1, yMin, yMax, zMin, zMax))
            }
            if (intersection.xMax < xMax) {
                result.add(Cuboid(on, intersection.xMax + 1, xMax, yMin, yMax, zMin, zMax))
            }
            if (intersection.yMin > yMin) {
                result.add(Cuboid(on, intersection.xMin, intersection.xMax, yMin, intersection.yMin - 1, zMin, zMax))
            }
            if (intersection.yMax < yMax) {
                result.add(Cuboid(on, intersection.xMin, intersection.xMax, intersection.yMax + 1, yMax, zMin, zMax))
            }
            if (intersection.zMin > zMin) {
                result.add(Cuboid(on, intersection.xMin, intersection.xMax, intersection.yMin, intersection.yMax, zMin, intersection.zMin - 1))
            }
            if (intersection.zMax < zMax) {
                result.add(Cuboid(on, intersection.xMin, intersection.xMax, intersection.yMin, intersection.yMax, intersection.zMax + 1, zMax))
            }
            return result
        } ?: listOf(this)
    }

    fun count(): Long {
        return (xMax - xMin + 1).toLong() * (yMax - yMin + 1L) * (zMax - zMin + 1L)
    }
}

fun main() {
    val regex = Regex("(on|off) x=(-?\\d+)..(-?\\d+),y=(-?\\d+)..(-?\\d+),z=(-?\\d+)..(-?\\d+)")
    var cuboids = mutableSetOf<Cuboid>()

    File("input").forEachLine { line ->
        regex.find(line)?.let { match ->
            val (state, xMin, xMax, yMin, yMax, zMin, zMax) = match.destructured
            val cuboid = Cuboid(state == "on", xMin.toInt(), xMax.toInt(), yMin.toInt(), yMax.toInt(), zMin.toInt(), zMax.toInt())

            var newCuboids = mutableSetOf<Cuboid>()

            for (existing in cuboids) {
                newCuboids.addAll(existing.minus(cuboid))
            }

            if (cuboid.on) {
                newCuboids.add(cuboid)
            }

            cuboids = newCuboids
        }
    }

    val count = cuboids.fold(0L) { sum, cuboid -> if (cuboid.on) sum + cuboid.count() else sum }

    println(count) // 1211172281877240
}
