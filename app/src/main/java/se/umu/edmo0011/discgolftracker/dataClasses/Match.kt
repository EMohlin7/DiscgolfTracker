package se.umu.edmo0011.discgolftracker.dataClasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize
import se.umu.edmo0011.discgolftracker.dataClasses.Hole

@Parcelize
data class Match(val course: String, val duration: Long, val dateMs: Long, val holes: List<Hole>) : Parcelable