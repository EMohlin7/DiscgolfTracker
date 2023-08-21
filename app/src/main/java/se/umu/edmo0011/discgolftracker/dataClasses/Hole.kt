package se.umu.edmo0011.discgolftracker.dataClasses

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Hole(val number: Int, val par: Int, val players: List<String>, val throws: List<Int>) : Parcelable