package se.umu.edmo0011.discgolftracker.viewModels

import android.util.Log
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.LiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.dataClasses.Hole
import se.umu.edmo0011.discgolftracker.misc.MATCHES_KEY
import se.umu.edmo0011.discgolftracker.dataClasses.Match
import se.umu.edmo0011.discgolftracker.misc.SharedPreferencesHelper
import se.umu.edmo0011.discgolftracker.graphs.MatchGraph
import se.umu.edmo0011.discgolftracker.graphs.OngoingMatchGraph
import se.umu.edmo0011.discgolftracker.misc.navigateAndPopUp
import java.util.Date

class MatchViewModel(private val state: SavedStateHandle) : ViewModel()
{
    private val HOLES_KEY = "match hole key"
    private val INDEX_KEY = "index key"
    private val TIME_KEY = "time key"
    private val TAB_KEY = "tab key"
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

    //Restore state from saved state handle
    init {
        val temp: LiveData<ArrayList<Hole>> = state.getLiveData(HOLES_KEY, arrayListOf())
        val h = temp.value?.toList() ?: emptyList()
        if(!h.isNullOrEmpty())
        {
            players = h[0].players
            holes = h.toMutableStateList()
            currentHoleIndex = holes.size-1
        }

        val i = state.getLiveData<Int>(INDEX_KEY)
        currentHoleIndex = i.value ?: 0

        val t = state.getLiveData<Long>(TIME_KEY)
        startTime = t.value ?: 0

        val tab = state.getLiveData<Int>(TAB_KEY)
        selectedTab = tab.value ?: 0
    }

    fun onNewMatch(navCon: NavController)
    {
        navCon.navigate(OngoingMatchGraph.route)
    }

    fun onCreateMatch(navCon: NavController, players: List<String>)
    {
        this.players = players
        holes.add(Hole(1, PAR_DEFAULT, players, List(players.size){0}))
        state.set(HOLES_KEY, arrayListOf<Hole>(*holes.toTypedArray()))

        currentHoleIndex = 0
        state.set(INDEX_KEY, currentHoleIndex)

        startTime = Date().time
        state.set(TIME_KEY, startTime)
        navCon.navigate(OngoingMatchGraph.Match.route)
        Log.w("Match", players.toString())
    }

    private fun setHole(index: Int)
    {
        if(index == holes.size) {
            holes.add(newHole(index + 1))
            state.set(HOLES_KEY, arrayListOf<Hole>(*holes.toTypedArray()))
        }

        currentHoleIndex = if(index < 0) 0 else index
        state.set(INDEX_KEY, currentHoleIndex)
    }

    fun editHole(par: Int, throws: List<Int>)
    {
        holes[currentHoleIndex] = holes[currentHoleIndex].copy(par = par, throws = throws)

        state.set(HOLES_KEY, arrayListOf<Hole>(*holes.toTypedArray()))
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
        state.set(TAB_KEY, selectedTab)
    }

    override fun onCleared() {
        super.onCleared()
        if(ended) {
            state.remove<ArrayList<Hole>>(HOLES_KEY)
            state.remove<Long>(TIME_KEY)
            state.remove<Int>(INDEX_KEY)
            state.remove<Int>(TAB_KEY)
        }

        Log.w("Match", "On cleared")
    }
}