package com.taoyixun.tracker.util

import android.view.View

object TagHelper {

    private val TRACKER_EVENT_ID_TAG = "TRACKER_EVENT_ID".hashCode()

    // kotlin Use
    var View.trackerTag: String?
        get() {
            return getTag(TRACKER_EVENT_ID_TAG) as? String ?: tag as? String
        }
        set(value) {
            setTag(TRACKER_EVENT_ID_TAG, value)
        }

    // java use
    @JvmStatic
    fun setTracker(view: View, eventId: String) {
        view.trackerTag = eventId
    }
}