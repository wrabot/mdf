import org.junit.Assert
import org.junit.Test

class Tests {
    @Test
    fun testDeterminant() {
        Assert.assertEquals(
            DoubleMatrix(2).init(
                1f, 4f,
                2f, 9f,
            ).determinant().toFloat(),
            1f
        )
        Assert.assertEquals(
            DoubleMatrix(3).init(
                4f, 5f, 1f,
                9f, 8f, 2f,
                7f, 6f, 3f,
            ).determinant().toFloat(),
            -19f
        )
        Assert.assertEquals(
            DoubleMatrix(4).init(
                2f, 4f, 5f, 6f,
                -1f, 5f, 6f, 9f,
                3f, 7f, 1f, -6f,
                4f, -2f, 3f, 5f,
            ).determinant().toFloat(),
            108f
        )
    }

    @Test
    fun testMunkres() {
        val couples = munkres(
            IntMatrix(3).init(
                1, 2, 3,
                2, 4, 6,
                3, 6, 9,
            )
        )
        Assert.assertEquals(couples, listOf(0 to 2, 1 to 1, 2 to 0))
    }
}
