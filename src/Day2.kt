import java.io.BufferedReader
import java.io.File
import kotlin.math.pow

fun main() {
    val input = getInput()
    var passcode = 0L
    var passcode2 = 0L

    input.forEach {
        val splits = it.split("-")
        var startNumber = splits.first().toLong()
        val endNumber = splits.last().toLong()

        while (startNumber < endNumber) {
            passcode += solve1(
                start = startNumber,
                end = minOf(10.0.pow(startNumber.getAmountOfDigits()).toLong() - 1, endNumber)
            )

            passcode2 += solve2(
                start = startNumber,
                end = minOf(10.0.pow(startNumber.getAmountOfDigits()).toLong() - 1, endNumber)
            )

            startNumber = 10.0.pow(startNumber.getAmountOfDigits()).toLong()
        }
    }

    println("Puzzle 1: $passcode")
    println("Puzzle 2: $passcode2")
}

private fun solve2(start: Long, end: Long): Long {
    val digits = start.getAmountOfDigits()

    val results = mutableSetOf<Long>()

    for(cut in 1 .. digits / 2) {
        if (digits % cut == 0) {
            val startChunks = start.getChunks(cut)
            val endChunks = end.getChunks(cut)

            for(n in startChunks.first()..endChunks.first()) {
                val res = (1..(digits / cut)).fold(0L) { acc, _ -> acc * 10.0.pow(cut).toLong() + n }
                if (res in start..end) {
                    results.add(res)
                }
            }
        }
    }

    return results.sum()
}

private fun Long.getChunks(n: Int): List<Long> {
    var x = this
    val res = mutableListOf<Long>()

    while (x != 0L) {
        res.add(x % 10.0.pow(n).toInt())
        x /= 10.0.pow(n).toInt()
    }

    return res.reversed()
}

private fun solve1(start: Long, end: Long): Long {
    val startDigits = start.getAmountOfDigits()
    val endDigits = end.getAmountOfDigits()

    if (startDigits % 2 != 0) return 0

    if (startDigits != endDigits) return 0

    val halfDigits = startDigits / 2

    var startFirstHalf = start / 10.0.pow(halfDigits).toInt()
    val startSecondHalf = start % 10.0.pow(halfDigits).toInt()

    val endFirstHalf = end / 10.0.pow(halfDigits).toInt()
    val endSecondHalf = end % 10.0.pow(halfDigits).toInt()

    var result = 0L

    if (startSecondHalf > startFirstHalf) {
        startFirstHalf++

        if (10.0.pow(halfDigits).toInt() * startFirstHalf > end) return 0
    }

    val lastPossible = if (endFirstHalf <= endSecondHalf) endFirstHalf else endFirstHalf - 1

    for(i in startFirstHalf..lastPossible) {
        result += (10.0.pow(halfDigits).toInt() * i) + i
    }

    return result
}

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