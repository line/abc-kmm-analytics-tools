
package com.linecorp.abc.analytics

import com.linecorp.abc.analytics.utils.JsonParser

class ATScreenNameMapper {
    companion object {
        val isConfigured: Boolean
            get() { return nameMap != null }

        private const val filename = "ATScreenNameMapper.json"

        private val keyMap: Map<String, String>?
        private val nameMap: Map<String, String>?

        init {
            nameMap = JsonParser().parse(filename)
            keyMap = nameMap?.entries?.associateBy({ (_, v) -> v }, { (k, _) -> k })
        }

        fun getClassName(screenName: String): String? {
            return keyMap?.get(screenName)
        }

        fun getScreenName(key: String): String? {
            return nameMap?.get(key)
        }
    }
}