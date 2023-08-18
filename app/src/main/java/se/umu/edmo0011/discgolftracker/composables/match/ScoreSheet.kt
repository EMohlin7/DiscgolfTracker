package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.min
import androidx.compose.ui.unit.times
import se.umu.edmo0011.discgolftracker.Hole
import java.lang.Math.abs
import kotlin.math.max
import kotlin.math.min


@Composable
fun ScoreSheet(holes: List<Hole>, labelWidth: Int, height: Dp)
{
    val eighteenHeight = height / 20

    val width = (LocalConfiguration.current.screenWidthDp - labelWidth) / holes[0].players.size

    Column(verticalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxSize()) {

        PlayersRow(height = eighteenHeight, labelWidth = labelWidth.dp, width = width.dp, players = holes[0].players)
        ScoreSheetHoles(height = height - 2*eighteenHeight, scoreWidth = width.dp, labelWidth = labelWidth.dp, holes = holes)
        TotalScoreRow(height = eighteenHeight, labelWidth = labelWidth.dp, scoreWidth = width.dp, score = totScore(holes))

    }
}

fun totScore(holes: List<Hole>): List<Int>
{
    val list = mutableListOf<Int>()
    for (i in holes[0].players.indices)
    {
        var score = 0
        for(h in holes)
        {
            score += h.throws[i] - h.par
        }
        list.add(score)
    }
    return list
}

@Composable
fun scoreColor(score: Int): Color
{
    if(score == 0)
        return MaterialTheme.colorScheme.background

    //If the score is negative, the color is green else it is red
    val h = if(score < 0) 120f else 0f
    //val s = 0.15f * kotlin.math.abs(score)
    val s = min(1f, 1f)
    val l = max(1f - 0.12f * kotlin.math.abs(score), 0.45f)
    return Color.hsl(h, s, l)
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
                ScoreBox(width = labelWidth, MaterialTheme.colorScheme.background) {
                    Text(text = "Hole ${holes[i].number}")
                }
                //The scores for that hole
                for (t in holes[i].throws)
                {
                    val score = t - holes[i].par
                    ScoreBox(width = scoreWidth, color = scoreColor(score)) {
                        Text(text = scoreString(score))
                    }
                }
            }
        }
    }
}

fun scoreString(score: Int): String
{
    return if(score == 0)
        "E"
    else
        score.toString()
}

@Composable
fun TotalScoreRow(height: Dp, labelWidth: Dp, scoreWidth: Dp, score: List<Int>)
{
    Row(modifier = Modifier.height(height)) {
        ScoreBox(width = labelWidth, MaterialTheme.colorScheme.background) {
            Text(text = "Score:")
        }
        for (s in score)
        {
            ScoreBox(width = scoreWidth, scoreColor(s)) {
                Text(text = scoreString(s))
            }
        }
    }
}

@Composable
fun PlayersRow(height: Dp, labelWidth: Dp, width: Dp, players: List<String>)
{
    val color = MaterialTheme.colorScheme.background
    Row(modifier = Modifier.height(height)) {
        ScoreBox(width = labelWidth, color) {
            Text(text = "Players:")
        }

        for (p in players)
        {
            ScoreBox(width = width, color) {
                Text(text = p)
            }
        }
    }
}

@Composable
fun ScoreBox(width: Dp, color: Color, content: @Composable ()->Unit){
    val bWidth = 1.dp
    val bColor = MaterialTheme.colorScheme.surfaceTint
    Box(modifier = Modifier
            .width(width = width)
            .fillMaxHeight()
            .background(color)
            .border(width = bWidth, color = bColor),
        contentAlignment = Alignment.Center){
        content.invoke()
    }
}