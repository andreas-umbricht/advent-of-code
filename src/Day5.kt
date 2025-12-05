import java.io.BufferedReader
import java.io.File

fun main() {
    val input = getInput()

    val passCode1 = solve1(input)
    val passCode2 = solve2(input)

    println("Passcode1: $passCode1")
    println("Passcode1: $passCode2")
}

private fun solve2(input: List<String>): Long {
    var isRanges = true

    val rangeList = input.mapNotNull {
        if (it.isEmpty() || !isRanges) {
            isRanges = false
            null
        } else {
            it.split("-").let {
                it[0].toLong()..it[1].toLong()
            }
        }
    }.sortedBy { it.first }

    var i = 0
    var result = 0L

    rangeList.forEachIndexed { index, range ->
        if (i > index) return@forEachIndexed

        var currentRange = range
        var finished = false
        var nextIndex = 1

        while (!finished) {
            i++
            val nextRange = rangeList.getOrNull(index + nextIndex)
            if (nextRange == null) {
                finished = true
                i = Int.MAX_VALUE
            } else if (currentRange.last < nextRange.first) {
                finished = true
            } else if (currentRange.last < nextRange.last) {
                currentRange = currentRange.first..nextRange.last
            }

            nextIndex++
        }

        result += currentRange.last - currentRange.first + 1
    }

    return result
}

private fun solve1(input: List<String>): Int {
    var isRanges = true

    val rangeList = input.mapNotNull {
        if (it.isEmpty()) {
            isRanges = false
            null
        } else if (isRanges) {
            it.split("-").let {
                listOf(
                    Input.RangeStart(it[0].toLong()),
                    Input.RangeEnd(it[1].toLong())
                )
            }
        } else {
            listOf(Input.Ingredient(it.toLong()))
        }
    }.flatten().sortedBy { it.value + it.importance }

    var rangeStarts = 0
    var freshIngredients = 0

    rangeList.forEach {
        when (it) {
            is Input.RangeStart -> rangeStarts++
            is Input.RangeEnd -> rangeStarts--
            is Input.Ingredient -> if (rangeStarts != 0) freshIngredients++
        }
    }

    return freshIngredients
}

sealed interface Input {
    val value: Long
    val importance: Float

    data class RangeStart(override val value: Long): Input {
        override val importance: Float
            get() = 0f
    }
    data class RangeEnd(override val value: Long): Input {
        override val importance: Float
            get() = 0.2f
    }
    data class Ingredient(override val value: Long): Input {
        override val importance: Float
            get() = 0.1f
    }
}

private fun getInput(): List<String> {
    val bufferedReader: BufferedReader = File("day-5-input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    return inputString.split("\n")
}

