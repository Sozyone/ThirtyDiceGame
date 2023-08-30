package se.umu.milo0047

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp

/**
 * Composable that creates a clickable box that represents a dice.
 * It can be clicked to hold / un-hold the dice.
 * It is clickable if there are less than 3 rolls left.
 * @param value The current value of the dice.
 * @param held A boolean indicating whether the dice is held or not.
 * @param onDiceClick A callback function that handles dice click events.
 * @param rollsLeft The number of rolls left in the current round.
 */
@Composable
fun Dice(value: Int, held: Boolean, onDiceClick: (Boolean) -> Unit, rollsLeft: Int) {
    Box(modifier = Modifier
        .padding(2.dp)
        .aspectRatio(1f)
        .clickable(enabled = rollsLeft < 3) {
            onDiceClick(!held) // Toggle hold status when clicked
        }
        .background(if (held) Color.Gray else Color.White), // Set bg color based on hold status
        contentAlignment = Alignment.Center) {
        Image( // Show correct dice image inside the box
            painter = painterResource(id = getDiceImage(value)),
            contentDescription = "Dice with $value",
            modifier = Modifier.size(90.dp)
        )
    }
}

/**
 * Utility function to get a dice image based on its value.
 * @param value The value of the dice.
 * @return The resource ID of the corresponding dice image.
 */
fun getDiceImage(value: Int): Int {
    return when (value) {
        1 -> R.drawable.white1
        2 -> R.drawable.white2
        3 -> R.drawable.white3
        4 -> R.drawable.white4
        5 -> R.drawable.white5
        else -> R.drawable.white6
    }
}
