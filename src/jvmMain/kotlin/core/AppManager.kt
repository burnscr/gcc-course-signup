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
import org.openqa.selenium.WebDriver
import java.time.Duration
import java.util.*
import kotlin.concurrent.thread

/**
 * A singleton object to manage the state of this application's backend.
 */
object AppManager {

    // A list of all drivers currently bound to a browser
    private val drivers = mutableListOf<WebDriver>()

    suspend fun preloadBrowserDrivers(browserType: BrowserType) {
        DriverFactory.preload(browserType)
    }

    /**
     * Creates a new browser to be used to automate course registration submission.
     *
     * @param browserType the type of browser for the new instance to use
     * @param registration the [Date] describing exactly when course registration is
     * scheduled to become available.
     */
    suspend fun newBrowserInstance(browserType: BrowserType, registration: Date) {
        val driver: WebDriver = DriverFactory.get(browserType)
        drivers.add(driver)
        startApplicationLogic(driver, registration)
    }

    /**
     * Shutdown all running browsers, cleaning up their resources.
     */
    fun quit() {
        drivers.forEach(WebDriver::quit)
    }

    //-----------------------------------------------------------------------
    /**
     * Main runner handling all the logic for automating course registration submission.
     *
     * This runner is wrapped in a daemon thread to ensure application shutdown is not
     * blocked by running browser instances.
     *
     * @param driver the [WebDriver] acting as a remote controller of a local browser
     * @param registration the [Date] describing exactly when course registration is
     * scheduled to become available
     */
    private fun startApplicationLogic(driver: WebDriver, registration: Date) = thread(isDaemon = true) {
        try {
            // Since many students will be using the site at the same time, the response times
            // will become very long. We need to tell the web driver to be more patient to avoid
            // it timing out waiting for the server to serve our request and load the pages.
            driver.manage().timeouts().pageLoadTimeout(Duration.ofMinutes(20))

            // Navigates to the MyGCC login page and waits for the user to successfully log in.
            // For convenience, this also expands the login dialog box since the browser size
            // often triggers the site's responsive design to collapse components for simplicity.
            val loginPage = LoginPage(driver)
            if (!loginPage.isLoginFormExpanded())
                loginPage.clickExpandLoginFormButton()
            waitUntilLoggedIn(driver, loginPage)

            // Starts a repeating task to continuously check if the user is about to be logged
            // out due to not interacting with the site for a while. This usually happens after
            // about 20 minutes of inactivity. Since we cannot submit courses if we get logged
            // out, this repeating task presses the "stay logged in" button within one second of
            // the prompt appearing to refresh the authentication token and stay logged in.
            val authentication = SiteAuthentication(driver)
            authentication.persistLogin(true)

            // Navigates to the course registration page and schedules course submission.
            val coursePage = CourseSignupPage(driver)
            coursePage.clickAddCourseByReferenceTab()
            coursePage.scheduleClickSubmitCoursesButton(registration)

            // We need to wait for the user to close out of the browser. If this application
            // failed to properly submit the registered courses (perhaps the computer's clock
            // is too far out of sync) then the user will low-key panic and try manually
            // re-registering the course by clicking the Add Course(s) button themselves. We
            // don't want to interfere with that by closing out of the browser and making them
            // start the entire process over amidst the inrush of hundreds of other students.
            // This also lets the user view the results of the registration (confirmation dialog).
            waitUntilBrowserClose(driver)
        } finally {
            // No matter what happened, ensure all running resources for this browser get cleaned up.
            driver.quit()
        }
    }

    /**
     * Blocks until the browser was manually closed by the user (or randomly dies).
     */
    private fun waitUntilBrowserClose(driver: WebDriver) {
        val duration = Duration.ofSeconds(1)
        try {
            while (true) {
                driver.title  // Fetching the site's title will raise an exception once closed
                Thread.sleep(duration.toMillis())
            }
        } catch (_: Exception) {
            // Closing the browser throws an exception
        }
    }

    /**
     * Blocks until the user logs into the website.
     */
    private fun waitUntilLoggedIn(driver: WebDriver, loginPage: LoginPage) {
        val duration = Duration.ofSeconds(1)
        val correctDomain: () -> Boolean = { driver.currentUrl.startsWith("https://my.gcc.edu/") }
        val loginButtonPresent: () -> Boolean = { loginPage.isLoginButtonPresent() }

        while (!correctDomain() || loginButtonPresent())
            loginPage.wait(duration)
    }
}
