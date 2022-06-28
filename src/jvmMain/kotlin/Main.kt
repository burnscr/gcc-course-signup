import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import core.AppManager
import ui.App


fun main() = application {
    val appManager = AppManager()
    appManager.initialize()
    Window(
        onCloseRequest = {
            appManager.quit()
            exitApplication()
        },
        title = "GCC Course Signup"
    ) {
        App(appManager)
    }
}
