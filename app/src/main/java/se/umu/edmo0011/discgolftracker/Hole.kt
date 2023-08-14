package se.umu.edmo0011.discgolftracker

data class Hole(val number: Int, val par: Int, val players: List<String>, val throws: List<Int>)