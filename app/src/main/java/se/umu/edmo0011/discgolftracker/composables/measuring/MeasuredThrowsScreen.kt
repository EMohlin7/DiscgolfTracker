package se.umu.edmo0011.discgolftracker.composables.measuring

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.dataClasses.ScaffoldState
import se.umu.edmo0011.discgolftracker.misc.SharedPreferencesHelper
import se.umu.edmo0011.discgolftracker.misc.THROWS_KEY
import se.umu.edmo0011.discgolftracker.dataClasses.Throw
import se.umu.edmo0011.discgolftracker.composables.general.AppList
import se.umu.edmo0011.discgolftracker.composables.general.AppListItem
import se.umu.edmo0011.discgolftracker.misc.formatDateMs
import se.umu.edmo0011.discgolftracker.graphs.MeasureGraph


@Composable
fun MeasuredThrowsScreen(navCon: NavController, state: ScaffoldState)
{
    state.fab?.fabAction = {
        navCon.navigate(MeasureGraph.route)
    }

    val l = SharedPreferencesHelper.getList<Throw>(navCon.context, THROWS_KEY).sortedByDescending { it.distance }
    val throws = remember { mutableStateListOf(*l.toTypedArray()) }

    val labels = listOf(
        stringResource(id = R.string.Date),
        stringResource(id = R.string.Disc),
        stringResource(id = R.string.Course),
        stringResource(id = R.string.Hole)
    )

    val content = throwListContent(throws = throws, labels = labels){
        throws.remove(it)
        SharedPreferencesHelper.saveList<Throw>(navCon.context, throws, THROWS_KEY)
    }
    
    AppList(content = content)
}

@Composable
fun throwListContent(throws: List<Throw>, labels: List<String>, onDelete: (Throw)->Unit): List<@Composable ()->Unit>
{
    val content = mutableListOf<@Composable ()->Unit>()
    for (t in throws) {
        val date = formatDateMs(t.dateMs)

        val distance = stringResource(id = R.string.Distance)+": ${t.distance}"
        val items = listOf(date, t.disc, t.course, t.hole)

        content.add {
            AppListItem(
                title = distance,
                items = items,
                labels = labels,
                itemsPerRow = 2,
                onDelete = { onDelete.invoke(t) })
            Spacer(Modifier.size(2.dp))
        }
    }

    return content
}



@Preview
@Composable
fun prev()
{
   // AppListItem(title = "Preview", items = listOf("asd", "asda", "asdasda", "asdd"), itemsPerRow = 2, {})
}