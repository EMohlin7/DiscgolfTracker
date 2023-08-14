package se.umu.edmo0011.discgolftracker

import java.util.Date

data class Match(val course: String, val date: Long, val holes: List<Hole>)