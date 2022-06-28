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
