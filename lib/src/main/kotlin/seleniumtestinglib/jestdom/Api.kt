package seleniumtestinglib.jestdom

import org.openqa.selenium.By
import org.openqa.selenium.WebElement
import seleniumtestinglib.*

/**
 * https://testing-library.com/docs/ecosystem-jest-dom/
 */
fun expect(element: WebElement?) = JestDomMatcher(element)

// TODO: test nulls
data class JestDomMatcher(
    private val element: WebElement?,
    private val requireTrue: Boolean = true,
) {

    val not get() = JestDomMatcher(element, requireTrue.not())

    fun toBeDisabled() {
        validate(element?.isEnabled == false)
    }

    fun toBeEnabled() {
        validate(element?.isEnabled == true)
    }

    fun toBeEmptyDomElement() {
        val innerHtml = element?.getAttribute("innerHTML")?.replace("<!--.*?-->".toRegex(), "")
        validate(innerHtml?.isEmpty() == true)
    }

    fun toBeInvalid() {
        copy(requireTrue = requireTrue.not()).toBeValid()
    }

    fun toBeInTheDocument() {
        validate(element != null)
    }

    fun toBeRequired() {
        validate(element?.isRequired == true)
    }

    fun toBeValid() {
        validate(element?.isValid == true)
    }

    fun toBeVisible() {
        validate(element?.isDisplayed == true)
    }

    fun toContainElement(ancestor: WebElement?) {
        validate(element?.findElements(By.xpath(".//*"))?.contains(ancestor) == true)
    }

    fun toContainHtml(htmlText: String) {
        val normalizedHtmlText = element?.wrappedDriver?.executeScript(
            """
            const parser = new DOMParser()
            const htmlDoc = parser.parseFromString(arguments[0], "text/html")
            return htmlDoc.querySelector("body").innerHTML
        """, htmlText
        ) as? String
        validate(element?.innerHtml?.contains(normalizedHtmlText.orEmpty()) == true)
    }

    fun toHaveAccessibleDescription(expectedAccessibleDescription: String? = null) {
        when (expectedAccessibleDescription) {
            null -> validate(element?.accessibleDescription?.isNotBlank() == true)
            else -> validate(expectedAccessibleDescription, element?.accessibleDescription)
        }
    }

    fun toHaveAccessibleName(expectedAccessibleName: String? = null) {
        when (expectedAccessibleName) {
            null -> validate(element?.accessibleName?.isNotBlank() == true)
            else -> validate(expectedAccessibleName, element?.accessibleName)
        }
    }

    fun toHaveAttribute(attribute: String, value: String? = null) {
        when (value) {
            null -> validate(element?.getAttribute(attribute)?.isNotBlank() == true)
            else -> validate(value, element?.getAttribute(attribute))
        }
    }

    fun toHaveClass(vararg classNames: String, exact: Boolean = false) {
        val expectedClasses = classNames.map { it.split(Regex("\\s+")) }.flatten().toSet()
        val elementClasses = element?.classList ?: emptySet()
        if (expectedClasses.isEmpty()) {
            validate(elementClasses.isNotEmpty())
            return
        }
        when (exact) {
            false -> validate(elementClasses.containsAll(expectedClasses))
            true  -> validate(expectedClasses, elementClasses)
        }
    }

    fun toHaveFocus() {
    }

    fun toHaveFormValues(values: Map<String, String>) {
        validate(values.all { element?.getAttribute(it.key) == it.value })
    }

    fun toHaveStyle(styles: Map<String, String>) {
        validate(styles.all { element?.getCssValue(it.key) == it.value })
    }

    fun toHaveTextContent(text: String, normalizeWhitespace: Boolean = false) {
        validate(
            text == if (normalizeWhitespace) element?.text?.replace(
                "\\s+".toRegex(), " "
            ) else element?.text
        )
    }

    fun toHaveValue(value: String) {
        validate(value == element?.getAttribute("value"))
    }

    fun toHaveDisplayValue(value: String) {
        validate(value == element?.getAttribute("value"))
    }

    fun toBeChecked() {
        validate(element?.getAttribute("checked") == "true")
    }

    fun toBePartiallyChecked() {
        validate("true" == element?.getAttribute("indeterminate"))
    }

    fun toHaveErrorMessage(message: String) {
        validate(message == element?.getAttribute("aria-errormessage"))
    }

    private fun validate(condition: Boolean) {
        check(condition xor requireTrue.not()) { "condition: $condition, requireTrue: $requireTrue" }
    }

    private fun validate(valueA: Any?, valueB: Any?) {
        check((valueA == valueB) xor requireTrue.not()) { "condition: $valueA, $valueB, requireTrue: $requireTrue" }
    }
}
