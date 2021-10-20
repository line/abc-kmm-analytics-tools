package com.linecorp.abc.analytics.utils

expect class JsonParser() {
    fun parse(filename: String): Map<String, String>?
}