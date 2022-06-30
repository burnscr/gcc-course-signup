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

import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import core.AppManager
import ui.App

fun main() = application {
    val appManager = AppManager()
    appManager.initialize()
    Window(
        onCloseRequest = {
            exitApplication()
            appManager.quit()
        },
        title = "GCC Course Signup",
        resizable = false,
        state = WindowState(
            height = 350.dp,
            width = 500.dp
        )
    ) {
        App(appManager)
    }
}
