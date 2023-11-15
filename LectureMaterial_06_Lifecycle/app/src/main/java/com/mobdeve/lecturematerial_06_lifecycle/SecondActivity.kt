package com.mobdeve.lecturematerial_06_lifecycle

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

/*
*   There's nothing much here as most of the important information is in the MainActivity.kt file
* */

class SecondActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)
    }
}