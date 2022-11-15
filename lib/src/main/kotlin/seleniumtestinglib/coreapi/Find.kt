package seleniumtestinglib.coreapi

import org.openqa.selenium.WebElement
import org.openqa.selenium.remote.RemoteWebDriver

fun RemoteWebDriver.findBy(
    by: ByType,
    textMatch: TextMatch,
    options: Map<String, Any?> = emptyMap(),
) = executeTLQuery(
    queryType = QueryType.Find,
    plural = false,
    by = by,
    textMatch = textMatch,
    options = options,
) as WebElement

@Suppress("UNCHECKED_CAST")
fun RemoteWebDriver.findAllBy(
    by: ByType,
    textMatch: TextMatch,
    options: Map<String, Any?> = emptyMap(),
) = executeTLQuery(
    queryType = QueryType.Find,
    plural = true,
    by = by,
    textMatch = textMatch,
    options = options,
) as List<WebElement>
