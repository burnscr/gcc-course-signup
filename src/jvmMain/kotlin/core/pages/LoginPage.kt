package core.pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import java.time.Duration

class LoginPage(driver: WebDriver) : Page(driver) {

    private val loginButtonBy = By.cssSelector("input[value='Login']")

    private val collapseLoginButtonBy = By.cssSelector("button[aria-controls='user-login-section']")

    private val loginButton: WebElement?
        get() = driver.findElements(loginButtonBy).firstOrNull()

    private val collapseLoginButton: WebElement?
        get() = driver.findElements(collapseLoginButtonBy).firstOrNull()


    init {
        driver.get(PAGE_URL)
    }


    fun isLoggedIn(): Boolean {
        waitUntilPageLoaded()
        return loginButton == null
    }

    fun isCollapsed(): Boolean {
        val button = collapseLoginButton ?: return false
        return button.getAttribute("aria-expanded") == "false"
    }

    fun setCollapsed(collapsed: Boolean) {
        val button = collapseLoginButton ?: return
        val isCollapsed = button.getAttribute("aria-expanded") == "false"
        if (collapsed != isCollapsed) button.click()
    }

    fun waitForLogin() {
        val duration = Duration.ofSeconds(1)
        while (!isLoggedIn()) {
            driver.manage().timeouts().implicitlyWait(duration)
        }
    }

    companion object {
        const val PAGE_URL = "https://my.gcc.edu"
    }
}
