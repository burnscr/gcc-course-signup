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

package core

import core.pages.CourseSignupPage
import core.pages.LoginPage
import core.pages.SiteAuthentication
import io.github.bonigarcia.wdm.WebDriverManager
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import java.time.Duration
import java.util.*
import kotlin.concurrent.thread

class AppManager {

    private val drivers = mutableListOf<WebDriver>()

    fun initialize() {
        WebDriverManager.chromedriver().setup()
    }

    fun newBrowserInstance(registration: Date) {
        val driver: WebDriver = ChromeDriver()
        drivers.add(driver)
        startApplicationLogic(driver, registration)
    }

    fun quit() {
        drivers.forEach(WebDriver::quit)
    }

    //-----------------------------------------------------------------------

    private fun startApplicationLogic(driver: WebDriver, registration: Date) = thread(isDaemon = true) {
        try {
            val loginPage = LoginPage(driver)
            if (loginPage.isCollapsed())
                loginPage.setCollapsed(false)
            loginPage.waitForLogin()

            val authentication = SiteAuthentication(driver)
            authentication.persistLogin(true)

            val coursePage = CourseSignupPage(driver)
            coursePage.clickAddCourseByReferenceTab()
            coursePage.scheduleClickSubmitCoursesButton(registration)

            waitUntilBrowserClose(driver)
        } finally {
            driver.quit()
        }
    }

    private fun waitUntilBrowserClose(driver: WebDriver) {
        try {
            while (true) {
                driver.title
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1))
            }
        } catch (_: Exception) {
            // closing the browser throws an exception
        }
    }
}
