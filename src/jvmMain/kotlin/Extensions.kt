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

import java.time.DateTimeException
import java.time.LocalDateTime
import java.util.*
import kotlin.concurrent.timerTask

/**
 * Schedules an [action] to be executed at the specified [time].
 */
inline fun Timer.schedule(time: Date, crossinline action: TimerTask.() -> Unit): TimerTask {
    val task = timerTask(action)
    schedule(task, time)
    return task
}

/**
 * Schedules an [action] to be executed periodically, starting after the specified [delay] (expressed
 * in milliseconds) and with the interval of [period] milliseconds between the end of the previous task
 * and the start of the next one.
 */
inline fun Timer.schedule(delay: Long, period: Long, crossinline action: TimerTask.() -> Unit): TimerTask {
    val task = timerTask(action)
    schedule(task, delay, period)
    return task
}

/**
 * Returns a copy of this [LocalDateTime] with the hour-of-day altered.
 *
 * This instance is immutable and unaffected by this method call.
 *
 * @param hour the hour-of-day to set in the result, from 1 to 12
 * @param afternoon if the hour-of-day is in the afternoon (post meridiem)
 * @return a [LocalDateTime] based on this date-time with the requested hour, not null
 * @throws DateTimeException if the hour value is invalid
 */
fun LocalDateTime.withHour(hour: Int, afternoon: Boolean): LocalDateTime {
    return withHour(when (true) {
        (!afternoon && hour == 12) -> 0
        (afternoon && hour != 12) -> hour + 12
        else -> hour
    })
}
