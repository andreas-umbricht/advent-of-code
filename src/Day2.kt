import java.io.BufferedReader
import java.io.File
import kotlin.math.pow

fun main() {
    val input = getInput()
    var passcode = 0L

    input.forEach {
        val splits = it.split("-")
        var startNumber = splits.first().toLong()
        val endNumber = splits.last().toLong()

        val ranges = mutableListOf<Range>()

        while (startNumber < endNumber) {
            if (startNumber.getAmountOfDigits() % 2 == 0) {
                ranges.add(
                    Range(
                        start = startNumber,
                        end = minOf(10.0.pow(startNumber.getAmountOfDigits()).toLong() - 1, endNumber)
                    )
                )
            }

            startNumber = 10.0.pow(startNumber.getAmountOfDigits()).toLong()
        }

        passcode += ranges.sumOf {
            step(
                start = it.start,
                end = it.end
            )
        }
    }

    println("PASSCODE: $passcode")
}

private fun step(start: Long, end: Long): Long {
    val startDigits = start.getAmountOfDigits()
    val endDigits = end.getAmountOfDigits()

    if (startDigits != endDigits) return 0

    val halfDigits = startDigits / 2

    var startFirstHalf = start / 10.0.pow(halfDigits).toInt()
    val startSecondHalf = start % 10.0.pow(halfDigits).toInt()

    val endFirstHalf = end / 10.0.pow(halfDigits).toInt()
    val endSecondHalf = end % 10.0.pow(halfDigits).toInt()

    var result = 0L

    /*1  2  0  0          4  9  2  3 <= start
    1  2  2  1          8  1  7  3 <= end

    1   2   0   0
    1   2   0   1
    1   2   0   2
    1   2   0   3
    1   2   0   4
    1   2   0   5*/

    if (startSecondHalf > startFirstHalf) {
        startFirstHalf++

        if (10.0.pow(halfDigits).toInt() * startFirstHalf > end) return 0
    }

    val lastPossible = if (endFirstHalf <= endSecondHalf) endFirstHalf else endFirstHalf - 1

    for(i in startFirstHalf..lastPossible) {
        println("FOUND: ${(10.0.pow(halfDigits).toInt() * i) + i}")
        result += (10.0.pow(halfDigits).toInt() * i) + i
    }
    /*1  2  0  1          0  0  0  0 <= start
    1  2  2  1          8  1  7  3 <= end*/

    return result
}

data class Range(
    val start: Long,
    val end: Long
)

private fun Long.getAmountOfDigits(): Int {
    var n = this
    var potence = 1
    while (n >= 10) {
        n /= 10
        potence++
    }

    return potence
}

private fun getInput(): List<String> {
    val bufferedReader: BufferedReader = File("day-2-input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    return inputString.removeSuffix("\n").split(",").filter { it.isNotEmpty() }
}