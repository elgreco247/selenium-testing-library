package com.luissoares

/**
 * https://testing-library.com/docs/queries/bylabeltext
 */
class ByLabelText(
    text: String,
    matchTextBy: TextMatchType = TextMatchType.STRING,
    exact: Boolean? = null,
    selector: String? = null,
    normalizer: String? = null,
) : ByTestingLibrary(
    by = "LabelText",
    textMatch = text,
    matchTextBy = matchTextBy,
    options = mapOf(
        "selector" to selector,
        "exact" to exact,
        "normalizer" to normalizer,
    ),
)
