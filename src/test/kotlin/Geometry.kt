data class PointF(val x: Float, val y: Float) {
    operator fun minus(other: PointF) = PointF(x - other.x, y - other.y)
    operator fun plus(other: PointF) = PointF(x + other.x, y + other.y)
    operator fun times(other: Float) = PointF(x * other, y * other)
    operator fun div(other: Float) = PointF(x / other, y / other)
    fun distance(other: PointF) = (this - other).run { kotlin.math.sqrt(norm2()) }
    fun norm2() = x * x + y * y
}

data class Disk(val center: PointF, val radius: Float) {
    operator fun contains(point: PointF) = center.distance(point) <= radius
}

// Welzl's algorithm
fun smallestCircle(points: List<PointF>, r: List<PointF>): Disk? {
    val point = points.firstOrNull()
    if (point == null || r.size == 3) {
        return when (r.size) {
            0 -> null
            1 -> Disk(r[0], 0f)
            2 -> Disk((r[0] + r[1]) / 2f, r[0].distance(r[1]) / 2)
            else -> circumscribedCircle(r[0], r[1], r[2])
        }
    }
    val remaining = points.drop(1)
    val disk = smallestCircle(remaining, r)
    return if (disk != null && point in disk) disk else smallestCircle(remaining, r + point)
}

fun circumscribedCircle(a: PointF, b: PointF, c: PointF) : Disk {
    val div = Matrix(3).init(
        a.x, a.y, 1f,
        b.x, b.y, 1f,
        c.x, c.y, 1f,
    ).determinant() * 2
    val center = PointF(
        (Matrix(3).init(
            a.norm2(), a.y, 1f,
            b.norm2(), b.y, 1f,
            c.norm2(), c.y, 1f,
        ).determinant() / div).toFloat(),
        (Matrix(3).init(
            a.x, a.norm2(), 1f,
            b.x, b.norm2(), 1f,
            c.x, c.norm2(), 1f,
        ).determinant() / div).toFloat(),
    )
    return Disk(center, center.distance(a))
}
