package seleniumtestinglib.locators

import org.openqa.selenium.By
import org.openqa.selenium.JavascriptExecutor
import org.openqa.selenium.SearchContext
import org.openqa.selenium.WebElement
import seleniumtestinglib.queries.LocatorType
import seleniumtestinglib.queries.TextMatch
import seleniumtestinglib.queries.TextMatch.Companion.asJsString
import seleniumtestinglib.queries.TextMatch.JsFunction
import seleniumtestinglib.queries.asJsExpression
import seleniumtestinglib.queries.executeTLQuery
import java.util.regex.Pattern

/**
 * https://testing-library.com/docs/queries/bytitle
 */
data class ByTitle(
    private val title: TextMatch,
    private val exact: Boolean? = null,
    private val normalizer: JsFunction? = null,
) : By() {

    constructor(
        title: String,
        exact: Boolean? = null,
        normalizer: JsFunction? = null,
    ) : this(
        title = title.asJsString(),
        exact = exact,
        normalizer = normalizer,
    )

    constructor(
        title: Pattern,
        exact: Boolean? = null,
        normalizer: JsFunction? = null,
    ) : this(
        title = title.asJsExpression(),
        exact = exact,
        normalizer = normalizer,
    )

    fun inexact() = copy(exact = false)

    override fun findElements(context: SearchContext): List<WebElement> =
        (getWebDriver(context) as JavascriptExecutor).executeTLQuery(
            by = LocatorType.Title,
            textMatch = title,
            options = mapOf(
                "exact" to exact,
                "normalizer" to normalizer,
            ),
        )
}
