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

import androidx.compose.material.ContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

/**
 * Material Design time dropdown selector.
 *
 *
 *
 * @param range The range of times to include within the dropdown menu.
 * @param default The time selected by default.
 * @param onSelection Called when the user selects a time from the menu when expanded.
 * @param modifier The modifier to be applied to the layout.
 * @param enabled Controls the enabled state. When false, this dropdown selector will not
 * be clickable.
 * @param style Style configuration for the text such as color, font, line height etc.
 */
@Composable
fun TimeDropdownSelector(
    range: IntProgression,
    default: Int,
    onSelection: (Int) -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    style: TextStyle = LocalTextStyle.current
) = DropdownSelector(
    items = range.map(Int::toTimeDigit),
    defaultItem = default.toTimeDigit(),
    onSelection = { onSelection(it.toInt()) },
    modifier = modifier,
    contentAlignment = Alignment.Center,
    propagateMinConstraints = false,
    enabled = enabled,
    itemContent = {
        Text(text = it)
    },
    content = {
        Text(
            text = it.toString(),
            style = style,
            color = if (enabled) {
                Color.Unspecified
            } else {
                LocalContentColor.current.copy(
                    alpha = ContentAlpha.disabled
                )
            }
        )
    }
)

/**
 * Converts an integer into a zero-padded-start two digit string.
 */
private fun Int.toTimeDigit(): String = this.toString().padStart(2, '0')
