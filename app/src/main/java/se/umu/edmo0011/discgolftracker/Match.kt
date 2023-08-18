package se.umu.edmo0011.discgolftracker

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Match(val course: String, val duration: Long, val dateMs: Long, val holes: List<Hole>) : Parcelable