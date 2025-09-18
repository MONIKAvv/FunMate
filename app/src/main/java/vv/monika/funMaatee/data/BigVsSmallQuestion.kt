package vv.monika.funMaatee.data

import vv.monika.funMaatee.Comparison

data class BigVsSmallQuestion(
    val question: Int,              // the base number shown (e.g., 456)
    val comparison: Comparison,     // GREATER or SMALLER
    val options: List<Int>,         // 4 options
    val correctIndex: Int
)
