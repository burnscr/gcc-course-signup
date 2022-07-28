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

import io.github.bonigarcia.wdm.WebDriverManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.openqa.selenium.SessionNotCreatedException
import org.openqa.selenium.WebDriver
import org.openqa.selenium.chrome.ChromeDriver
import org.openqa.selenium.chrome.ChromeOptions
import org.openqa.selenium.edge.EdgeDriver
import org.openqa.selenium.edge.EdgeOptions
import org.openqa.selenium.firefox.FirefoxDriver
import org.openqa.selenium.firefox.FirefoxOptions
import org.openqa.selenium.remote.DesiredCapabilities
import org.openqa.selenium.safari.SafariDriver
import org.openqa.selenium.safari.SafariOptions
import java.util.concurrent.atomic.AtomicBoolean

/**
 * A singleton object to manage the browser web drivers.
 */
object DriverFactory {

    private val browserFactories = mapOf(
        BrowserType.CHROME to BrowserFactory(
            webDriverManagerFactory = { WebDriverManager.chromedriver() },
            webDriverFactory = { ChromeDriver(ChromeOptions().merge(it)) }
        ),
        BrowserType.FIREFOX to BrowserFactory(
            webDriverManagerFactory = { WebDriverManager.firefoxdriver() },
            webDriverFactory = { FirefoxDriver(FirefoxOptions().merge(it)) }
        ),
        BrowserType.SAFARI to BrowserFactory(
            webDriverManagerFactory = { WebDriverManager.safaridriver() },
            webDriverFactory = { SafariDriver(SafariOptions().merge(it)) }
        ),
        BrowserType.EDGE to BrowserFactory(
            webDriverManagerFactory = { WebDriverManager.edgedriver() },
            webDriverFactory = { EdgeDriver(EdgeOptions().merge(it)) }
        )
    )

    /**
     * Initializes the requested browser's web drivers without creating a new instance
     * of the [WebDriver] itself.
     */
    suspend fun preload(browserType: BrowserType) {
        val factory = browserFactories[browserType] ?: throw RuntimeException("Unsupported browser type: $browserType")
        factory.ensureInitialized()
    }

    /**
     * Builds a new web driver for the specified browser type with the provided options.
     *
     * Lazily initializes the requested browser drivers exactly once atomically.
     *
     * @param browserType the type of browser to create a driver for
     * @param capabilities options to use when creating the web driver
     * @return an initialized [WebDriver] for the specified browser
     * @throws SessionNotCreatedException if the selected browser is not installed on the system
     */
    suspend fun get(browserType: BrowserType, capabilities: DesiredCapabilities = DesiredCapabilities()): WebDriver {
        val factory = browserFactories[browserType] ?: throw RuntimeException("Unsupported browser type: $browserType")
        return factory.buildWebDriver(capabilities)
    }

    //-----------------------------------------------------------------------
    private class BrowserFactory<M : WebDriverManager, D : WebDriver>(
        private val webDriverManagerFactory: () -> M,
        private val webDriverFactory: (DesiredCapabilities) -> D
    ) {
        private val initialized = AtomicBoolean(false)
        private val initializing = AtomicBoolean(false)

        /**
         * Atomically downloads the relevant drivers if they don't already exist.
         */
        suspend fun ensureInitialized() {
            while (!initialized.get()) {
                if (initializing.compareAndSet(false, true)) {
                    try {
                        if (!initialized.get()) {
                            withContext(Dispatchers.IO) {
                                webDriverManagerFactory().setup()
                            }
                            initialized.set(true)
                        }
                    } finally {
                        initializing.set(false)
                    }
                }
            }
        }

        /**
         * Builds a new instance of the web driver.
         */
        suspend fun buildWebDriver(desiredCapabilities: DesiredCapabilities): WebDriver {
            ensureInitialized()
            return webDriverFactory(desiredCapabilities)
        }
    }
}
