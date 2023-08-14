package se.umu.edmo0011.discgolftracker

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check


sealed class Screen(val route: String, val scaffoldState: ScaffoldState)
{
    object Game : Screen("game", ScaffoldState(topBar = TopBarState(R.string.app_name)))

    object History: Screen("history", ScaffoldState(topBar = TopBarState(title = R.string.title_history_screen)))
}

class MatchGraph
{
    companion object
    {
        const val route: String = "match graph"
    }
    object NewMatch : Screen("new match", ScaffoldState(topBar = TopBarState(title = R.string.New_match)))
    object SetupMatch : Screen("Setup match", ScaffoldState(topBar = TopBarState(title = R.string.title_setup_match)))
    object Match : Screen("match", ScaffoldState(topBar = TopBarState(title = R.string.title_match, actionIcons = List(1){Icons.Default.Check})))
}

class HistoryGraph
{
    companion object
    {
        const val route: String = "history graph"
    }
    object MatchHistory : Screen("match history", ScaffoldState(topBar = TopBarState(title = R.string.title_history_screen)))
    object Match : Screen("old match score", ScaffoldState(topBar = TopBarState(title = R.string.title_history_screen)))
}

class MeasuredThrowsGraph
{
    companion object
    {
        const val route: String = "measured throws graph"
    }

    object Throws: Screen("measured throws",
        ScaffoldState(
            fab = FabState(Icons.Default.Add),
            topBar = TopBarState(R.string.title_measured_throws_screen)
        )
    )
}

class MeasureGraph()
{
    companion object
    {
        const val route: String = "measure graph"
    }

    object StartMeasure: Screen("start measure", ScaffoldState())
    object Measuring : Screen("measuring", ScaffoldState())
    object Save : Screen("save measurement", ScaffoldState(topBar = TopBarState(title = R.string.title_save_throw)))
}


