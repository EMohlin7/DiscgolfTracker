package se.umu.edmo0011.discgolftracker.composables.general

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.ScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import se.umu.edmo0011.discgolftracker.R

@Composable
fun AppList(content: List<@Composable ()->Unit>)
{
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .scrollable(ScrollableState { 0f }, Orientation.Vertical),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        for(i in content.indices)
        {
            this.item {
                Column(Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
                    content[i].invoke()
                    //Don't add a divider to the last element
                    if(i < content.size - 1)
                        Divider(Modifier.fillMaxWidth(0.93f))
                }
            }
        }
    }
}



@Composable
fun AppListItem(title: String, items: List<String>, labels: List<String>, itemsPerRow: Int, onDelete: ()->Unit)
{
    Box(Modifier.defaultMinSize(Dp.Unspecified, 60.dp), contentAlignment = Alignment.CenterEnd){
        AppListItemMenu(text = listOf(stringResource(id = R.string.Delete)), actions = listOf(onDelete))

        Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(modifier = Modifier.padding(top = 5.dp),text = title, style = MaterialTheme.typography.titleLarge)

            Column(Modifier.fillMaxSize().padding(15.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
                AppListItemContent(items = items, labels, itemsPerRow = itemsPerRow)
            }
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
fun AppListItemContent(items: List<String>, labels: List<String>, itemsPerRow: Int)
{
    @Composable
    fun Content(index: Int): Int
    {
        var ret = 0
        for(i in index until Math.min(items.size, index + itemsPerRow))
        {
            Column(modifier = Modifier
                .fillMaxWidth(1f / (itemsPerRow - i + index))
                .padding(start = 3.dp)) {
                Text(text = labels[i]+": ", style = MaterialTheme.typography.titleLarge)
                Text(text = items[i])
            }
            ++ret
        }
        return ret
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        var i = 0
        while (i < items.size)
        {
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = 5.dp),verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
                i += Content(i)
            }
        }
    }
}