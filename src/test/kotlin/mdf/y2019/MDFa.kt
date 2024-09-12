package mdf.y2019

import mdf.BaseTest
import org.junit.Test
import tools.read.readAllLines
import tools.text.toInts

class MDFa : BaseTest() {
    @Test
    fun test1() = test(4, ::p1)

    @Test
    fun test2() = test(6, ::p2)

    @Test
    fun test3() = test(7, ::p3)

    private fun p1() {
        println(readAllLines().drop(1).filter { it == it.reversed() }.joinToString(" "))
    }

    private fun p2() {
        readln()
        val hisDeck = readln().toInts().toMutableList()
        val myDeck = readln().toInts().toMutableList()
        while (myDeck.isNotEmpty() && hisDeck.isNotEmpty()) {
            val myCard = myDeck.removeAt(0)
            val hisCard = hisDeck.removeAt(0)
            when {
                myCard > hisCard -> myDeck.add(myCard)
                myCard < hisCard -> hisDeck.add(hisCard)
            }
        }
        println(when {
            myDeck.isNotEmpty() -> "G"
            hisDeck.isNotEmpty() -> "P"
            else -> "N"
        })
    }

    private fun p3() {
        val constraints = readAllLines().drop(1).map { it.split(" ") }
        val ingredients = constraints.flatMap { it.toList() }.toSet()
        println(findRecipe(constraints, ingredients, emptyList())?.joinToString(" < ") ?: "KO")
    }

    private fun findRecipe(constraints: List<List<String>>, ingredients: Set<String>, recipe: List<String>): List<String>? {
        val seconds = constraints.map { it.last() }
        val next = ingredients.filter { it !in seconds }.toSet()
        val remaining = constraints.filter { it.first() !in next }
        return when {
            next.isNotEmpty() -> findRecipe(remaining, ingredients - next, recipe + next)
            constraints.isEmpty() -> recipe + ingredients
            else -> null
        }
    }
}
