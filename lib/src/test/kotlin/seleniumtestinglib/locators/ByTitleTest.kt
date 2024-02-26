package seleniumtestinglib.locators

import seleniumtestinglib.driver
import seleniumtestinglib.queries.TextMatch.Companion.asJsFunction
import seleniumtestinglib.render
import java.util.regex.Pattern
import java.util.regex.Pattern.CASE_INSENSITIVE
import kotlin.test.Test
import kotlin.test.assertEquals

class ByTitleTest {

    @Test
    fun `by title`() {
        driver.render("<div title='foobar'>Hello World!</div>")

        val result = driver.findElement(ByTitle("foobar"))

        assertEquals("Hello World!", result.text)
    }

    @Test
    fun `by title on svg`() {
        driver.render(
            """<svg>
              <title>foobar</title>
              <g><path /></g>
            </svg>"""
        )

        val result = driver.findElement(ByTitle("foobar"))

        assertEquals("foobar", result.text)
    }

    @Test
    fun `not exact`() {
        driver.render("<div title='foobar'>Hello World!</div>")

        val result = driver.findElement(ByTitle("FOO", exact = false))

        assertEquals("Hello World!", result.text)
    }

    @Test
    fun `not exact 2`() {
        driver.render("<div title='foobar'>Hello World!</div>")

        val result = driver.findElement(ByTitle("FOO").inexact())

        assertEquals("Hello World!", result.text)
    }

    @Test
    fun regex() {
        driver.render("<div title='foobar'>Hello World!</div>")

        val result = driver.findElement(ByTitle(Pattern.compile("FOO", CASE_INSENSITIVE)))

        assertEquals("Hello World!", result.text)
    }

    @Test
    fun function() {
        driver.render("<div title='foobar'>Hello World!</div>")

        val result = driver.findElement(ByTitle("c => c.startsWith('foo')".asJsFunction()))

        assertEquals("Hello World!", result.text)
    }
}
