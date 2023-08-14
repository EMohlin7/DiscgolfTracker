package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.times
import se.umu.edmo0011.discgolftracker.Hole



@Composable
fun ScoreSheet(holes: List<Hole>, labelWidth: Int, height: Dp)
{
    val eighteenHeight = height / 20

    val width = (LocalConfiguration.current.screenWidthDp - labelWidth) / holes[0].players.size

    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {

        PlayersRow(height = eighteenHeight, labelWidth = labelWidth.dp, width = width.dp, players = holes[0].players)
        ScoreSheetHoles(height = height - 2*eighteenHeight, scoreWidth = width.dp, labelWidth = labelWidth.dp, holes = holes)
        TotalScoreRow(height = eighteenHeight, labelWidth = labelWidth.dp, scoreWidth = width.dp, score = List(holes[0].players.size){0})

    }
}

@Composable
fun ScoreSheetHoles(height: Dp, scoreWidth: Dp, labelWidth: Dp, holes: List<Hole>)
{
    //You will always see at least 18 holes, more than that and the column becomes scrollable
    val holeHeight = max((height) / holes.size, height / 18)

    Column(modifier = Modifier
        .height(height)
        .verticalScroll(rememberScrollState())
    ) {
        for (i in holes.indices)
        {
            Row(Modifier.height(holeHeight)) {
                //Label for the hole
                ScoreBox(width = labelWidth) {
                    Text(text = "Hole ${holes[i].number}")
                }
                //The scores for that hole
                for (t in holes[i].throws)
                {
                    ScoreBox(width = scoreWidth) {
                        Text(text = "${t - holes[i].par}")
                    }
                }
            }
        }
    }
}

@Composable
fun TotalScoreRow(height: Dp, labelWidth: Dp, scoreWidth: Dp, score: List<Int>)
{
    Row(modifier = Modifier.height(height)) {
        ScoreBox(width = labelWidth) {
            Text(text = "Score:")
        }
        for (s in score)
        {
            ScoreBox(width = scoreWidth) {
                Text(text = s.toString())
            }
        }
    }
}

@Composable
fun PlayersRow(height: Dp, labelWidth: Dp, width: Dp, players: List<String>)
{
    Row(modifier = Modifier.height(height)) {
        ScoreBox(width = labelWidth) {
            Text(text = "Players:")
        }

        for (p in players)
        {
            ScoreBox(width = width) {
                Text(text = p)
            }
        }
    }
}

@Composable
fun ScoreBox(width: Dp, content: @Composable ()->Unit){
    val bWidth = 1.dp
    val bColor = MaterialTheme.colorScheme.surfaceTint
    Box(modifier = Modifier
        .width(width = width)
        .fillMaxHeight()
        .border(width = bWidth, color = bColor),
        contentAlignment = Alignment.Center){
        content.invoke()
    }
}