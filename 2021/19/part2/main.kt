import java.io.File
import kotlin.math.abs
import kotlin.math.max

fun main() {
    val pointsByScanner = readInput()

    val positionsByScanner = MutableList<Vector?>(pointsByScanner.count()) { null }
    positionsByScanner[0] = Vector(0, 0, 0)

    val locatedScanners = mutableSetOf(0)
    val scannersToLocate = (1 until pointsByScanner.count()).toMutableSet()

    while (scannersToLocate.isNotEmpty()) {
        val scanner1 = locatedScanners.first().also { locatedScanners.remove(it) }
        val origin1 = positionsByScanner[scanner1]!!

        for (scanner2 in scannersToLocate) {
            point1@ for (point1 in pointsByScanner[scanner1]) {
                val vectors1 = createVectorSet(point1, pointsByScanner[scanner1])

                for (transform in transforms) {
                    for (point2 in pointsByScanner[scanner2]) {
                        val vectors2 = createVectorSet(point2, pointsByScanner[scanner2], transform)

                        if (vectors1.intersect(vectors2).count() < 11) {
                            continue
                        }

                        positionsByScanner[scanner2] = origin1.plus(point1.minus(transform(point2)))
                        locatedScanners.add(scanner2)

                        // Align scanner2's coordinate system to be able to use it as scanner1 in a later iteration
                        for (i in pointsByScanner[scanner2].indices) {
                            pointsByScanner[scanner2][i] = transform(pointsByScanner[scanner2][i])
                        }

                        break@point1
                    }
                }
            }
        }

        scannersToLocate.removeAll(locatedScanners)
    }

    var maxDistance = Int.MIN_VALUE

    for (i in 0 until positionsByScanner.count()) {
        for (j in i + 1 until pointsByScanner.count()) {
            maxDistance = max(maxDistance, positionsByScanner[i]!!.manhattanDistance(positionsByScanner[j]!!))
        }
    }

    println(maxDistance) // 13225
}

val transforms = arrayOf<(Vector) -> Vector>(
        { v -> Vector(v.x, v.y, v.z) },
        { v -> Vector(v.x, -v.y, -v.z) },
        { v -> Vector(v.x, v.z, -v.y) },
        { v -> Vector(v.x, -v.z, v.y) },
        { v -> Vector(-v.x, v.y, -v.z) },
        { v -> Vector(-v.x, -v.y, v.z) },
        { v -> Vector(-v.x, v.z, v.y) },
        { v -> Vector(-v.x, -v.z, -v.y) },
        { v -> Vector(v.y, -v.x, v.z) },
        { v -> Vector(v.y, v.x, -v.z) },
        { v -> Vector(v.y, v.z, v.x) },
        { v -> Vector(v.y, -v.z, -v.x) },
        { v -> Vector(-v.y, v.x, v.z) },
        { v -> Vector(-v.y, -v.x, -v.z) },
        { v -> Vector(-v.y, -v.z, v.x) },
        { v -> Vector(-v.y, v.z, -v.x) },
        { v -> Vector(v.z, v.y, -v.x) },
        { v -> Vector(v.z, -v.y, v.x) },
        { v -> Vector(v.z, v.x, v.y) },
        { v -> Vector(v.z, -v.x, -v.y) },
        { v -> Vector(-v.z, v.y, v.x) },
        { v -> Vector(-v.z, -v.y, -v.x) },
        { v -> Vector(-v.z, v.x, -v.y) },
        { v -> Vector(-v.z, -v.x, v.y) }
)

data class Vector(val x: Int, val y: Int, val z: Int) {
    fun minus(other: Vector): Vector {
        return Vector(x - other.x, y - other.y, z - other.z)
    }

    fun plus(other: Vector): Vector {
        return Vector(x + other.x, y + other.y, z + other.z)
    }

    fun manhattanDistance(other: Vector): Int {
        return abs(x - other.x) + abs(y - other.y) + abs(z - other.z)
    }
}

fun readInput(): MutableList<MutableList<Vector>> {
    var result = mutableListOf<MutableList<Vector>>()

    File("input").forEachLine { line ->
        if (line.trim().isEmpty()) {
            return@forEachLine
        }
        if (line.startsWith("---")) {
            result.add(mutableListOf())
            return@forEachLine
        }
        val (x, y, z) = line.split(",").map { Integer.parseInt(it) }
        result.last()?.let { it.add(Vector(x, y, z)) }
    }

    return result
}

fun createVectorSet(origin: Vector, points: List<Vector>, transform: ((Vector) -> Vector)? = null): Set<Vector> {
    var result = mutableSetOf<Vector>()
    for (point in points) {
        if (point != origin) {
            val difference = point.minus(origin)
            result.add(transform?.let { it(difference) } ?: difference)
        }
    }
    return result
}
