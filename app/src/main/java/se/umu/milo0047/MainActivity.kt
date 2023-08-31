package se.umu.milo0047

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels

/**
 * This is a dice game called Thirty.
 * This is the main activity and starting point of the app.
 */
class MainActivity : ComponentActivity() {
    private val gameViewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            if (isGameEnded()) {
                GameResult(gameViewModel.totalScore, gameViewModel.roundScores) {
                    gameViewModel.restartGame()
                }
            } else {
                ThirtyApp(gameViewModel)
            }
        }
    }

    /**
     * Called when the activity is resumed.
     */
    override fun onResume() {
        super.onResume()
        gameViewModel.loadGameState(this)
    }

    /**
     * Called when the activity is paused.
     */
    override fun onPause() {
        super.onPause()
        gameViewModel.saveGameState(this)
    }

    /**
     * Checks if the game has ended.
     * @return `true` if the game has ended, `false` otherwise.
     */
    private fun isGameEnded(): Boolean {
        return gameViewModel.roundCount > 10
    }
}
