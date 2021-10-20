package com.linecorp.abc.analytics.extensions

inline fun <T> Iterable<T>.contains(predicate: (T) -> Boolean): Boolean {
    return filterTo(ArrayList<T>(), predicate).count() > 0
}