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
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Composable
@Preview
fun App(appManager: AppManager) {
    var textFieldValue by remember { mutableStateOf("") }
    val composableScope = rememberCoroutineScope()

    val initialDarkMode = isSystemInDarkTheme()
    var darkMode by remember { mutableStateOf(initialDarkMode) }

    MaterialTheme(
        colors = if (darkMode) darkColors() else lightColors(),
    ) {
        Surface(color = MaterialTheme.colors.background) {
            Spacer(modifier = Modifier.fillMaxSize())
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxSize(),
            ) {
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "Course Registration Time",
                        style = MaterialTheme.typography.h5
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    DarkModeToggleButton(onClick = { darkMode = !darkMode })
                }

                TimePicker(
                    defaultHour = 17,
                    defaultMinute = 0,
                    selectedTime = {
                        val localDateTime = LocalDateTime.now()
                            .withHour(it.hour)
                            .withMinute(it.minute)
                            .withSecond(0)
                            .withNano(0)
                        val zoneId = ZoneId.systemDefault()
                        val epoch = localDateTime.atZone(zoneId).toEpochSecond()
                        textFieldValue = epoch.toString()
                    }
                )
                Spacer(modifier = Modifier.height(16.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Button(
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                                .compositeOver(MaterialTheme.colors.surface),
                        ),
                        onClick = {
                            composableScope.launch {
                                appManager.quit()
                            }
                        },
                    ) {
                        Text(text = "Cancel")
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Button(
                        onClick = {
                            composableScope.launch {
                                val unixTimestamp = textFieldValue.toLong()
                                val dateTime = Date.from(Instant.ofEpochSecond(unixTimestamp))
                                appManager.newBrowserInstance(dateTime)
                            }
                        },
                        enabled = textFieldValue.isNotEmpty(),
                    ) {
                        Text(text = "Launch")
                    }
                }
            }
        }
    }
}
