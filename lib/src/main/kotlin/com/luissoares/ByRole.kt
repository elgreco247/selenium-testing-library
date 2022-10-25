package com.luissoares

import org.openqa.selenium.By
import org.openqa.selenium.SearchContext

data class ByRole(
    private val role: String,
    private val exact: Boolean = true,
    private val name: String? = null,
    private val selected: Boolean? = null,
    // TODO
    // hidden?: boolean = false,
    // description?: TextMatch,
    // checked?: boolean,
    // pressed?: boolean,
    // current?: boolean | string,
    // expanded?: boolean,
    // queryFallbacks?: boolean,
    // level?: number,
) : By() {
    override fun findElements(context: SearchContext) =
        with(TestingLibraryScript) {
            getJavascriptExecutor(context).queryAllBy(
                by = "Role",
                mainArgument = role,
                options = mapOf(
                    "exact" to exact,
                    "name" to name,
                    "selected" to selected,
                ).filterValues { it != null },
            )
        }
}
