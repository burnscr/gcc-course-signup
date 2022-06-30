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

package core.pages

import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.WebDriver
import org.openqa.selenium.support.ui.WebDriverWait
import java.time.Duration

open class Page(protected val driver: WebDriver) {

    fun waitUntilPageLoaded() {
        val timeout = Duration.ofMinutes(10)
        driver.manage().timeouts().pageLoadTimeout(timeout)
    }

    fun waitUntilPageReady() {
        val timeout = Duration.ofMinutes(10)
        val wait = WebDriverWait(driver, timeout)
        wait.until {
            try {
                val result = (it as JavascriptExecutor).executeScript(
                    "return 'complete' == document.readyState;")

                if (result is Boolean && result) {
                    return@until true
                }
            } catch (_: Exception) {
                // Possible page reload. Fine
            }
            return@until false
        }
    }
}
