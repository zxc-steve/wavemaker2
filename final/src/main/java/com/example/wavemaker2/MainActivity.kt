package com.example.wavemaker2

import android.Manifest
import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.wavemaker2.R
import android.view.View.OnTouchListener
import android.view.MotionEvent
import android.widget.CompoundButton
import android.content.pm.PackageManager
import android.util.Log
import android.view.View
import android.widget.Switch
import androidx.core.app.ActivityCompat
import androidx.core.content.PermissionChecker

/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */   class MainActivity : AppCompatActivity() {
    external fun startEngine()
    external fun stopEngine()
    external fun setRecording(isRecording: Boolean)
    external fun setPlaying(isPlaying: Boolean)
    private external fun setLooping(isOn: Boolean)
    @SuppressLint("ClickableViewAccessibility", "UseSwitchCompatOrMaterialCode")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val recordButton = findViewById<View>(R.id.button_record)
        recordButton.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> setRecording(true)
                MotionEvent.ACTION_UP -> setRecording(false)
            }
            true
        }
        val playButton = findViewById<View>(R.id.button_play)
        playButton.setOnTouchListener { view, motionEvent ->
            when (motionEvent.action) {
                MotionEvent.ACTION_DOWN -> setPlaying(true)
                MotionEvent.ACTION_UP -> setPlaying(false)
            }
            true
        }
        val loopButton = findViewById<Switch>(R.id.switch_loop)
        loopButton.setOnCheckedChangeListener { compoundButton, b -> setLooping(b) }


    }

    public override fun onResume() {
        // Check we have the record permission
        if (isRecordPermissionGranted) {
            startEngine()
        } else {
            Log.d(TAG, "Requesting recording permission")
            requestRecordPermission()
        }
        super.onResume()
    }

    public override fun onPause() {
        stopEngine()
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Check that our permission was granted
        if (permissions.isNotEmpty() && permissions[0] == Manifest.permission.RECORD_AUDIO && grantResults[0] == PermissionChecker.PERMISSION_GRANTED) {
            startEngine()
        }
    }

    private fun requestRecordPermission() {
        ActivityCompat.requestPermissions(
            this, arrayOf(Manifest.permission.RECORD_AUDIO),
            WAVEMAKER2_REQUEST
        )
    }

    private val isRecordPermissionGranted: Boolean
        get() = ActivityCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        ) ==
                PackageManager.PERMISSION_GRANTED

    companion object {
        private const val WAVEMAKER2_REQUEST = 0
        private val TAG = MainActivity::class.java.toString()

        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}