/*
 * Copyright 2022 Christian Burns
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.unit.dp
import core.AppManager
import core.BrowserType
import kotlinx.coroutines.launch

/**
 * App is the top-level UI component which acts as a bridge between the
 * user interface and the core functionality of this application.
 */
@Composable
@Preview
fun App() {
    // 5:00pm is usually the first timeslot that gets to enjoy registration
    val initialTimeOfDay = TimeOfDay(hour = 5, minute = 0, afternoon = true)
    val composableScope = rememberCoroutineScope()
    val initialDarkMode = isSystemInDarkTheme()
    val browserType = BrowserType.CHROME
    var timeOfDay by remember { mutableStateOf(initialTimeOfDay) }
    var darkMode by remember { mutableStateOf(initialDarkMode) }

    // Preloads the browser web drivers now so that we avoid a long delay the
    // first time a user clicks the launch button.
    composableScope.launch {
        AppManager.preloadBrowserDrivers(browserType)
    }

    // Material design aesthetic (•̀ᴗ•́)━☆.*･｡ﾟ★
    MaterialTheme(
        colors = if (darkMode) darkColors() else lightColors()
    ) {
        // This surface applies the dark/light mode theme to the background of the window
        Surface(
            color = MaterialTheme.colors.background,
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(6.dp))

                // Text describing the time picker and dark-mode toggle button
                Header(
                    title = "Course Registration Time",
                    onClick = { darkMode = !darkMode }
                )
                Spacer(modifier = Modifier.height(4.dp))

                // Time picker to select what time of day registration occurs
                TimePicker(
                    timeOfDay = timeOfDay,
                    selectedTime = { timeOfDay = it }
                )
                Spacer(modifier = Modifier.height(10.dp))

                // Buttons at the bottom of the application to launch or cancel browser instances
                // Coroutines are used here to prevent hanging the UI thread while performing the actions
                ActionButtons(
                    onStartRequest = {
                        composableScope.launch {
                            // Launches a new browser window
                            AppManager.newBrowserInstance(browserType, timeOfDay.toDate())
                        }
                    },
                    onCancelRequest = {
                        composableScope.launch {
                            // Closes out of all browser windows
                            AppManager.quit()
                        }
                    }
                )
            }
        }
    }
}

/**
 * Header containing text and a dark mode toggle button.
 *
 * @param title The header text to display.
 * @param onClick The lambda to be invoked when the dark mode toggle button is pressed.
 */
@Composable
@Suppress("SameParameterValue")
private fun Header(
    title: String,
    onClick: () -> Unit
) = Row(
    verticalAlignment = Alignment.CenterVertically,
    horizontalArrangement = Arrangement.Center
) {
    Text(
        text = title,
        style = MaterialTheme.typography.h5
    )
    Spacer(modifier = Modifier.width(8.dp))
    DarkModeToggleButton(onClick = onClick)
}

/**
 * Button group used to launch or cancel an action.
 *
 * @param onStartRequest The lambda to be invoked when the start button is pressed.
 * @param onCancelRequest The lambda to be invoked when the cancel button is pressed.
 * @param startEnabled Controls the enabled state. When `false`, the start button will not
 * be clickable.
 * @param cancelEnabled Controls the enabled state. When `false`, the cancel button will not
 * be clickable.
 */
@Composable
private fun ActionButtons(
    onStartRequest: () -> Unit,
    onCancelRequest: () -> Unit,
    startEnabled: Boolean = true,
    cancelEnabled: Boolean = true
) = Row(verticalAlignment = Alignment.CenterVertically) {

    // Mutes the colors of the button and content within the button
    val cancelButtonColors = ButtonDefaults.buttonColors(
        backgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
            .compositeOver(MaterialTheme.colors.surface)
    )

    Button(
        onClick = onCancelRequest,
        enabled = cancelEnabled,
        colors = cancelButtonColors,
        content = { Text("Cancel") }
    )

    Spacer(modifier = Modifier.width(16.dp))

    Button(
        onClick = onStartRequest,
        enabled = startEnabled,
        content = { Text("Launch") }
    )
}
