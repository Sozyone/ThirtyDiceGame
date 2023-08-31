package se.umu.milo0047

import android.content.Context
import android.content.SharedPreferences
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

/**
 * ViewModel class for managing the game state and logic.
 * Implements the GameLogicProvider interface to provide data and methods.
 */
class GameViewModel : ViewModel(), GameLogicProvider {
    private lateinit var sharedPreferences: SharedPreferences
    override var diceValues by mutableStateOf(listOf(1, 2, 3, 4, 5, 6))
    override var diceHeld by mutableStateOf(listOf(false, false, false, false, false, false))
    override var rollsLeft by mutableStateOf(3)
    override var rollButtonText by mutableStateOf("Play")
    override var roundScores: Map<String, Int> by mutableStateOf(emptyMap())
    override var totalScore by mutableStateOf(0)
    override var roundCount by mutableStateOf(1)
    override var selectedScoringOption by mutableStateOf("Select scoring option")
    override var usedScoringOptions by mutableStateOf(setOf<String>())
    override var isScoringMenuOpen by mutableStateOf(false)

    /**
     * Utility function to roll a dice.
     * @param index The index of the dice to roll.
     */
    fun rollDice(index: Int) {
        diceValues = diceValues.toMutableList().apply {
            this[index] = (1..6).random()
        }
    }

    /**
     * Utility function to reset a round.
     * @param choice The choice made for the scoring option.
     */
    fun resetRound(choice: String) {
        diceValues = listOf(1, 2, 3, 4, 5, 6)
        diceHeld = List(6) { false }
        rollsLeft = 3
        rollButtonText = "Play"
        roundCount++
        selectedScoringOption = "Select scoring option"
        isScoringMenuOpen = false
        usedScoringOptions = usedScoringOptions + choice
    }

    /**
     * Utility function to restart the game.
     */
    fun restartGame() {
        diceValues = listOf(1, 2, 3, 4, 5, 6)
        diceHeld = listOf(false, false, false, false, false, false)
        rollsLeft = 3
        rollButtonText = "Play"
        totalScore = 0
        roundCount = 1
        selectedScoringOption = "Select scoring option"
        usedScoringOptions = emptySet()
        isScoringMenuOpen = false
    }

    /**
     * Calculate the score for a specific scoring option.
     * @param choice The selected scoring option.
     * @param diceValues The current values of the dice.
     * @return The calculated score.
     */
    fun calculateScore(choice: String, diceValues: List<Int>): Int {
        return when (choice) {
            "Low" -> diceValues.filter { it <= 3 }.sum()
            else -> {
                val targetSum = choice.toIntOrNull() ?: 0
                var score = 0

                val combinations = mutableListOf<List<Int>>()
                for (i in diceValues.indices) {
                    for (j in i until diceValues.size) {
                        combinations.add(diceValues.subList(i, j + 1))
                    }
                }

                for (combination in combinations) {
                    val sum = combination.sum()
                    if (sum == targetSum) {
                        score += sum
                    }
                }
                score
            }
        }
    }

    /**
     * Utility function to update the selected choice in the scoring menu
     * when a scoring option is selected. Then closes the menu. It is passed as
     * a parameter to the ScoringMenu composable for its onScoringOptionSelected event handling.
     * @param choice The selected scoring option.
     */
    fun handleScoringOptionSelected(choice: String) {
        selectedScoringOption = choice
        isScoringMenuOpen = false
    }

    /**
     * Save the game state to SharedPreferences.
     * @param context The context used to access SharedPreferences.
     */
    fun saveGameState(context: Context) {
        val prefs = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
        with(prefs.edit()) {
            putInt("roundCount", roundCount)
            putInt("totalScore", totalScore)
            putInt("rollsLeft", rollsLeft)
            putBoolean("isScoringMenuOpen", isScoringMenuOpen)
            putString("selectedScoringOption", selectedScoringOption)
            putStringSet("usedScoringOptions", usedScoringOptions)
            roundScores.forEach { (key, value) ->
                putInt("roundScore_$key", value)
            }
            putInt("dice1Value", diceValues[0])
            putInt("dice2Value", diceValues[1])
            putInt("dice3Value", diceValues[2])
            putInt("dice4Value", diceValues[3])
            putInt("dice5Value", diceValues[4])
            putInt("dice6Value", diceValues[5])
            putBoolean("dice1Held", diceHeld[0])
            putBoolean("dice2Held", diceHeld[1])
            putBoolean("dice3Held", diceHeld[2])
            putBoolean("dice4Held", diceHeld[3])
            putBoolean("dice5Held", diceHeld[4])
            putBoolean("dice6Held", diceHeld[5])
            apply()
        }
    }

    /**
     * Load the game state from SharedPreferences.
     * @param context The context used to access SharedPreferences.
     */
    fun loadGameState(context: Context) {
        val prefs = context.getSharedPreferences("GamePrefs", Context.MODE_PRIVATE)
        roundCount = prefs.getInt("roundCount", 1)
        totalScore = prefs.getInt("totalScore", 0)
        rollsLeft = prefs.getInt("rollsLeft", 3)
        isScoringMenuOpen = prefs.getBoolean("isScoringMenuOpen", false)
        selectedScoringOption = prefs.getString("selectedScoringOption", "Select scoring option")
            ?: "Select scoring option"
        usedScoringOptions = prefs.getStringSet("usedScoringOptions", emptySet()) ?: emptySet()
        val loadedRoundScores = mutableMapOf<String, Int>()
        prefs.all.keys.forEach { key ->
            if (key.startsWith("roundScore_")) {
                val scoringOption = key.substringAfter("roundScore_")
                val score = prefs.getInt(key, 0)
                loadedRoundScores[scoringOption] = score
            }
        }
        roundScores = loadedRoundScores.toMap()
        diceValues = listOf(
            prefs.getInt("dice1Value", 1),
            prefs.getInt("dice2Value", 2),
            prefs.getInt("dice3Value", 3),
            prefs.getInt("dice4Value", 4),
            prefs.getInt("dice5Value", 5),
            prefs.getInt("dice6Value", 6)
        )
        diceHeld = listOf(
            prefs.getBoolean("dice1Held", false),
            prefs.getBoolean("dice2Held", false),
            prefs.getBoolean("dice3Held", false),
            prefs.getBoolean("dice4Held", false),
            prefs.getBoolean("dice5Held", false),
            prefs.getBoolean("dice6Held", false)
        )
    }
}