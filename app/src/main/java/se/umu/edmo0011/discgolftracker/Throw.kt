package se.umu.edmo0011.discgolftracker

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Throw(
    val distance: Int,
    val startLat: Double,
    val startLon: Double,
    val disc: String,
    val dateMs: Long,
    val course: String,
    val hole: String
) : Parcelable
