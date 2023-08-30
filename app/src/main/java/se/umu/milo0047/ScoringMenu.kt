package se.umu.milo0047

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

/**
 * Composable that creates the scoring menu.
 * It is a button that shows a dropdown menu when clicked.
 * It shows all available, unused scoring options.
 * @param onScoringOptionSelected A callback function that handles what happens when an option is selected.
 * @param usedScoringOptions The set of scoring options that have been used in the game.
 * @param selectedScoringOption The currently selected scoring option.
 * @param isScoringMenuOpen A boolean indicating whether the scoring menu is open.
 * @param onMenuVisibilityChanged A callback function to change the visibility of the scoring menu.
 */
@Composable
fun ScoringMenu(
    onScoringOptionSelected: (String) -> Unit,
    usedScoringOptions: Set<String>,
    selectedScoringOption: String,
    isScoringMenuOpen: Boolean,
    onMenuVisibilityChanged: (Boolean) -> Unit
) {
    val remainingOptions = remember(usedScoringOptions) {
        listOf(
            "Low", "4", "5", "6", "7", "8", "9", "10", "11", "12"
        ).filterNot { it in usedScoringOptions }
    }

    Button(
        onClick = { onMenuVisibilityChanged(true) },
        enabled = remainingOptions.isNotEmpty(),
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text = selectedScoringOption.takeIf { it.isNotEmpty() } ?: "Select scoring option",
            style = TextStyle(fontSize = 20.sp))
    }

    DropdownMenu(
        expanded = isScoringMenuOpen,
        onDismissRequest = { onMenuVisibilityChanged(false) },
        modifier = Modifier.fillMaxWidth()
    ) {
        remainingOptions.forEach { choice ->
            DropdownMenuItem(text = { Text(text = choice, style = TextStyle(fontSize = 20.sp)) },
                onClick = {
                    onScoringOptionSelected(choice)
                    onMenuVisibilityChanged(false)
                })
        }
    }
}