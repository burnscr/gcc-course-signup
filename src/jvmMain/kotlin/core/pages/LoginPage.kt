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

import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import org.openqa.selenium.support.FindBy
import org.openqa.selenium.support.PageFactory

class LoginPage(driver: WebDriver) : Page(driver) {

    @FindBy(css = "input[value='Login']")
    private val loginButton: List<WebElement> = listOf()

    @FindBy(css = "button[aria-controls='user-login-section']")
    private val expandLoginFormButton: List<WebElement> = listOf()

    init {
        driver.get(PAGE_URL)
        PageFactory.initElements(driver, this)
    }

    fun clickExpandLoginFormButton() {
        expandLoginFormButton.first().click()
    }

    fun isLoginFormExpanded(): Boolean {
        val expandButton = expandLoginFormButton.firstOrNull() ?: return true
        return !expandButton.isDisplayed || expandButton.getAttribute("aria-expanded") == "true"
    }

    fun isLoginButtonPresent(): Boolean {
        return loginButton.isNotEmpty()
    }

    companion object {
        const val PAGE_URL = "https://my.gcc.edu"
    }
}
