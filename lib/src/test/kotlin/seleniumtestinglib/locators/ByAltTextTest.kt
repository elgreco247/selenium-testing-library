package seleniumtestinglib.locators

import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import org.openqa.selenium.NoSuchElementException
import seleniumtestinglib.driver
import seleniumtestinglib.queries.TextMatch.JsFunction
import seleniumtestinglib.render
import java.util.regex.Pattern
import java.util.regex.Pattern.CASE_INSENSITIVE
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ByAltTextTest {

    @ParameterizedTest
    @ValueSource(strings = ["img", "input", "area"])
    fun `by alt text`(tagName: String) {
        driver.render("<$tagName alt='Incredibles 2 Poster' src='/incredibles-2.png' />")

        val result = driver.findElement(ByAltText("Incredibles 2 Poster"))

        assertEquals(tagName, result.tagName)
    }

    @Test
    fun `not exact`() {
        driver.render("<img alt='Incredibles 2 Poster' src='/incredibles-2.png' />")

        val result = driver.findElement(ByAltText("incredibles 2", exact = false))

        assertEquals("img", result.tagName)
    }

    @Test
    fun `not exact 2`() {
        driver.render("<img alt='Incredibles 2 Poster' src='/incredibles-2.png' />")

        val result = driver.findElement(ByAltText("incredibles 2").inexact())

        assertEquals("img", result.tagName)
    }

    @Test
    fun regex() {
        driver.render("<img alt='Incredibles 2 Poster' src='/incredibles-2.png' />")

        val result = driver.findElement(ByAltText(Pattern.compile("incred", CASE_INSENSITIVE)))

        assertEquals("img", result.tagName)
    }

    @Test
    fun function() {
        driver.render("<div alt='Incredibles 2 Poster' src='/incredibles-2.png' />")

        val result = runCatching {
            driver.findElement(ByAltText(JsFunction("c => c.startsWith('inc')")))
        }

        assertTrue(result.exceptionOrNull() is NoSuchElementException)
    }

    @Test
    fun `by alt text not-valid type`() {
        driver.render("<div alt='Incredibles 2 Poster' src='/incredibles-2.png' />")

        val result = runCatching {
            driver.findElement(ByAltText("Incredibles 2 Poster"))
        }

        assertTrue(result.exceptionOrNull() is NoSuchElementException)
    }

}
