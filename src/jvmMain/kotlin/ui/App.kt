package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.text.font.FontWeight
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
            LazyColumn(contentPadding = PaddingValues(16.dp)) {
                item {
                    Column(
                        verticalArrangement = Arrangement.SpaceAround,
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxSize(),
                    ) {
                        Text(
                            text = "GCC Course Signup",
                            style = MaterialTheme.typography.h4,
                            fontWeight = FontWeight.Light,
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = """
                                Tool to register for Grove City College courses as soon as course registration opens.
                            """.trimIndent(),
                            style = MaterialTheme.typography.subtitle1,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Divider()
                        Spacer(modifier = Modifier.height(8.dp))
                        DarkModeToggleButton(onClick = { darkMode = it })
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
                            modifier = Modifier.fillMaxHeight(),
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
    }
}
