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

import withHour
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*

/**
 * Immutable data class to store a time-of-day in a 12-hour format.
 *
 * @param hour the hour-of-day (1 - 12)
 * @param minute the minute-of-hour (0 - 59)
 * @param afternoon if the hour-of-day is after noon (post meridiem)
 */
data class TimeOfDay(val hour: Int, val minute: Int, val afternoon: Boolean) {

    /**
     * Converts the time-of-day into a timezone-aware [Date] relative to 'today'.
     *
     * @return a timezone-aware [Date] of the time-of-day
     * @throws DateTimeException if the time values are invalid
     */
    fun toDate(): Date {
        val localDateTime = LocalDateTime.now()
            .withHour(hour, afternoon)
            .withMinute(minute)
            .withSecond(0)
            .withNano(0)

        val zoneId = ZoneId.systemDefault()  // your timezone
        val instant = localDateTime.atZone(zoneId).toInstant()
        return Date.from(instant)
    }
}
