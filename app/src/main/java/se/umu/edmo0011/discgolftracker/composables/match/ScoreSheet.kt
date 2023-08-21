package se.umu.edmo0011.discgolftracker.composables.match

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.times
import se.umu.edmo0011.discgolftracker.dataClasses.Hole
import se.umu.edmo0011.discgolftracker.R
import se.umu.edmo0011.discgolftracker.misc.scoreString
import se.umu.edmo0011.discgolftracker.misc.totScore
import java.lang.Math.abs
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

//Shows each players score on each hole
@Composable
fun ScoreSheetHoles(height: Dp, scoreWidth: Dp, labelWidth: Dp, holes: List<Hole>)
{
    //You will always see at least 18 holes, more than that and the column becomes scrollable
    val holeHeight = max((height) / holes.size, height / 18)

    Column(modifier = Modifier
        .height(height)
        .verticalScroll(rememberScrollState(), enabled = holes.size > 18)
    ) {
        for (i in holes.indices)
        {
            Row(Modifier.height(holeHeight)) {
                //Label for the hole
                ScoreBox(width = labelWidth, MaterialTheme.colorScheme.background) {
                    Column(Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center) {
                        Text(text = stringResource(id = R.string.Hole)+" ${holes[i].number}")
                    }
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


//The bottom row of the score sheet. Shows the players total score
@Composable
fun TotalScoreRow(height: Dp, labelWidth: Dp, scoreWidth: Dp, score: List<Int>)
{
    Row(modifier = Modifier.height(height)) {
        ScoreBox(width = labelWidth, MaterialTheme.colorScheme.background) {
            Text(text = stringResource(id = R.string.Score))
        }
        for (s in score)
        {
            ScoreBox(width = scoreWidth, scoreColor(s)) {
                Text(text = scoreString(s))
            }
        }
    }
}

//The top most row of the score sheet. Shows the players names
@Composable
fun PlayersRow(height: Dp, labelWidth: Dp, width: Dp, players: List<String>)
{
    val color = MaterialTheme.colorScheme.background
    Row(modifier = Modifier.height(height)) {
        ScoreBox(width = labelWidth, color) {
            Text(text = stringResource(id = R.string.Players))
        }

        for (p in players)
        {
            ScoreBox(width = width, color) {
                Text(text = p, textAlign = TextAlign.Center)
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


@Composable
fun scoreColor(score: Int): Color
{
    if(score == 0)
        return MaterialTheme.colorScheme.background

    val color = if(score < 0)
    //Green
        0xff shl 8
    else
    //Red
        0xff shl 16

    //Alpha
    val a = min((50 * abs(score)) , 0xff) shl 24

    return Color(color or a)
}



