package ui

import LocalTime
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    var localHourTextSelected by remember { mutableStateOf(hour24To12String(defaultHour)) }
    var localMinuteTextSelected by remember { mutableStateOf(minuteToString(defaultMinute)) }
    val callbackWithSelectedTime: () -> Unit = {
        val localTime = LocalTime(localHourTextSelected.toInt(), localMinuteTextSelected.toInt(), isAfternoonSelected)
        selectedTime(localTime)
    }
    callbackWithSelectedTime()

    Card(elevation = 5.dp) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(all = 24.dp),
        ) {
            NumberSelector(1..12, localHourTextSelected) {
                localHourTextSelected = it.toString()
                callbackWithSelectedTime()
            }

            Spacer(modifier = Modifier.width(4.dp))

            Text(":", style = MaterialTheme.typography.h2)

            Spacer(modifier = Modifier.width(4.dp))

            NumberSelector(0..59, localMinuteTextSelected) {
                localMinuteTextSelected = it.toString()
                callbackWithSelectedTime()
            }

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

private fun hour24To12String(hour24: Int): String {
    return when (true) {
        (hour24 < 1) -> (hour24 + 12).toString()
        (hour24 > 12) -> (hour24 - 12).toString()
        else -> hour24.toString()
    }.padStart(2, '0')
}

private fun minuteToString(minute: Int): String {
    return minute.toString().padStart(2, '0')
}


@Composable
fun DropDownList(
    requestToOpen: Boolean = false,
    list: List<String>,
    request: (Boolean) -> Unit,
    selectedString: (String) -> Unit,
) {
    DropdownMenu(
        expanded = requestToOpen,
        onDismissRequest = { request(false) },
    ) {
        list.forEach {
            DropdownMenuItem(
                onClick = {
                    request(false)
                    selectedString(it)
                }
            ) {
                Text(
                    text = it,
                    modifier = Modifier.wrapContentWidth()
                )
            }
        }
    }
}


@Composable
fun NumberSelector(
    range: IntProgression,
    default: String,
    selectedNumber: (Int) -> Unit,
) {
    val numberList = range.map { it.toString().padStart(2, '0') }
    val text = remember { mutableStateOf(default) }
    val isOpen = remember { mutableStateOf(false) }
    val openCloseOfDropDownList: (Boolean) -> Unit = {
        isOpen.value = it
    }
    val userSelectedString: (String) -> Unit = {
        text.value = it
        selectedNumber(it.toInt())
    }

    Box {
        Column {
//            OutlinedTextField(
//                value = text.value,
//                onValueChange = { text.value = it },
//                modifier = Modifier.width(100.dp),
//                textStyle = TextStyle(
//                    fontSize = 50.sp,
//                    textAlign = TextAlign.Center,
//                ),
//            )
            LargeText(text.value)
            DropDownList(
                requestToOpen = isOpen.value,
                list = numberList,
                openCloseOfDropDownList,
                userSelectedString,
            )
        }
        Spacer(
            modifier = Modifier.matchParentSize()
                .background(Color.Transparent)
                .clickable(
                    onClick = { isOpen.value = true }
                )
        )
    }
}

@Composable
fun LargeText(text: String) {
    Text(
        text = text,
//        style = MaterialTheme.typography.,
        fontSize = 100.sp,
        fontWeight = FontWeight.ExtraLight,
        modifier = Modifier.padding(
            horizontal = 8.dp,
        ),
    )
}
