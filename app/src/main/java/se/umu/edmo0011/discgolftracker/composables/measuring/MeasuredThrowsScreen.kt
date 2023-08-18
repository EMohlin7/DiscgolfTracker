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
import se.umu.edmo0011.discgolftracker.formatDateMs
import se.umu.edmo0011.discgolftracker.formatDurationMs
import java.lang.Math.min


@Composable
fun MeasuredThrowsScreen(navCon: NavController, state: ScaffoldState)
{
    state.fab?.fabAction = {
        navCon.navigate(MeasureGraph.route)
    }

    val l = SharedPreferencesHelper.getList<Throw>(navCon.context, THROWS_KEY)
    val list = remember { mutableStateListOf(*l.toTypedArray()) }
    fun onDelete(t: Throw)
    {
        list.remove(t)
        SharedPreferencesHelper.saveList<Throw>(navCon.context, list, THROWS_KEY)
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(ScrollableState { 0f }, Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
        for (i in list) {
            val d = formatDateMs(i.dateMs)
            val items = listOf("Date: $d","Disc: ${i.disc}","Course: ${i.course}","Hole: ${i.hole}")
            this.item {
                AppListItem(title = "Distance: ${i.distance}", items = items, itemsPerRow = 2, onDelete = {onDelete(i)})
                Spacer(Modifier.size(2.dp))
            }
        }
    }
}

@Composable
fun AppListItem(title: String, items: List<String>, itemsPerRow: Int, onDelete: ()->Unit)
{
    Card(Modifier.defaultMinSize(Dp.Unspecified, 60.dp)){
        Row(modifier = Modifier
            .fillMaxSize()
            .padding(10.dp, 2.dp),
                horizontalArrangement = Arrangement.End,
                verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.fillMaxWidth(0.85f).fillMaxHeight(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                AppListItemItem(items = items, itemsPerRow = itemsPerRow)
            }

            AppListItemMenu(text = listOf(stringResource(id = R.string.Delete)), actions = listOf(onDelete))
        }
    }
}

@Composable
fun AppListItemMenu(text: List<String>, actions: List<()->Unit>)
{
    var show by rememberSaveable { mutableStateOf(false) }
    Box {
        IconButton(onClick = { show = true }) {
            Icon(Icons.Default.MoreVert, null)
        }
        DropdownMenu(expanded = show, onDismissRequest = { show = false }) {
            for(i in text.indices)
                DropdownMenuItem(text = { Text(text = text[i]) }, onClick = { show = false; actions[i].invoke() })
        }
    }
}

@Composable
fun AppListItemItem(items: List<String>, itemsPerRow: Int)
{
    @Composable
    fun Content(index: Int): Int
    {
        var ret = 0
        for(i in index until min(items.size, index + itemsPerRow))
        {
            Text(text = items[i], modifier = Modifier.fillMaxWidth(1f/(itemsPerRow-i+index)))
            ++ret
        }
        return ret
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var i = 0
        while (i < items.size)
        {
            Row(Modifier.fillMaxWidth().padding(bottom = 5.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                i += Content(i)
            }
        }
    }
}

@Preview
@Composable
fun prev()
{
    AppListItem(title = "Preview", items = listOf("asd", "asda", "asdasda", "asdd"), itemsPerRow = 2, {})
}