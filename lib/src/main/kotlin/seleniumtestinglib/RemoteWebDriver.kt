package seleniumtestinglib

import org.openqa.selenium.remote.RemoteWebDriver
import java.lang.Thread.sleep

internal fun RemoteWebDriver.ensureScript(fileName: String, isPresentFunction: String, retries: Int = 0) {
    require(retries < 5) { "can't ensure script $fileName after $retries retries" }
    if (!isLoaded(isPresentFunction))
        executeScript(loadScript(fileName))
    if (!isLoaded(isPresentFunction)) {
        println("retrying loading script $fileName...")
        sleep(retries * 10L)
        ensureScript(fileName, isPresentFunction, retries + 1)
    }
}

private fun RemoteWebDriver.isLoaded(isPresentFunction: String) =
    executeScript("return typeof $isPresentFunction == 'function'") as Boolean

private val resources = mutableMapOf<String, String>()
private fun loadScript(fileName: String) =
    resources.computeIfAbsent(fileName) {
        {}.javaClass.getResource("/$fileName")?.readText() ?: error("$fileName not found")
    }

val RemoteWebDriver.selection: String
    get() = executeScript("return window.getSelection().toString()") as String
