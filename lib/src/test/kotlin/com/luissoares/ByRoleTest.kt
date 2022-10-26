package com.luissoares

import org.junit.jupiter.api.extension.ExtendWith
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments.of
import org.junit.jupiter.params.provider.MethodSource
import org.openqa.selenium.remote.RemoteWebDriver
import kotlin.test.Test
import kotlin.test.assertEquals

@ExtendWith(DriverLifeCycle::class)
class ByRoleTest(private val driver: RemoteWebDriver) {

    @ParameterizedTest
    @MethodSource("examples")
    fun `by role`(role: String, content: String) {
        driver.getFromHtml(content)

        val result = driver.findElement(ByRole(role))

        assertEquals(role, result.ariaRole)
    }

    private fun examples() = setOf(
        of("textbox", """<input type="text" placeholder="5-digit zipcode" id="txtbox" />"""),
        of("textbox", """<textarea id="txtboxMultiline" required></textarea>"""),
        of("button", """"<div id="saveChanges" tabindex="0" role="button" aria-pressed="false">Save</div>"""),
        of("button", """<button type="button" id="saveChanges">Save</button>"""),
        of("form", """<div role="form"></div>"""),
        of("form", """<form aria-label="xyz">test</form>"""),
    )

    @Test
    fun `not exact`() {
        driver.getFromHtml(
            """
            <div role="tablist">
                <button role="tab" aria-selected="true">Native</button>
            </div>
        """
        )

        val result = driver.findElement(ByRole("tabli", exact = false))

        assertEquals("div", result.tagName)
    }

    @Test
    fun regex() {
        driver.getFromHtml(
            """
            <div role="tablist">
                <button role="tab" aria-selected="true">Native</button>
            </div>
        """
        )

        val result = driver.findElement(ByRole("/TABL/i", matchTextBy = TextMatchType.REGEX))

        assertEquals("div", result.tagName)
    }

    @Test
    fun `aria-selected`() {
        driver.getFromHtml(
            """
              <div role="tablist">
                <button role="tab" aria-selected="false">React</button>
                <button role="tab" aria-selected="true">Native</button>
                <button role="tab" aria-selected="false">Cypress</button>
              </div>
        """
        )

        val result = driver.findElements(ByRole("tab", selected = true))

        assertEquals("Native", result.single().text)
    }

    @Test
    fun `with name`() {
        driver.getFromHtml(
            """
                <label for="email">Email address</label>
                <input />
                <input
                  type="email"
                  id="email"
                  aria-describedby="email-help"
                  placeholder="Enter email"
                />
                <input />
        """
        )

        val result = driver.findElements(ByRole("textbox", name = "Email address"))

        assertEquals("input", result.single().tagName)
    }

    @ParameterizedTest
    @MethodSource("hidden values")
    fun hidden(hidden: Boolean, expectedButtonsFound: List<String>) {
        driver.getFromHtml(
            """<main aria-hidden="true">
                <button>Open dialog</button>
              </main>
              <div role="dialog">
                <button>Close dialog</button>
              </div>"""
        )

        val result = driver.findElements(ByRole("button", hidden = hidden))

        assertEquals(expectedButtonsFound, result.map { it.text })
    }

    private fun `hidden values`() = setOf(
        of(false, listOf("Close dialog")),
        of(true, listOf("Open dialog", "Close dialog")),
    )

    @Test
    fun description() {
        driver.getFromHtml(
            """<ul>
                      <li role="alertdialog" aria-describedby="notification-id-1">
                        <div><button>Close</button></div>
                        <div id="notification-id-1">You have unread emails</div>
                      </li>
                      <li role="alertdialog" aria-describedby="notification-id-2">
                        <div><button>Close</button></div>
                        <div id="notification-id-2">Your session is about to expire!</div>
                      </li>
                    </ul>"""
        )

        val result = driver.findElements(ByRole("alertdialog", description = "Your session is about to expire!"))

        assertEquals("Your session is about to expire!", result.single().text.substringAfter("Close\n"))
    }

    @Test
    fun `description with regex`() {
        driver.getFromHtml(
            """<ul>
                      <li role="alertdialog" aria-describedby="notification-id-1">
                        <div><button>Close</button></div>
                        <div id="notification-id-1">You have unread emails</div>
                      </li>
                      <li role="alertdialog" aria-describedby="notification-id-2">
                        <div><button>Close</button></div>
                        <div id="notification-id-2">Your session is about to expire!</div>
                      </li>
                    </ul>"""
        )

        val result = driver.findElements(ByRole("alertdialog", description = "/your session/i", matchDescriptionBy = TextMatchType.REGEX))

        assertEquals("Your session is about to expire!", result.single().text.substringAfter("Close\n"))
    }

    @ParameterizedTest
    @MethodSource("heading level values")
    fun `heading level`(level: Int?, expectedResults: List<String>) {
        driver.getFromHtml(
            """<h1>Heading Level One</h1>
                <h2>First Heading Level Two</h2>
                <h3>Heading Level Three</h3>
                <div role="heading" aria-level="2">Second Heading Level Two</div>"""
        )

        val result = driver.findElements(ByRole("heading", level = level))

        assertEquals(expected = expectedResults, result.map { it.tagName })
    }

    private fun `heading level values`() = setOf(
        of(2, listOf("h2", "div")),
        of(null, listOf("h1", "h2", "h3", "div"))
    )

    @Test
    fun checked() {
        driver.getFromHtml(
            """<button role="checkbox" aria-checked="false">Gummy bears</button>
                    <button role="checkbox" aria-checked="true">Sugar</button>
                    <button role="checkbox" aria-checked="false">Whipped cream</button>"""
        )

        val result = driver.findElements(ByRole("checkbox", checked = true))

        assertEquals("Sugar", result.single().text)
    }

    @Test
    fun pressed() {
        driver.getFromHtml(
            """<button aria-pressed="true">👍</button>
                     <button aria-pressed="false">👎</button>"""
        )

        val result = driver.findElements(ByRole("button", pressed = true))

        assertEquals("👍", result.single().text)
    }

    @Test
    fun current() {
        driver.getFromHtml(
            """<nav>
                        <a href="current/page" aria-current="true">👍</a>
                        <a href="another/page">👎</a>
                      </nav>"""
        )

        val result = driver.findElements(ByRole("link", current = true))

        assertEquals("👍", result.single().text)
    }

    @Test
    fun expanded() {
        driver.getFromHtml(
            """
            <a href="x"></a>
            <a aria-expanded="false" href="x">expanded</a>
            <a href="x"></a>
        """
        )

        val result = driver.findElements(ByRole("link", expanded = false))

        assertEquals("expanded", result.single().text)
    }

    @ParameterizedTest
    @MethodSource("test cases query fallbacks")
    fun `query fallbacks`(queryFallbacks: Boolean?, expectedCount: Int) {
        driver.getFromHtml(
            """ <div role="switch checkbox" /> """
        )

        val result = driver.findElements(ByRole("checkbox", queryFallbacks = queryFallbacks))

        assertEquals(expectedCount, result.size)
    }

    private fun `test cases query fallbacks`() = setOf(
        of(false, 0),
        of(null, 0),
        of(true, 1),
    )
}
