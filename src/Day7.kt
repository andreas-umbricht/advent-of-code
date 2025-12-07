import java.io.BufferedReader
import java.io.File

fun main() {
    val input = getInput()

    val passCode1 = solve1(input)
    val passCode2 = solve2(input)

    println("Passcode1: $passCode1")
    println("Passcode2: $passCode2")
}

private fun solve2(input: List<String>): Long {
    val scores = List(input.size) { rowIndex -> MutableList(input.first().length) { if (rowIndex == input.lastIndex) 1L else 0L } }

    var result = 0L

    var rowIndex = input.lastIndex

    while (result == 0L) {
        val row = input[rowIndex]

        row.forEachIndexed { charIndex, char ->
            if (rowIndex == input.lastIndex || char == '^') {
                for (checkRelevantRowIndex in rowIndex - 1 downTo 0) {
                    val currentScore = scores[rowIndex][charIndex]

                    if (input[checkRelevantRowIndex][charIndex] == '.') {
                        listOf(1, -1).forEach { neighbourIndex ->
                            if (input[checkRelevantRowIndex].getOrNull(charIndex + neighbourIndex) == '^') {
                                scores[checkRelevantRowIndex][charIndex + neighbourIndex] += currentScore
                            }
                        }
                    } else if(input[checkRelevantRowIndex][charIndex] == 'S') {
                        result = currentScore
                    } else {
                        return@forEachIndexed
                    }
                }
            }
        }

        rowIndex--
    }

    return result
}

private fun solve1(input: List<String>): Int {
    val startIndex = input.first().indexOfFirst { it == 'S' }

    val indexes = mutableSetOf(startIndex)

    val nextRowElements = mutableSetOf<Int>()

    var result = 0

    input.drop(1).forEach { row ->
        indexes.forEach { index ->
            when (row.getOrNull(index)) {
                '.' -> nextRowElements.add(index)
                null -> {}
                else -> {
                    result++
                    nextRowElements.add(index + 1)
                    nextRowElements.add(index - 1)
                }
            }
        }

        indexes.clear()
        indexes.addAll(nextRowElements)
        nextRowElements.clear()
    }

    return result
}

private fun getInput(): List<String> {
    val bufferedReader: BufferedReader = File("day-7-input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    return inputString.split("\n")
}
