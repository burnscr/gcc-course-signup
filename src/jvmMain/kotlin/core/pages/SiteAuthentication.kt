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

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import schedule
import java.time.Duration
import java.util.*

class SiteAuthentication(driver: WebDriver) : Page(driver) {

    private var persistentLoginTask: Timer? = null

    private val stayLoggedInButtonBy = By.xpath("//button[text()='Stay logged in']")

    private val stayLoggedInButton: WebElement?
        get() = driver.findElements(stayLoggedInButtonBy).firstOrNull()


    private fun isLoginTimeoutShowing(): Boolean {
        return stayLoggedInButton?.isDisplayed ?: false
    }

    private fun clickStayLoggedInButton() {
        stayLoggedInButton?.click()
    }

    fun persistLogin(enabled: Boolean) {
        if (!enabled && persistentLoginTask != null) {
            persistentLoginTask!!.cancel()
            persistentLoginTask = null
        } else if (enabled && persistentLoginTask == null) {
            val timer = Timer("persistent-login-scheduler", true)
            val period = Duration.ofSeconds(1).toMillis()

            timer.schedule(0L, period) {
                try {
                    if (isLoginTimeoutShowing()) {
                        clickStayLoggedInButton()
                    }
                } catch (_: Exception) {
                    // Possible page reload. Fine
                }
            }

            persistentLoginTask = timer
        }
    }
}
