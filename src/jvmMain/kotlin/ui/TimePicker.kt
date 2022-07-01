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

import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Material Design time picker.
 *
 * Time pickers are components designed to allow a user to select a time in 12-hour format.
 *
 * @param timeOfDay The initial/default time-of-day to be displayed on the selector.
 * @param selectedTime The lambda to be invoked when the time-of-day is updated.
 */
@Composable
fun TimePicker(
    timeOfDay: TimeOfDay,
    selectedTime: (TimeOfDay) -> Unit
) {
    val rangeOfHours = 1..12
    val rangeOfMinutes = 0..59
    val timeDropdownTextStyle = TextStyle(
        fontSize = 100.sp,
        fontWeight = FontWeight.ExtraLight
    )

    Card(elevation = 5.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(all = 24.dp),
        ) {

            // Hours selection
            TimeDropdownSelector(
                range = rangeOfHours,
                default = timeOfDay.hour,
                onSelection = { selectedTime(timeOfDay.copy(hour = it)) },
                style = timeDropdownTextStyle
            )

            // Deliminator - smaller to avoid drawing attention
            Spacer(modifier = Modifier.width(4.dp))
            Text(":", style = MaterialTheme.typography.h2)
            Spacer(modifier = Modifier.width(4.dp))

            // Minutes selection
            TimeDropdownSelector(
                range = rangeOfMinutes,
                default = timeOfDay.minute,
                onSelection = { selectedTime(timeOfDay.copy(minute = it)) },
                style = timeDropdownTextStyle
            )

            Spacer(modifier = Modifier.width(16.dp))

            // AM / PM selection
            AmPmButtons(
                afternoon = timeOfDay.afternoon,
                onClickAm = { selectedTime(timeOfDay.copy(afternoon = false)) },
                onClickPm = { selectedTime(timeOfDay.copy(afternoon = true)) }
            )
        }
    }
}

/**
 * Button grouping to toggle between AM / PM.
 *
 * @param afternoon if afternoon (PM) is currently selected
 * @param onClickAm the lambda to be invoked when the AM button is pressed
 * @param onClickPm the lambda to be invoked when the PM button is pressed
 */
@Composable
private fun AmPmButtons(
    afternoon: Boolean,
    onClickAm: () -> Unit,
    onClickPm: () -> Unit
) = Column {

    // Before noon (ante meridiem)
    Button(
        onClick = onClickAm,
        colors = ToggleColors(!afternoon),
        content = { Text("AM") }
    )

    // After noon (post meridiem)
    Button(
        onClick = onClickPm,
        colors = ToggleColors(afternoon),
        content = { Text("PM") }
    )
}

/**
 * Toggles between primary vs muted colors for a toggle button.
 *
 * @param selected if the button is currently selected
 */
@Composable
private fun ToggleColors(selected: Boolean): ButtonColors {
    return ButtonDefaults.buttonColors(
        backgroundColor = if (selected)
            MaterialTheme.colors.primary
        else MaterialTheme.colors.background
    )
}
