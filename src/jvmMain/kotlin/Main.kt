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

/**
 * Main entry point for this application.
 */
fun main() = application {
    // Referencing the AppManager object here invokes its init{} code
    // which preloads the web driver manager now. This will cause the
    // first launch of the program to take slightly longer, but will
    // avoid a delayed response later on when the user tries to launch
    // a browser for the first time.
    AppManager

    Window(
        onCloseRequest = {
            exitApplication()
            AppManager.quit()
        },
        title = "GCC Course Signup",
        resizable = false,
        state = WindowState(
            height = 350.dp,
            width = 500.dp
        ),
        content = { App() }
    )
}
