package se.umu.edmo0011.discgolftracker

data class Throw(
    val distance: Int,
    val startLat: Double,
    val startLon: Double,
    val disc: String,
    val course: String,
    val hole: String
)
