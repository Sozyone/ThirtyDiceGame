package se.umu.milo0047

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Composable that creates end game results.
 * Creates UI elements that shows that the game is finished.
 * Displays the round scores for each options, total score, and a restart button.
 * @param totalScore The total score achieved in the game.
 * @param roundScores A map containing round scores for different scoring options.
 * @param onRestart A callback function to restart the game.
 */
@Composable
fun GameResult(totalScore: Int, roundScores: Map<String, Int>, onRestart: () -> Unit) {
    val sortedRoundScores = roundScores.toSortedMap()

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Finished", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        for ((option, score) in sortedRoundScores) {
            Text(text = "Score for option: $option is: $score", fontSize = 20.sp)
        }
        Spacer(modifier = Modifier.height(16.dp))

        Text(text = "Total Score: $totalScore", fontSize = 24.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = { onRestart() }, modifier = Modifier.fillMaxWidth()
        ) {
            Text(text = "Restart", style = TextStyle(fontSize = 20.sp))
        }
    }
}

