package ui

import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import core.AppManager
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

@Composable
@Preview
fun App(appManager: AppManager) {
    var textFieldValue by remember { mutableStateOf("") }

    MaterialTheme {
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            item {
                Column {
                    Text(
                        text = "GCC Course Signup",
                        style = MaterialTheme.typography.h3,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = """
                            Tool to register for Grove City College courses as soon as course registration opens.
                        """.trimIndent(),
                        style = MaterialTheme.typography.subtitle1,
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    TimePicker(
                        defaultHour = 17,
                        defaultMinute = 0,
                    ) {
                        val localDateTime = LocalDateTime.now()
                            .withHour(it.hour)
                            .withMinute(it.minute)
                            .withSecond(0)
                            .withNano(0)
                        val zoneId = ZoneId.systemDefault()
                        val epoch = localDateTime.atZone(zoneId).toEpochSecond()
                        textFieldValue = epoch.toString()
                    }
                    OutlinedTextField(
                        value = textFieldValue,
                        onValueChange = { textFieldValue = it },
                        label = { Text(text = "Timestamp") },
                        trailingIcon = { Icon(Icons.Filled.DateRange, contentDescription = null) },
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxHeight(),
                    ) {
                        Button(
                            onClick = { appManager.quit() },
                            enabled = false,
                        ) {
                            Text(text = "Cancel")
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Button(
                            onClick = {
                                val unixTimestamp = textFieldValue.toLong()
                                val dateTime = Date.from(Instant.ofEpochSecond(unixTimestamp))
                                appManager.newBrowserInstance(dateTime)
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
