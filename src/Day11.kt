import java.io.BufferedReader
import java.io.File

fun main() {
    val graph = getGraph(getInput())

    println("Passcode1: ${solve1(graph)}")
    println("Passcode2: ${solve2(graph)}")
}

private fun solve1(graph: MutableMap<String, Device>) =
    graph.getVertices(start = "you", end = "out")

private fun solve2(graph: MutableMap<String, Device>) =
    graph.getVertices(start = "svr", end = "fft") * graph.getVertices(start = "fft", end = "dac") * graph.getVertices(start = "dac", end = "out") +
    graph.getVertices(start = "svr", end = "dac") * graph.getVertices(start = "dac", end = "fft") * graph.getVertices(start = "fft", end = "out")


private fun MutableMap<String, Device>.getVertices(start: String, end: String) =
    this[start]!!.getVertices(graph = this, end = end).also { this.reset() }

private data class Device(val name: String, val outputs: List<String>) {
    var count: Long? = null

    fun getVertices(graph: Map<String, Device>, end: String): Long =
        if (name == end) 1L
        else count ?: outputs.mapNotNull { graph[it] }.sumOf { device ->
                device.getVertices(graph = graph, end = end)
            }.also { count = it }
}

private fun MutableMap<String, Device>.reset() = this.forEach { it.value.count = null }

private fun getGraph(input: List<String>): MutableMap<String, Device> {
    val graph = mutableMapOf<String, Device>()

    input.forEach { row ->
        val splits = row.split(" ")

        val deviceName = splits.first().removeSuffix(":")

        graph[deviceName] = Device(
            name = deviceName,
            outputs = buildList {
                for (i in 1..splits.lastIndex) {
                    add(splits[i])
                }
            },
        )
    }

    graph["out"] = Device(name = "out", outputs = listOf())

    return graph
}

private fun getInput(): List<String> {
    val bufferedReader: BufferedReader = File("day-11-input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    return inputString.split("\n")
}