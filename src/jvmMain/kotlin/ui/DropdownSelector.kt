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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.material.DropdownMenu
import androidx.compose.material.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp

/**
 * Material Design dropdown selector.
 *
 * Dropdown selectors are components that wrap a selectable component with a dropdown menu
 * to abstract away the logic of toggling menu visibility to select an item.
 *
 * @param items The list of items to include within the dropdown menu.
 * @param defaultItem The item selected by default.
 * @param onSelection Called when the user selects an item from the menu when expanded.
 * @param modifier The modifier to be applied to the layout.
 * @param contentAlignment The default alignment inside the dropdown selector.
 * @param propagateMinConstraints Whether the incoming min constraints should be passed to
 * content.
 * @param enabled Controls the enabled state. When `false`, this dropdown selector will not
 * be clickable.
 * @param offset [DpOffset] to be added to the position of the menu.
 * @param itemContent The content of an individual dropdown menu item.
 * @param content The content of the dropdown selector.
 */
@Composable
fun <T> DropdownSelector(
    items: List<T>,
    defaultItem: T? = null,
    onSelection: (T) -> Unit,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.TopStart,
    propagateMinConstraints: Boolean = false,
    enabled: Boolean = true,
    offset: DpOffset = DpOffset(0.dp, 0.dp),
    itemContent: @Composable @ExtensionFunctionType RowScope.(T) -> Unit,
    content: @Composable @ExtensionFunctionType BoxScope.(T?) -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    var selected by remember { mutableStateOf(defaultItem) }

    Box(
        modifier = modifier,
        contentAlignment = contentAlignment,
        propagateMinConstraints = propagateMinConstraints
    ) {
        content(selected)

        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            offset = offset
        ) {
            items.forEach { item ->
                DropdownMenuItem(
                    onClick = {
                        selected = item
                        expanded = false
                        onSelection(item)
                    },
                    content = { itemContent(item) }
                )
            }
        }

        Spacer(
            modifier = Modifier
                .matchParentSize()
                .background(Color.Transparent)
                .clickable(
                    onClick = { expanded = true },
                    enabled = enabled
                )
        )
    }
}
