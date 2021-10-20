package com.linecorp.abc.analytics.utils

import com.google.gson.Gson
import com.linecorp.abc.analytics.ATEventCenter

actual class JsonParser {
    actual fun parse(filename: String): Map<String, String>? {
        return try {
            val string = ATEventCenter.configuration.context.resources.assets
                .open(filename)
                .bufferedReader()
                .use { it.readText() }
            Gson().fromJson(string, mutableMapOf<String, String>().javaClass)
        } catch(e: Exception) {
            null
        }
    }
}