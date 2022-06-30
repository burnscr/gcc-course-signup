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

import LocalTime
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalTime

@Composable
fun TimePicker(
    defaultHour: Int,
    defaultMinute: Int,
    selectedTime: (LocalTime) -> Unit
) {
    var isAfternoonSelected by remember { mutableStateOf(defaultHour >= 12) }
    var localHourSelected by remember { mutableStateOf(hour24To12(defaultHour)) }
    var localMinuteSelected by remember { mutableStateOf(defaultMinute) }
    val callbackWithSelectedTime: () -> Unit = {
        val localTime = LocalTime(localHourSelected, localMinuteSelected, isAfternoonSelected)
        selectedTime(localTime)
    }
    callbackWithSelectedTime()

    Card(elevation = 5.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(all = 24.dp),
        ) {
            TimeDropdownSelector(
                range = 1..12,
                default = localHourSelected,
                onSelection = {
                    localHourSelected = it
                    callbackWithSelectedTime()
                },
                style = TextStyle(
                    fontSize = 100.sp,
                    fontWeight = FontWeight.ExtraLight
                )
            )

            Spacer(modifier = Modifier.width(4.dp))

            Text(":", style = MaterialTheme.typography.h2)

            Spacer(modifier = Modifier.width(4.dp))

            TimeDropdownSelector(
                range = 0..59,
                default = localMinuteSelected,
                onSelection = {
                    localMinuteSelected = it
                    callbackWithSelectedTime()
                },
                style = TextStyle(
                    fontSize = 100.sp,
                    fontWeight = FontWeight.ExtraLight
                )
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Button(
                    onClick = {
                        isAfternoonSelected = false
                        callbackWithSelectedTime()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isAfternoonSelected) {
                            MaterialTheme.colors.background
                        } else {
                            MaterialTheme.colors.primary
                        }
                    )
                ) {
                    Text(text = "AM")
                }
                Button(
                    onClick = {
                        isAfternoonSelected = true
                        callbackWithSelectedTime()
                    },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = if (isAfternoonSelected) {
                            MaterialTheme.colors.primary
                        } else {
                            MaterialTheme.colors.background
                        }
                    )
                ) {
                    Text(text = "PM")
                }
            }
        }
    }
}

private fun hour24To12(hour24: Int): Int {
    return when (true) {
        (hour24 < 1) -> hour24 + 12
        (hour24 > 12) -> hour24 - 12
        else -> hour24
    }
}
