package com.taoyixun.tracker.bean

/**
 * created by xutao
 * on 6/28/21
 */
data class TrackerBean(
    val type: TrackerType,
    val eventId: String?,
    val chainPath: String?,
    val description: String?,
)

enum class TrackerType {
    CLICK,
    // ...
}