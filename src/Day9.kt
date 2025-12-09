import java.io.BufferedReader
import java.io.File
import kotlin.math.abs
import kotlin.math.max

fun main() {
    val input = getInput()

    val passCode1 = solve1(input)
    val passCode2 = solve2(input)

    println("Passcode1: $passCode1")
    println("Passcode2: $passCode2")
}

private fun solve2(input: List<String>): Long {
    val redPoints = input.map {
        it.split(",").let {
            Point(it[0].toLong(), it[1].toLong())
        }
    }

    var res = 0L
    val maxIterations = (redPoints.size * (redPoints.size - 1)) / 2
    var iteration = 0L

    redPoints.forEachIndexed { index1, point ->
        for (i in (index1 + 1)..redPoints.lastIndex) {
            val checkPoint = redPoints[i]
            val area = (abs(point.x - checkPoint.x) + 1) * (abs(point.y - checkPoint.y) + 1)
            if (
                area > res &&
                (point to Point(point.x, checkPoint.y)).isLineSegmentInsidePolygon(polygon = redPoints) &&
                (point to Point(checkPoint.x, point.y)).isLineSegmentInsidePolygon(polygon = redPoints) &&
                (checkPoint to Point(point.x, checkPoint.y)).isLineSegmentInsidePolygon(polygon = redPoints) &&
                (checkPoint to Point(checkPoint.x, point.y)).isLineSegmentInsidePolygon(polygon = redPoints)
            ) {
                res = area
                println("$res       ${iteration}/$maxIterations")
            }

            if (iteration % 1000L == 0L) {
                println("$res       ${iteration}/$maxIterations")
            }

            ++iteration
        }
    }

    return res
}

private fun Pair<Point, Point>.isLineSegmentInsidePolygon(
    polygon: List<Point>,
): Boolean {
    val pA = this.first
    val pB = this.second

    if (pA.x == pB.x) {
        for (y in minOf(pA.y, pB.y)..maxOf(pA.y, pB.y)) {
            if (!isPointInPolygon(polygon = polygon, p = Point(pA.x, y))) {
                return false
            }
        }
    } else {
        for (x in minOf(pA.x, pB.x)..maxOf(pA.x, pB.x)) {
            if (!isPointInPolygon(polygon = polygon, p = Point(x, pA.y))) {
                return false
            }
        }
    }

    return true
}

private fun isPointInPolygon(p: Point, polygon: List<Point>): Boolean {
    var inside = false
    val n = polygon.size

    for (i in 0 until n) {
        val a = polygon[i]
        val b = polygon[(i + 1) % n]

        // 1) Check if point is exactly on the segment AB
        if (isPointOnSegment(p, a, b)) return true

        // 2) Standard ray-casting
        val intersects = (a.y > p.y) != (b.y > p.y) &&
                p.x < (b.x - a.x) * (p.y - a.y).toDouble() / (b.y - a.y) + a.x

        if (intersects) inside = !inside
    }

    return inside
}

private fun isPointOnSegment(p: Point, a: Point, b: Point): Boolean {
    val cross = (p.y - a.y) * (b.x - a.x) - (p.x - a.x) * (b.y - a.y)
    if (cross != 0L) return false

    val dot = (p.x - a.x) * (b.x - a.x) + (p.y - a.y) * (b.y - a.y)
    if (dot < 0) return false

    val lenSq = (b.x - a.x) * (b.x - a.x) + (b.y - a.y) * (b.y - a.y)
    if (dot > lenSq) return false

    return true
}


private fun solve1(input: List<String>): Long {
    val points = input.map {
        it.split(",").let {
            Point(it[0].toLong(), it[1].toLong())
        }
    }

    var res = 0L

    points.forEach { pointA ->
        points.forEach { pointB ->
            res = max(res, (abs(pointA.x - pointB.x) + 1) * (abs(pointA.y - pointB.y) + 1))
        }
    }

    return res
}

private data class Point(val x: Long, val y: Long)

private fun getInput(): List<String> {
    val bufferedReader: BufferedReader = File("day-9-input.txt").bufferedReader()
    val inputString = bufferedReader.use { it.readText() }

    return inputString.split("\n")
}