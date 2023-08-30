package se.umu.milo0047

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.runtime.remember

/**
 * Main composable for the Thirty game app. Creates most of the UI.
 * @param gameViewModel The view model that holds the game state and logic.
 */
@Composable
fun ThirtyApp(gameViewModel: GameViewModel) {
    val viewModel = remember { gameViewModel }
    val diceValues by viewModel::diceValues
    var diceHeld by viewModel::diceHeld
    var rollsLeft by viewModel::rollsLeft
    var rollButtonText by viewModel::rollButtonText
    var roundScore by viewModel::roundScore
    var totalScore by viewModel::totalScore
    val roundCount by viewModel::roundCount
    val selectedScoringOption by viewModel::selectedScoringOption
    var usedScoringOptions by viewModel::usedScoringOptions
    var isScoringMenuOpen by viewModel::isScoringMenuOpen

    // Game UI
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        LazyVerticalGrid( // Six dice display in 2 by 3 grid
            modifier = Modifier.fillMaxWidth(),
            columns = GridCells.Fixed(3)
        ) {
            itemsIndexed(diceValues) { index, value ->
                Dice(
                    value = value, held = diceHeld[index], onDiceClick = {
                        if (rollsLeft < 3) diceHeld =
                            diceHeld.toMutableList().apply { this[index] = !this[index] }
                    }, rollsLeft = rollsLeft
                )
            }

        }

        // Roll button
        Button(
            onClick = {
                diceValues.forEachIndexed { index, _ ->
                    if (rollsLeft <= 3 && !diceHeld[index]) {
                        viewModel.rollDice(index)
                    }
                }
                rollsLeft--
                if (rollsLeft < 3) {
                    rollButtonText = "Roll ($rollsLeft)"
                }
            }, modifier = Modifier.fillMaxWidth(), enabled = rollsLeft > 0 && roundCount < 11
        ) {
            Text(text = rollButtonText, style = TextStyle(fontSize = 20.sp))
        }

        // Scoring menu button
        ScoringMenu(onScoringOptionSelected = { selectedChoice ->
            viewModel.handleScoringOptionSelected(selectedChoice)
        },
            usedScoringOptions = usedScoringOptions,
            selectedScoringOption = selectedScoringOption,
            isScoringMenuOpen = isScoringMenuOpen,
            onMenuVisibilityChanged = { isVisible ->
                isScoringMenuOpen = isVisible
            })

        // Score button
        Button(
            onClick = {
                roundScore = viewModel.calculateScore(selectedScoringOption, diceValues)
                totalScore += roundScore
                usedScoringOptions = usedScoringOptions + selectedScoringOption
                viewModel.resetRound(selectedScoringOption)
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = selectedScoringOption != "Select scoring option" && rollsLeft == 0
        ) {
            Text(text = "Score", style = TextStyle(fontSize = 20.sp))
        }

        Text(
            text = "Round Score: $roundScore",
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier.padding(top = 8.dp)
        )

        Text(
            text = "Total Score: $totalScore",
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier.padding(top = 14.dp)
        )

        Text(
            text = "Round: $roundCount",
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier.padding(top = 8.dp)
        )

        // Shows the game result after 10 rounds.
        if (roundCount > 10) {
            GameResult(totalScore = totalScore, onRestart = {
                viewModel.restartGame()
            })
        }
    }
}