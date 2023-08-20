package se.umu.edmo0011.discgolftracker.composables.measuring

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.MeasureGraph
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.ScaffoldState
import se.umu.edmo0011.discgolftracker.SharedPreferencesHelper
import se.umu.edmo0011.discgolftracker.THROWS_KEY
import se.umu.edmo0011.discgolftracker.Throw
import se.umu.edmo0011.discgolftracker.composables.general.AppList
import se.umu.edmo0011.discgolftracker.composables.general.AppListItem
import se.umu.edmo0011.discgolftracker.formatDateMs
import se.umu.edmo0011.discgolftracker.formatDurationMs
import java.lang.Math.min


@Composable
fun MeasuredThrowsScreen(navCon: NavController, state: ScaffoldState)
{
    state.fab?.fabAction = {
        navCon.navigate(MeasureGraph.route)
    }

    val l = SharedPreferencesHelper.getList<Throw>(navCon.context, THROWS_KEY).sortedByDescending { it.distance }
    val list = remember { mutableStateListOf(*l.toTypedArray()) }
    fun onDelete(t: Throw)
    {
        list.remove(t)
        SharedPreferencesHelper.saveList<Throw>(navCon.context, list, THROWS_KEY)
    }

    val content = mutableListOf<@Composable ()->Unit>()
    for (i in list) {
        val date = formatDateMs(i.dateMs)
        val labels = listOf(
            stringResource(id = R.string.Date),
            stringResource(id = R.string.Disc),
            stringResource(id = R.string.Course),
            stringResource(id = R.string.Hole)
        )

        val distance = stringResource(id = R.string.Distance)+": ${i.distance}"
        val items = listOf(date, i.disc, i.course, i.hole)
        
        content.add {
            AppListItem(
                title = distance,
                items = items,
                labels = labels,
                itemsPerRow = 2,
                onDelete = { onDelete(i) })
            Spacer(Modifier.size(2.dp))
        }
    }
    
    AppList(content = content)
}



@Preview
@Composable
fun prev()
{
   // AppListItem(title = "Preview", items = listOf("asd", "asda", "asdasda", "asdd"), itemsPerRow = 2, {})
}