import org.junit.Assert
import org.junit.Test

class Tests {
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
