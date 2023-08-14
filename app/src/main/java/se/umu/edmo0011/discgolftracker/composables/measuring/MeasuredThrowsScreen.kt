package se.umu.edmo0011.discgolftracker.composables.measuring

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import se.umu.edmo0011.discgolftracker.MeasureGraph
import se.umu.edmo0011.discgolftracker.ScaffoldState
import se.umu.edmo0011.discgolftracker.SharedPreferencesHelper
import se.umu.edmo0011.discgolftracker.THROWS_KEY
import se.umu.edmo0011.discgolftracker.Throw


@Composable
fun MeasuredThrowsScreen(navCon: NavController, state: ScaffoldState)
{
    state.fab?.fabAction = {
        navCon.navigate(MeasureGraph.route)
    }

    val list = SharedPreferencesHelper.getList<Throw>(navCon.context, THROWS_KEY)
    LazyColumn(
        modifier = Modifier.fillMaxSize().scrollable(ScrollableState{0f}, Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top) {
        for (i in list) {
            val items = listOf("Disc: ${i.disc}","Course: ${i.course}","Hole: ${i.hole}")
            this.item {
                AppListItem(title = "Distance: ${i.distance}", items = items, itemsPerRow = 2)
                Spacer(Modifier.size(2.dp))
            }

        }
    }
}

@Composable
fun AppListItem(title: String, items: List<String>, itemsPerRow: Int)
{
    Card(Modifier.defaultMinSize(minHeight = 100.dp)){
        Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement =  Arrangement.Center) {
            Text(text = title)
            AppListItemItem(items = items, itemsPerRow = itemsPerRow)
        }
    }
}

@Composable
fun AppListItemItem(items: List<String>, itemsPerRow: Int)
{
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        for (i in 0 until items.size)
        {
            if(i % itemsPerRow == 0)
                Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                    for(j in 0 until itemsPerRow)
                    {
                        Text(text = items[j])
                    }
                }

        }
    }
}
