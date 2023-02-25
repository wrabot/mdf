fun testDeterminant() {
    assert(
        DoubleMatrix(2).init(
            1f, 4f,
            2f, 9f,
        ).determinant() == 1.0
    )
    assert(
        DoubleMatrix(3).init(
            4f, 5f, 1f,
            9f, 8f, 2f,
            7f, 6f, 3f,
        ).determinant() == -19.0
    )
    assert(
        DoubleMatrix(4).init(
            2f, 4f, 5f, 6f,
            -1f, 5f, 6f, 9f,
            3f, 7f, 1f, -6f,
            4f, -2f, 3f, 5f,
        ).determinant() == 108.0
    )
}
