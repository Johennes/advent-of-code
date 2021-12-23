import java.io.File
import kotlin.math.abs
import kotlin.math.min

fun main() {
    var hallway = mutableListOf<Char?>()
    var rooms = mutableListOf<MutableList<Char?>>()
    var entrances = mutableListOf<Int>()

    val reader = File("input").bufferedReader()

    // Skip initial wall
    reader.readLine()

    // Parse hallway
    val hallwayLine = reader.readLine()
    val trimmedHallwayLine = hallwayLine.trim('#')
    val padding = (hallwayLine.length - trimmedHallwayLine.length) / 2
    hallway.addAll(trimmedHallwayLine.map { null })

    // Parse rooms
    reader.forEachLine { line ->
        val trimmedLine = line.trim(' ', '#')
        if (trimmedLine.isEmpty()) {
            return@forEachLine // Skip final wall
        }

        // If not yet done, determine entrance indices
        if (entrances.isEmpty()) {
            for (i in line.indices) {
                if (line[i] != ' ' && line[i] != '#') {
                    entrances.add(i - padding)
                    rooms.add(mutableListOf())
                }
            }
        }

        // Store pods into rooms
        var roomIndex = 0
        for (pod in trimmedLine.split('#')) {
            rooms[roomIndex].add(pod.first())
            roomIndex += 1
        }
    }

    // Static mappings
    val expectedPods = listOf('A', 'B', 'C', 'D')
    val roomIndexByPod = mapOf('A' to 0, 'B' to 1, 'C' to 2, 'D' to 3)
    val energyByPod = mapOf('A' to 1, 'B' to 10, 'C' to 100, 'D' to 1000)

    var minEnergy = Int.MAX_VALUE

    fun isDone(): Boolean {
        for (i in rooms.indices) {
            for (j in rooms[i].indices) {
                if (rooms[i][j] != expectedPods[i]) {
                    return false
                }
            }
        }
        return true
    }

    fun isRoomReadyToTakePods(roomIndex: Int): Boolean {
        return rooms[roomIndex].fold(true) { result, next -> result && (next == null || next == expectedPods[roomIndex]) }
    }

    fun isHallwayEmpty(start: Int, end: Int): Boolean {
        val direction = if (start < end) 1 else -1
        for (i in 1 until abs(end - start)) {
            if (hallway[start + direction * i] != null) {
                return false
            }
        }
        return true
    }

    fun recurse(collectedEnergy: Int) {
        if (collectedEnergy >= minEnergy) {
            return // Can never undercut current minimum along this path
        }

        // Check if we're done
        if (isDone()) {
            if (collectedEnergy < minEnergy) {
                println("Found new minimum: $collectedEnergy")
            }
            minEnergy = min(minEnergy, collectedEnergy)
            return
        }

        // Try to move pods into rooms
        for (i in hallway.indices) {
            hallway[i]?.let { pod ->
                // Is the final room ready for taking this pod?
                val roomIndex = roomIndexByPod[pod]!!
                if (!isRoomReadyToTakePods(roomIndex)) {
                    return@let
                }

                // Is the way into the final room free?
                val entrance = entrances[roomIndex]
                if (!isHallwayEmpty(i, entrance)) {
                    return@let
                }

                // Compute energy to take step
                val stepEnergy = (abs(entrance - i) + rooms[roomIndex].count { it == null }) * energyByPod[pod]!!

                // Move pod into room
                val indexInRoom = rooms[roomIndex].indices.last { rooms[roomIndex][it] == null }
                hallway[i] = null
                rooms[roomIndex][indexInRoom] = pod

                // Recurse
                recurse(collectedEnergy + stepEnergy)

                // Move pod back into hallway
                hallway[i] = pod
                rooms[roomIndex][indexInRoom] = null
            }
        }

        // Try to move pods into hallway
        for (roomIndex in rooms.indices) {
            // Is the room done?
            if (isRoomReadyToTakePods(roomIndex)) {
                continue
            }

            // Get the topmost pod
            val room = rooms[roomIndex]
            val indexInRoom = room.indices.firstOrNull { room[it] != null } ?: continue
            val pod = room[indexInRoom]

            val entrance = entrances[roomIndex]

            // Try to move pod into hallway to the right
            for (i in entrance + 1 until hallway.count()) {
                if (hallway[i] != null) {
                    break // Can't move further
                }
                if (entrances.contains(i)) {
                    continue // Can't stop here
                }

                // Compute energy to take step
                val stepEnergy = (room.count { it == null } + 1 + abs(i - entrance)) * energyByPod[pod]!!

                // Move pod into hallway
                hallway[i] = pod
                rooms[roomIndex][indexInRoom] = null

                // Recurse
                recurse(collectedEnergy + stepEnergy)

                // Move pod back into room
                hallway[i] = null
                rooms[roomIndex][indexInRoom] = pod
            }

            // Try to move pod into hallway to the left
            for (i in (entrance-1) downTo 0) {
                if (hallway[i] != null) {
                    break // Can't move further
                }
                if (entrances.contains(i)) {
                    continue // Can't stop here
                }

                // Compute energy to take step
                val stepEnergy = (room.count { it == null } + 1 + abs(i - entrance)) * energyByPod[pod]!!

                // Move pod into hallway
                hallway[i] = pod
                rooms[roomIndex][indexInRoom] = null

                // Recurse
                recurse(collectedEnergy + stepEnergy)

                // Move pod back into room
                hallway[i] = null
                rooms[roomIndex][indexInRoom] = pod
            }
        }
    }

    recurse(0)

    println(minEnergy) // 13556
}
