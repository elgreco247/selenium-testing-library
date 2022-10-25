package com.luissoares

import org.junit.jupiter.api.Disabled
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
        of(
            "textbox", """<div role="textbox"
                    contenteditable="true"
                    aria-placeholder="5-digit zipcode"
                    aria-labelledby="txtboxLabel">
               </div>"""
        ),
        of("textbox", """<input type="text" placeholder="5-digit zipcode" id="txtbox" />"""),
        of("textbox", """<textarea id="txtboxMultiline" required></textarea>"""),
        of("button", """"<div id="saveChanges" tabindex="0" role="button" aria-pressed="false">Save</div>"""),
        of("button", """<button type="button" id="saveChanges">Save</button>"""),
        of(
            "article", """<div role="article">
                  <h2>Heading of the segment</h2>
                  <p>Paragraph for the segment.</p>
                  Controls to interact with the article, share it, etc.
               </div>"""
        ),
        of(
            "article", """<article>
                  <h2>Heading of the segment</h2>
                  <p>Paragraph for the segment.</p>
                  Controls to interact with the article, share it, etc.
                </article>
            """
        ),
        of(
            "banner", """<div role="banner">
                  <h2>Heading of the segment</h2>
                  Controls to interact with the article, share it, etc.
               </div>"""
        ),
        of(
            "banner", """<header>
                  <a href="#main" id="skipToMain" class="skiptocontent">Skip To main content</a>
                  <img src="images/w3c.png" alt="W3C Logo" />
               </header>"""
        ),
        of(
            "checkbox", """<<span role="checkbox"
                      aria-checked="false"
                      tabindex="0"
                      aria-labelledby="chk1-label"></span>
                    <label id="chk1-label">Remember my preferences</label>"""
        ),
        of(
            "checkbox", """<input type="checkbox" id="chk1-label" />
               <label for="chk1-label">Remember my preferences</label>"""
        ),
        of("link", """<a href="https://mozilla.org">Link 123</a>"""),
        of(
            "link", """<span data-href="https://mozilla.org" tabindex="0" role="link">
              Fake accessible link created using a span
            </span>"""
        ),
        of("toolbar", """""<div role="toolbar"></div>"""),
        of(
            "figure", """<figure>
                          <img src="image.png" alt="put image description here" />
                          <figcaption>Figure 1: The caption</figcaption>
                        </figure>"""
        ),
        of(
            "figure", """<div role="figure" aria-labelledby="figure-1">
                          …
                          <p id="figure-1">Text that describes the figure.</p>
                        </div>"""
        ),
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
    @Disabled("https://github.com/testing-library/dom-testing-library/issues/1181")
    fun description() {
        driver.getFromHtml(
            """<ul>
                    <li role="alertdialog" aria-describedby="notification-id-1">
                      <div><button>Close</button></div>
                      <div id="notification-id-1">You have unread emails</div>
                    </li>
                    <li role="alertdialog" aria-describedby="notification-id-2">
                      <div><button>Close</button></div>
                      <div id="notification-id-2">Your session is about to expire</div>
                    </li>
                  </ul>"""
        )

        val result = driver.findElements(ByRole("alertdialog", description = "Your session is about to expire"))

        assertEquals(1, result.size)
    }

    @ParameterizedTest
    @MethodSource("heading level values")
    fun `heading level`(level: Int?, expectedResults: List<String>) {
        driver.getFromHtml(
            """<section>
                <h1>Heading Level One</h1>
                <h2>First Heading Level Two</h2>
                <h3>Heading Level Three</h3>
                <div role="heading" aria-level="2">Second Heading Level Two</div>
              </section>"""
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
            """<section>
                        <button role="checkbox" aria-checked="false">Gummy bears</button>
                        <button role="checkbox" aria-checked="true">Sugar</button>
                        <button role="checkbox" aria-checked="false">Whipped cream</button>
                    </section>"""
        )

        val result = driver.findElements(ByRole("checkbox", checked = true))

        assertEquals("Sugar", result.single().text)
    }
}
