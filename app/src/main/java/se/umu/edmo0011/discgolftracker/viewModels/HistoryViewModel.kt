package se.umu.edmo0011.discgolftracker.viewModels

import android.content.Context
import androidx.lifecycle.ViewModel
import se.umu.edmo0011.discgolftracker.MATCHES_KEY
import se.umu.edmo0011.discgolftracker.Match
import se.umu.edmo0011.discgolftracker.SharedPreferencesHelper

class HistoryViewModel : ViewModel()
{
    var loadedMatches: List<Match> = emptyList(); private set

    fun loadMatches(context: Context): List<Match>
    {
        loadedMatches = SharedPreferencesHelper.getList<Match>(context, MATCHES_KEY)
        return loadedMatches
    }

    fun deleteMatch(context: Context, date: Long): List<Match>
    {
        val list = loadedMatches.toMutableList()
        list.removeIf { it.dateMs == date }
        loadedMatches = list
        SharedPreferencesHelper.saveList<Match>(context, loadedMatches, MATCHES_KEY)
        return loadedMatches
    }
}