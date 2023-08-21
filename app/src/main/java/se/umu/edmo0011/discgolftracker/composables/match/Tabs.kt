package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun TabBar(selected: Int, height: Dp, width: Dp, titles: List<@Composable ()->Unit>, onSelected: List<(Int)->Unit>)
{
    Box (Modifier.width(width).height(height)){
        Row (
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically){
            for(i in titles.indices)
            {
                TabItem(selected = i == selected, width = width/titles.size,
                    title = titles[i]) {
                    onSelected[i].invoke(i)
                }
            }
        }
    }
}

@Composable
fun TabItem(selected: Boolean, width: Dp, title: @Composable ()->Unit, onSelected: ()->Unit)
{
    val color = if(selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.background
    Box(modifier = Modifier
        .fillMaxHeight()
        .width(width)
        .background(color)
        .clickable { if (!selected) onSelected.invoke() },
        contentAlignment = Alignment.Center) {
        title.invoke()
    }
}

@Composable
@Preview
fun TabBarPreview()
{
    var selected by remember {
        mutableStateOf(0)
    }
    val l = List<@Composable ()->Unit>(2){ { Text(text = "$it") }}
    TabBar(selected = selected, height = 60.dp, width = LocalConfiguration.current.screenWidthDp.dp,
        titles = l, onSelected = List<(Int)->Unit>(2){{selected = it}})
}