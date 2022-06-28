package core.pages

import org.openqa.selenium.By
import org.openqa.selenium.WebDriver
import org.openqa.selenium.WebElement
import schedule
import java.util.*

class CourseSignupPage(driver: WebDriver) : Page(driver) {

    private var persistentLoginTask: Timer? = null

    private val addCourseByRefTabBy = By.xpath("//div[@id='AddDropCourseTabs']//a[contains(text(), 'Add by Reference')]")

    private val submitCourseButtonBy = By.xpath("//div[@id='AddDropCourseTabs']//input[@type='submit'][@value='Add Course(s)']")

    private val addCourseByRefTab: WebElement?
        get() = driver.findElements(addCourseByRefTabBy).firstOrNull()

    private val submitCoursesButton: WebElement?
        get() = driver.findElements(submitCourseButtonBy).firstOrNull()


    init {
        driver.get(PAGE_URL)
    }


    fun clickAddCourseByReferenceTab() {
        addCourseByRefTab?.click()
    }

    fun clickSubmitCoursesButton() {
        submitCoursesButton?.click()
    }

    fun scheduleClickSubmitCoursesButton(date: Date) {
        val timer = Timer("scheduled-course-submit", true)

        if (persistentLoginTask != null) {
            persistentLoginTask!!.cancel()
        }

        timer.schedule(date) {
            try {
                clickSubmitCoursesButton()
            } catch (_: Exception) {
                // Uh oh
            }
        }

        persistentLoginTask = timer
    }

    companion object {
        const val PAGE_URL = "https://my.gcc.edu/ICS/My_Info/Academics.jnz?portlet=AddDrop_Courses&screen=Add+Drop+Courses&screenType=next"
    }
}
