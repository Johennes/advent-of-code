import java.io.File

data class Point(val x: Int, val y: Int)

class Image(var points: Set<Point>, private val originalSize: Int) {

    private var size = originalSize
    private var min = 0
    private var max = size - 1
    private var infinity = false

    fun apply(algorithm: List<Boolean>) {
        val newMin = -(size - originalSize) / 2 - 1
        val newMax = size - (size - originalSize) / 2
        var newPoints = mutableSetOf<Point>()

        for (x in newMin..newMax) {
            for (y in newMin..newMax) {
                if (enhance(Point(x, y), algorithm)) {
                    newPoints.add(Point(x, y))
                }
            }
        }

        points = newPoints
        size += 2
        min = newMin
        max = newMax
        infinity = if (infinity) algorithm.last() else algorithm.first()
    }

    private fun enhance(point: Point, algorithm: List<Boolean>): Boolean {
        var index = 0
        if (getValue(point.x - 1, point.y - 1)) {
            index += 1 shl 8
        }
        if (getValue(point.x, point.y - 1)) {
            index += 1 shl 7
        }
        if (getValue(point.x + 1, point.y - 1)) {
            index += 1 shl 6
        }
        if (getValue(point.x - 1, point.y)) {
            index += 1 shl 5
        }
        if (getValue(point.x, point.y)) {
            index += 1 shl 4
        }
        if (getValue(point.x + 1, point.y)) {
            index += 1 shl 3
        }
        if (getValue(point.x - 1, point.y + 1)) {
            index += 1 shl 2
        }
        if (getValue(point.x, point.y + 1)) {
            index += 1 shl 1
        }
        if (getValue(point.x + 1, point.y + 1)) {
            index += 1 shl 0
        }
        return algorithm[index]
    }

    private fun getValue(x: Int, y: Int): Boolean {
        if (x < min || x > max || y < min || y > max) {
            return infinity
        }
        return points.contains(Point(x, y))
    }

}

fun main() {
    val reader = File("input").bufferedReader()

    val algorithm = reader.readLine().map { it == '#' }
    reader.readLine()

    var points = mutableSetOf<Point>()
    var y = 0
    reader.forEachLine { line ->
        line.indices.filter { line[it] == '#' }.forEach { x -> points.add(Point(x, y)) }
        ++y
    }

    val image = Image(points, y)

    (0..49).forEach { image.apply(algorithm) }

    println(image.points.count()) // 14790
}
