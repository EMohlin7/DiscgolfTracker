package se.umu.edmo0011.discgolftracker

import androidx.annotation.DrawableRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close


sealed class Screen(val route: String, val scaffoldState: ScaffoldState)
{

}



class MatchGraph
{
    companion object
    {
        const val route: String = "match graph"
    }
    object NewMatch : Screen("new match", ScaffoldState(topBar = TopBarState(title = R.string.New_match)))

}

class OngoingMatchGraph
{
    companion object
    {
        const val route: String = "ongoing match graph"
    }
    object SetupMatch : Screen("Setup match", ScaffoldState(
        topBar = TopBarState(title = R.string.title_setup_match, topNavIcon = Icons.Default.ArrowBack)))
    object Match : Screen("match", ScaffoldState(topBar = TopBarState(
        title = R.string.title_match,
        topNavIcon = Icons.Default.Close,
        actionIcons = List(1){Icons.Default.Check})))
}

class HistoryGraph
{
    companion object
    {
        const val route: String = "history graph"
    }
    object MatchHistory : Screen("match history", ScaffoldState(topBar = TopBarState(title = R.string.title_history_screen)))
    object Match : Screen("old match score", ScaffoldState(
        topBar = TopBarState(title = R.string.title_history_screen, topNavIcon = Icons.Default.ArrowBack)))
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

    object StartMeasure: Screen("start measure", ScaffoldState(
        topBar = TopBarState(title = R.string.title_start_measure_screen, topNavIcon = Icons.Default.ArrowBack)
    ))
    object Measuring : Screen("measuring", ScaffoldState(
        topBar = TopBarState(title = R.string.title_measuring_screen, topNavIcon = Icons.Default.ArrowBack)
    ))
    object Save : Screen("save measurement", ScaffoldState(
        topBar = TopBarState(title = R.string.title_save_throw, topNavIcon = Icons.Default.ArrowBack)))
}


