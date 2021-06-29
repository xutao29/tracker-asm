package com.taoyixun.tracker.delegate

import android.util.Log
import com.taoyixun.tracker.bean.TrackerBean

class DefaultDelegate : BaseTrackerDelegate() {
    override fun action(data: TrackerBean) {
        Log.d(
            "TrackerDelegate",
            "absolutePath = ${data.chainPath} ; " +
                    "tag = ${data.eventId} ; " +
                    "description = ${data.description} ; " +
                    "type = ${data.type.name}"
        )
    }
}