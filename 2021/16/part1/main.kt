import java.io.File

final class PacketReader(private val input: String) {
    private val TYPE_ID_LITERAL = 4

    private var index = 0

    fun readPacket(): Int {
        val version = readInteger(3)
        val typeId = readInteger(3)

        if (typeId == TYPE_ID_LITERAL) {
            return readLiteralValuePacketContents(version)
        }

        return readOperatorPacketContents(version)
    }

    private fun readInteger(numBits: Int): Int {
        return readBits(numBits).toInt(2)
    }

    private fun readBits(num: Int): String {
        return input.substring(index, index + num).also { index += num }
    }

    private fun readLiteralValuePacketContents(version: Int): Int {
        skipLiteralValue()
        return version
    }

    private fun readOperatorPacketContents(version: Int): Int {
        var result = version
        val lengthTypeId = readBit()

        if (lengthTypeId == '0') {
            val totalLength = readInteger(15)
            val cutoff = index + totalLength
            while (index < cutoff) {
                result += readPacket()
            }
        } else {
            val numSubpackets = readInteger(11)
            for (i in 1..numSubpackets) {
                result += readPacket()
            }
        }

        return result
    }

    private fun skipLiteralValue() {
        var hasMore = true
        while (hasMore) {
            hasMore = (readBit() != '0')
            skipBits(4)
        }
    }

    private fun readBit(): Char {
        return input[index].also { index += 1}
    }

    private fun skipBits(num: Int) {
        index += num
    }
}

fun main() {
    val input = File("input").bufferedReader().readLine().map { char ->
        Integer.toBinaryString(Integer.parseInt(Character.toString(char), 16)).padStart(4, '0')
    }.joinToString("")

    println(PacketReader(input).readPacket()) // 986
}
