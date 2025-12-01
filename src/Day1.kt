import java.io.BufferedReader
import java.io.File

fun main() {
    val input = getInput()

    val linkedList = createLinkedList()

    puzzle1(input, linkedList)
    puzzle2(input, linkedList)
}

private fun puzzle1(input: List<String>, linkedList: List<Node>) {
    var passcode = 0
    var currentElement = linkedList.first { it.index == 50 }

    input.forEach {
        if (it.first() == 'L') {
            repeat(it.removePrefix("L").toInt()) {
                currentElement = currentElement.previous!!
            }
        } else {
            repeat(it.removePrefix("R").toInt()) {
                currentElement = currentElement.next!!
            }
        }

        if (currentElement.index == 0) passcode++
    }

    println("Puzzle 1: $passcode")
}

private fun puzzle2(input: List<String>, linkedList: List<Node>) {
    var passcode = 0
    var currentElement = linkedList.first { it.index == 50 }

    input.forEach {
        if (it.first() == 'L') {
            repeat(it.removePrefix("L").toInt()) {
                currentElement = currentElement.previous!!
                if (currentElement.index == 0) passcode++
            }
        } else {
            repeat(it.removePrefix("R").toInt()) {
                currentElement = currentElement.next!!
                if (currentElement.index == 0) passcode++
            }
        }
    }

    println("Puzzle 2: $passcode")
}

private fun getInput(): List<String> {
    val bufferedReader: BufferedReader = File("day-1-input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    return inputString.split("\n").filter { it.isNotEmpty() }
}

private fun createLinkedList(): List<Node> {
    val result = mutableListOf<Node>()

    result.add(
        Node(
            previous = null,
            index = 0,
            next = null
        )
    )

    repeat(99) {
        val n = Node(
            previous = result[it],
            index = it + 1,
            next = null
        )

        result.add(n)
        result[it].next = n
    }

    result.first().previous = result.last()
    result.first().next = result[1]
    result.last().next = result.first()

    return result
}

private data class Node(
    var next: Node?,
    val index: Int,
    var previous: Node?
)