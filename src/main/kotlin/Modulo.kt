fun Long.times(multiplier: Long, modulo: Long): Long {
    if (this < 0) error("negative multiplicand")
    if (multiplier < 0) error("negative multiplier")
    if (modulo < 0) error("negative modulo")
    var res = 0L
    var a = this % modulo
    var b = multiplier % modulo
    while (true) {
        if (b and 1L > 0L) res = (res + a) % modulo
        b = b shr 1
        if (b == 0L) return res
        a = a shl 1
        if (a < 0) error("overflow")
        a %= modulo
    }
}

tailrec fun pgcd(a: Int, b: Int): Int = if (b == 0) a else pgcd(b, a % b)
