package ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun DarkModeToggleButton(
    onClick: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val darkMode = !MaterialTheme.colors.isLight

    IconButton(
        onClick = { onClick(!darkMode) },
        modifier = modifier,
        enabled = enabled,
    ) {
        Icon(
            imageVector = if (darkMode) Icons.Filled.Star else Icons.Filled.Star,
            contentDescription = if (darkMode) "Light mode" else "Dark mode",
        )
    }
}
