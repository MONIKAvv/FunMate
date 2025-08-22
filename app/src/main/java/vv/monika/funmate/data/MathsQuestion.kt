package vv.monika.funmate.data

import vv.monika.funmate.MatchFunActivity.Op

data class MathsQuestion(
    val a: Int,
    val b: Int,
    val op: Op,
    val answer: Int,
    val options: List<Int>, // size 4
    val correctIndex: Int
)
