package seleniumtestinglib.jestdom

import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver
import org.openqa.selenium.remote.RemoteWebElement
import seleniumtestinglib.ensureScript


val WebElement.isDisabled get() = executeJestDomQuery("toBeDisabled")
val WebElement.isEmptyDomElement get() = executeJestDomQuery("toBeEmptyDOMElement")
val WebElement.isInTheDocument get() = executeJestDomQuery("toBeInTheDocument")
val WebElement.isInvalid get() = executeJestDomQuery("toBeInvalid")
val WebElement.isRequired get() = executeJestDomQuery("toBeRequired")

private fun WebElement.executeJestDomQuery(domFunction: String): Boolean {
    val driver = (this as RemoteWebElement).wrappedDriver as RemoteWebDriver
    driver.ensureScript("jest-dom.js", "window.matchers?.toBeInTheDocument")
    return driver.executeScript("return matchers.$domFunction(arguments[0]).pass", this) as Boolean
}
