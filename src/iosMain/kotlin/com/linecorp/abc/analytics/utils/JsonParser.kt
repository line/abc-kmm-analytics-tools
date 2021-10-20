package com.linecorp.abc.analytics.utils

import platform.Foundation.NSBundle
import platform.Foundation.NSData
import platform.Foundation.NSJSONSerialization
import platform.Foundation.dataWithContentsOfFile

actual class JsonParser {
    @Suppress("UNCHECKED_CAST")
    actual fun parse(filename: String): Map<String, String>? {
        val split = filename.split(".")
        val path = NSBundle.mainBundle.pathForResource(split.first(), split.last()) ?: ""
        val data = NSData.dataWithContentsOfFile(path) ?: return null
        return NSJSONSerialization.JSONObjectWithData(data, 0, null) as? Map<String, String>
    }
}