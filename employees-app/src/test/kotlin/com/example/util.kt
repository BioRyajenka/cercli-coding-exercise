package com.example

// mockito is not friendly with kotlin inline classes
// therefore this workaround is needed
// https://github.com/mockito/mockito-kotlin/issues/309
inline fun <Outer, reified Inner> eqInline(
    expected: Outer,
    crossinline access: (Outer) -> Inner,
    test: ((actual: Any) -> Boolean) -> Any?
): Outer {
    val assertion: (Any) -> Boolean = { actual ->
        if (actual is Inner) {
            access(expected) == actual
        } else {
            expected == actual
        }
    }

    @Suppress("UNCHECKED_CAST")
    return test(assertion) as Outer? ?: expected
}
