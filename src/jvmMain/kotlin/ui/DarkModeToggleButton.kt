package ui

import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import ui.icons.DarkMode
import ui.icons.LightMode

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
            imageVector = if (darkMode) Icons.Filled.LightMode else Icons.Filled.DarkMode,
            contentDescription = if (darkMode) "Light mode" else "Dark mode",
        )
    }
}
