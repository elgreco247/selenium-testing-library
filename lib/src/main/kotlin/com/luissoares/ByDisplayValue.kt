package com.luissoares

/**
 * https://testing-library.com/docs/queries/bydisplayvalue
 */
class ByDisplayValue(
    value: String,
    matchTextBy: TextMatchType = TextMatchType.STRING,
    exact: Boolean? = null,
    normalizer: String? = null,
) : ByTestingLibrary(
    by = "DisplayValue",
    textMatch = value,
    matchTextBy = matchTextBy,
    options = mapOf("exact" to exact, "normalizer" to normalizer),
)
