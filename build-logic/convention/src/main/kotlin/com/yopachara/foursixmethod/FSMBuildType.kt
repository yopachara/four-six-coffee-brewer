package com.yopachara.foursixmethod

/**
 * Build types used by :app, as an enum so the applicationId suffixes stay type-safe.
 */
@Suppress("unused")
enum class FSMBuildType(val applicationIdSuffix: String? = null) {
    DEBUG(".debug"),
    RELEASE,
}
