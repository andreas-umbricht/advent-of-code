import java.io.BufferedReader
import java.io.File

fun main() {
    val input = getInput()

    val linkedList = createLinkedList()

    puzzle(input, linkedList)
}

private fun puzzle(input: List<String>, linkedList: List<Node>) {
    var passThroughPasscode = 0
    var landOnPasscode = 0
    var currentElement = linkedList.first { it.index == 50 }

    input.forEach {
        if (it.first() == 'L') {
            repeat(it.removePrefix("L").toInt()) {
                currentElement = currentElement.previous!!
                if (currentElement.index == 0) passThroughPasscode++
            }
        } else {
            repeat(it.removePrefix("R").toInt()) {
                currentElement = currentElement.next!!
                if (currentElement.index == 0) passThroughPasscode++
            }
        }

        if (currentElement.index == 0) landOnPasscode++
    }

    println("Puzzle 1: $landOnPasscode")
    println("Puzzle 2: $passThroughPasscode")
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