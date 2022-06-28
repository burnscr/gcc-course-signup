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
