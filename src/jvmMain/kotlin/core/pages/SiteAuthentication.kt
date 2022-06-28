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
