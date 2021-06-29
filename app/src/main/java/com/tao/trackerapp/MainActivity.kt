package com.tao.trackerapp

import android.content.DialogInterface
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.taoyixun.tracker.util.TagHelper.trackerTag
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text_view.trackerTag = "text_view_tag"
        button.trackerTag = "button_tag"
        image_view.trackerTag = "image_view_tag"
        radio_group.trackerTag = "radio_group_tag"
        radio_button_01.trackerTag = "radio_button_01_tag"
        radio_button_02.trackerTag = "radio_button_02_tag"

        text_view.setOnClickListener {
            Log.d(TAG, "text_view click")
        }

        button.setOnClickListener {
            Log.d(TAG, "button click")
        }

        image_view.setOnClickListener {
            Log.d(TAG, "image_view click")
        }

        radio_group.setOnCheckedChangeListener { group, checkedId ->
            Log.d(TAG, "radio_group checked change")
        }

        radio_button_01.setOnClickListener {
            Log.d(TAG, "radio_button_01 click")
        }

        radio_button_02.setOnClickListener {
            Log.d(TAG, "radio_button_01 click")
        }
    }
}