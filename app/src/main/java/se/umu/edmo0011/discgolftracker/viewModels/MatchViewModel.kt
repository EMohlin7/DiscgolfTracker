package se.umu.edmo0011.discgolftracker.viewModels

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.Hole
import se.umu.edmo0011.discgolftracker.MATCHES_KEY
import se.umu.edmo0011.discgolftracker.Match
import se.umu.edmo0011.discgolftracker.MatchGraph
import se.umu.edmo0011.discgolftracker.OngoingMatch
import se.umu.edmo0011.discgolftracker.OngoingMatchGraph
import se.umu.edmo0011.discgolftracker.SharedPreferencesHelper
import se.umu.edmo0011.discgolftracker.navigateAndPopUp
import java.util.Date

class MatchViewModel(private val state: SavedStateHandle) : ViewModel()
{
    private val HOLES_KEY = "match hole key"
    private val TIME_KEY = "time key"
    //var numPlayers by mutableStateOf(0); private set
    val PAR_DEFAULT = 3

    var players: List<String> = emptyList(); private set

    private var holes = mutableStateListOf<Hole>()
    val playedHoles: List<Hole> get() = holes
    var currentHoleIndex by mutableStateOf(0); private set

    var selectedTab by mutableStateOf(0); private set
    val showScore by derivedStateOf { selectedTab == 1 }

    private var ended = false
    var startTime: Long = 0; private set

    init {
        val temp = state.get<Array<Hole>>(HOLES_KEY)
        val h = temp?.toMutableList() ?: OngoingMatch.getInstance().holes
        if(!h.isNullOrEmpty())
        {
            players = h[0].players
            holes = h.toMutableStateList()
            currentHoleIndex = holes.size-1
        }

        val t = state.get<Long>(TIME_KEY) ?: OngoingMatch.getInstance().startTime
        if(t != null)
            startTime = t
    }

    fun onNewMatch(navCon: NavController)
    {
        navCon.navigate(OngoingMatchGraph.route)
    }

    fun onCreateMatch(navCon: NavController, players: List<String>)
    {
        this.players = players
        holes.add(Hole(1, PAR_DEFAULT, players, List(players.size){0}))
        currentHoleIndex = 0
        startTime = Date().time
        navCon.navigate(OngoingMatchGraph.Match.route)
        Log.w("Match", players.toString())
    }

    private fun setHole(index: Int)
    {
        if(index == holes.size)
            holes.add(newHole(index+1))

        currentHoleIndex = if(index < 0) 0 else index
    }

    fun editHole(par: Int, throws: List<Int>)
    {
        holes[currentHoleIndex] = holes[currentHoleIndex].copy(par = par, throws = throws)
    }

    fun nextHole()
    {
        setHole(currentHoleIndex + 1)
    }


    fun prevHole()
    {
        setHole(currentHoleIndex - 1)
    }

    private fun newHole(number: Int): Hole
    {
        return Hole(number, PAR_DEFAULT, players, List<Int>(players.size){0})
    }

    fun stopGame(navCon: NavController)
    {
        ended = true
        navCon.navigateAndPopUp(MatchGraph.NewMatch.route, true)
    }

    fun saveGame(navCon: NavController, course: String)
    {
        val list = SharedPreferencesHelper.getList<Match>(navCon.context, MATCHES_KEY).toMutableList()
        val date = Date().time
        val duration = date - startTime
        list.add(Match(course, duration, date, holes))
        SharedPreferencesHelper.saveList<Match>(navCon.context, list, MATCHES_KEY)

        stopGame(navCon)
    }

    fun onSelectedTab(index: Int)
    {
        selectedTab = index
    }

    override fun onCleared() {
        if(ended) {
            OngoingMatch.getInstance().clearMatch()
            state.remove<Array<Hole>>(HOLES_KEY)
            state.remove<Long>(TIME_KEY)
        }
        else {
            OngoingMatch.getInstance().save(holes, startTime)
            state.set(HOLES_KEY, holes.toTypedArray())
            state.set(TIME_KEY, startTime)
        }

        super.onCleared()
        Log.w("Match", "On cleared")
    }
}