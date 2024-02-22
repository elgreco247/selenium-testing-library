package seleniumtestinglib.locators

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.openqa.selenium.NoSuchElementException
import seleniumtestinglib.driver
import seleniumtestinglib.queries.JsType.Companion.asJsExpression
import seleniumtestinglib.render
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.text.RegexOption.IGNORE_CASE

class ByPlaceholderTextTest {

    @Test
    fun exact() {
        driver.render("<input placeholder='Username' />")

        val result = driver.findElement(ByPlaceholderText("Username", exact = true))

        assertEquals("input", result.tagName)
    }


    @ParameterizedTest
    @ValueSource(strings = ["Username", "USERNAME", "user"])
    fun `not exact`(text: String) {
        driver.render("<input placeholder='Username' />")

        val result = driver.findElement(ByPlaceholderText(text, exact = false))

        assertEquals("input", result.tagName)
    }

    @Test
    fun `not exact 2`() {
        driver.render("<input placeholder='Username' />")

        val result = driver.findElement(ByPlaceholderText("user").inexact())

        assertEquals("input", result.tagName)
    }

    @Test
    fun regex() {
        driver.render("<input placeholder='Username' />")

        val result = driver.findElement(ByPlaceholderText("/user/i".asJsExpression()))

        assertEquals("input", result.tagName)
    }

    @Test
    fun `regex alternative`() {
        driver.render("<input placeholder='Username' />")

        val result = driver.findElement(ByPlaceholderText(Regex("user", IGNORE_CASE)))

        assertEquals("input", result.tagName)
    }

    @Test
    fun `exact not found`() {
        driver.render("<input placeholder='Username' />")

        val result = runCatching {
            driver.findElement(ByPlaceholderText("username", exact = true))
        }

        assertTrue(result.exceptionOrNull() is NoSuchElementException)
    }
}
