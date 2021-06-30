package com.taoyixun.tracker.bean

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