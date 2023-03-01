data class Point(val x: Int = 0, val y: Int = 0, val z: Int = 0) {
    fun rotateX() = Point(x, -z, y)
    fun rotateY() = Point(z, y, -x)
    fun rotateZ() = Point(-y, x, z)
    operator fun minus(other: Point) = Point(x - other.x, y - other.y, z - other.z)
    operator fun plus(other: Point) = Point(x + other.x, y + other.y, z + other.z)
    fun distanceM(other: Point) = (this - other).run { kotlin.math.abs(x) + kotlin.math.abs(y) }
    fun norm2() = x * x + y * y
}

fun String.toPoint() = split(" ").let { Point(it[0].toInt(), it[1].toInt()) }

class Board<T>(val width: Int, val height: Int, val cells: List<T>) {
    val points = (0 until height).flatMap { y ->
        (0 until width).map { x ->
            Point(x, y)
        }
    }

    val directions4 = listOf(Point(1, 0), Point(-1, 0), Point(0, 1), Point(0, -1))
    val directions8 = directions4 + listOf(Point(1, 1), Point(-1, 1), Point(1, -1), Point(-1, -1))

    init {
        if (cells.size != width * height) throw Error("invalid board ${cells.size} !=  $width * $height (${width * height})")
    }

    override fun toString() = cells.chunked(width) { it.joinToString("") }.joinToString("\n")

    fun toString(start: Point, end: Point) = cells.chunked(width) {
        it.joinToString("").substring(start.x, end.x + 1)
    }.subList(start.y, end.y + 1).joinToString("\n")

    fun isValid(x: Int, y: Int) = x in 0 until width && y in 0 until height
    fun getOrNull(x: Int, y: Int) = if (isValid(x, y)) cells[y * width + x] else null
    operator fun get(x: Int, y: Int) =
        getOrNull(x, y) ?: throw Error("invalid cell : x=$x y=$y width=$width height=$height")

    fun isValid(point: Point) = isValid(point.x, point.y)
    fun getOrNull(point: Point) = getOrNull(point.x, point.y)
    operator fun get(point: Point) = get(point.x, point.y)

    fun neighbors4(point: Point) = directions4.map { point + it }.filter { isValid(it) }
    fun neighbors8(point: Point) = directions8.map { point + it }.filter { isValid(it) }

    fun zone4(point: Point, predicate: (Point) -> Boolean) =
        zone(point) { neighbors4(it).filter(predicate) }

    fun zone8(point: Point, predicate: (Point) -> Boolean) =
        zone(point) { neighbors8(it).filter(predicate) }

    fun zone(point: Point, neighbors: (Point) -> List<Point>): List<Point> {
        val zone = mutableListOf(point)
        var index = 0
        while (index < zone.size) {
            zone.addAll(neighbors(zone[index++]).filter { it !in zone })
        }
        return zone
    }
}
