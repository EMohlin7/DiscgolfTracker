package se.umu.edmo0011.discgolftracker.viewModels

import android.content.Context
import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import se.umu.edmo0011.discgolftracker.HistoryGraph
import se.umu.edmo0011.discgolftracker.Hole
import se.umu.edmo0011.discgolftracker.MATCHES_KEY
import se.umu.edmo0011.discgolftracker.Match
import se.umu.edmo0011.discgolftracker.MatchGraph
import se.umu.edmo0011.discgolftracker.OngoingMatch
import se.umu.edmo0011.discgolftracker.OngoingMatchGraph
import se.umu.edmo0011.discgolftracker.Screen
import se.umu.edmo0011.discgolftracker.SharedPreferencesHelper
import se.umu.edmo0011.discgolftracker.navigateAndPopUp
import java.time.LocalDateTime
import java.util.Date
import kotlin.math.max

class MatchViewModel(private val state: SavedStateHandle) : ViewModel()
{
    //var numPlayers by mutableStateOf(0); private set
    val PAR_DEFAULT = 3

    var players: List<String> = emptyList(); private set

    private var holes = mutableListOf<Hole>()
    val playedHoles: List<Hole> get() = holes
    var currentHole by mutableStateOf(Hole(0,0, emptyList(), emptyList())); private set

    var selectedTab by mutableStateOf(0); private set
    val showScore by derivedStateOf { selectedTab == 1 }

    private var ended = false

    init {
        val h = OngoingMatch.getInstance().holes
        if(h != null)
        {
            players = h[0].players
            holes = h.toMutableList()
            currentHole = holes[holes.size-1]
        }
    }

    fun onNewMatch(navCon: NavController)
    {
        navCon.navigate(OngoingMatchGraph.SetupMatch.route)
    }

    fun onCreateMatch(navCon: NavController, players: List<String>)
    {
        this.players = players
        currentHole = newHole(1)
        holes.add(currentHole)
        OngoingMatch.getInstance().startTime = Date().time
        navCon.navigate(OngoingMatchGraph.Match.route)
        Log.w("Match", players.toString())
    }

    private fun setHole(hole: Hole)
    {
        if(holes.size == hole.number) {
            holes[hole.number - 1] = hole
            holes.add(newHole(hole.number+1))
        }
        else if(holes.size > hole.number) {
            holes[hole.number - 1] = hole
        }
        else {
            holes.add(hole)
        }
    }


    fun nextHole(hole: Hole)
    {
        setHole(hole)
        currentHole = holes[hole.number]
    }


    fun prevHole(hole: Hole)
    {
        setHole(hole)
        currentHole = holes[max(currentHole.number - 2, 0)]
    }

    private fun newHole(number: Int): Hole
    {
        return Hole(number, PAR_DEFAULT, players, List<Int>(players.size){0})
    }

    fun stopGame(navCon: NavController)
    {
        ended = true
        navCon.navigateAndPopUp(MatchGraph.NewMatch.route, false)
    }

    fun saveGame(navCon: NavController, course: String)
    {
        val list = SharedPreferencesHelper.getList<Match>(navCon.context, MATCHES_KEY).toMutableList()
        val date = Date().time
        val duration = date - (OngoingMatch.getInstance().startTime ?: 0)
        list.add(Match(course, duration, date, holes))
        SharedPreferencesHelper.saveList<Match>(navCon.context, list, MATCHES_KEY)

        stopGame(navCon)
    }

    fun onSelectedTab(index: Int)
    {
        selectedTab = index
    }

    override fun onCleared() {
        if(ended)
            OngoingMatch.getInstance().clearMatch()
        else
            OngoingMatch.getInstance().holes = holes

        super.onCleared()
        Log.w("Match", "On cleared")
    }
}