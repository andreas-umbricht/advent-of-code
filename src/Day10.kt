import java.io.BufferedReader
import java.io.File
import kotlin.text.forEach

fun main() {
    val input = getInput()

    val passcode1 = solve1(input)
    val passcode2 = solve2(input)

    println("Passcode1: $passcode1")
    println("Passcode2: $passcode2")
}

private fun solve2(input: List<String>): Int {
    return input.sumOf { row ->
        val splits = row.split(" ")

        val joltage = splits.last().removePrefix("{").removeSuffix("}").split(",").map { it.toInt() }

        val buttons = splits.subList(1, splits.lastIndex).map {
            buildList {
                it.forEach {
                    if (it != '(' && it != ')' && it != ',') {
                        add(it.digitToInt())
                    }
                }
            }
        }

        val result = getNewPattern(
            joltage = joltage,
            buttons = buttons,
            pattern = List(joltage.size) { 0 },
            currentMinPress = Int.MAX_VALUE,
            currentPress = 0,
            level = 1
        )

        result
    }
}

private fun getNewPattern(
    joltage: List<Int>,
    pattern: List<Int>,
    buttons: List<List<Int>>,
    currentMinPress: Int,
    currentPress: Int,
    level: Int
): Int {
    var minPress = currentMinPress

    val joltageDiffs = joltage.mapIndexed { index, it -> it - pattern[index] }

    if (currentPress + joltageDiffs.max() >= minPress) return Int.MAX_VALUE

    val buttonIndex = joltageDiffs.mapIndexedNotNull { index, value ->
            if (value != 0) {
                Pair(value, index)
            } else {
                null
            }
        }.minByOrNull { it.first }?.second

    if (buttonIndex == null) {
        return currentPress
    }

    val relevantButtons = buttons.filter { it.contains(buttonIndex) }

    val sumValue = joltage[buttonIndex] - pattern[buttonIndex]

    val maxButtonPresses = List(relevantButtons.size) { relevantButtons[it].minOf { joltageDiffs[it] } }

    val combinations = findConstrainedCombinations(sumValue, maxButtonPresses.map { it })

    return combinations.minOfOrNull { combination ->
        calculateFinalCounters(combination = combination, buttons = relevantButtons, joltage = joltage, pattern = pattern)?.let {
            getNewPattern(
                joltage = joltage,
                currentPress = currentPress + combination.sum(),
                pattern = it,
                currentMinPress = minPress,
                buttons = buttons.filter { !it.contains(buttonIndex) },
                level = level + 1
            ).also {
                minPress = it
            }
        } ?: Int.MAX_VALUE
    } ?: Int.MAX_VALUE
}

private fun calculateFinalCounters(
    combination: List<Int>,
    buttons: List<List<Int>>,
    joltage: List<Int>,
    pattern: List<Int>
): List<Int>? {
    val result = MutableList(pattern.size) { pattern[it] }

    for (i in combination.indices) {
        val presses = combination[i]
        val buttonEffect = buttons[i]

        for (indexToIncrement in buttonEffect) {
            result[indexToIncrement] += presses
        }

        if (result.zip(joltage).any { (aI, bI) -> bI < aI }) {
            return null
        }
    }

    return result
}

fun findConstrainedCombinations(targetSum: Int, maxConstraints: List<Int>): List<List<Int>> {
    val results = mutableListOf<List<Int>>()
    val currentCombination = mutableListOf<Int>()

    fun backtrack(index: Int, remainingSum: Int) {
        if (index == maxConstraints.size) {
            if (remainingSum == 0) {
                results.add(currentCombination.toList())
            }
            return
        }
        val maxValForThisPosition = maxConstraints[index]

        val upperLimit = minOf(maxValForThisPosition, remainingSum)

        for (i in 0..upperLimit) {
            currentCombination.add(i)

            backtrack(index + 1, remainingSum - i)

            currentCombination.removeAt(currentCombination.lastIndex)
        }
    }

    backtrack(0, targetSum)

    return results
}

private fun solve1(input: List<String>): Long {
    return input.sumOf { row ->
        val splits = row.split(" ")

        val a = buildString {
            splits[0].forEach {
                if (it == '#') {
                    append('1')
                } else if (it == '.') {
                    append('0')
                }
            }
        }

        val buttons = splits.subList(1, splits.lastIndex).map {
            buildList {
                it.forEach {
                    if (it != '(' && it != ')' && it != ',') {
                        add(it.digitToInt())
                    }
                }
            }
        }

        val searchedNumber = a.toInt(radix = 2)

        findFastest(searchedNumber = searchedNumber, buttons = buttons, patternWidth = a.length).also {
            println("$it")
        }
    }
}

private fun findFastest(searchedNumber: Int, buttons: List<List<Int>>, patternWidth: Int): Long {
    var inputNumbers = setOf(0)

    val nextInputNumbers = mutableSetOf<Int>()
    val checkedNumbers = mutableSetOf<Int>()

    var moveNumber = 1L

    while (true) {
        inputNumbers.forEach { inputNumber ->
            val newNumbers = buttons.map { positions ->
                var outputNumber = inputNumber

                for (index in positions) {
                    outputNumber = outputNumber xor (1 shl (patternWidth - 1 - index))
                }

                outputNumber
            }

            if(newNumbers.contains(searchedNumber)) {
                return moveNumber
            }

            nextInputNumbers.addAll(newNumbers)
        }

        moveNumber++

        checkedNumbers.addAll(inputNumbers)

        inputNumbers = nextInputNumbers.filter { !checkedNumbers.contains(it) }.toSet()
        nextInputNumbers.clear()
    }
}

private fun getInput(): List<String> {
    val bufferedReader: BufferedReader = File("day-10-input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    return inputString.split("\n")
}