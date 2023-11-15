package com.mobdeve.lecturematerial_04_movingbetweenactivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mobdeve.lecturematerial_04_movingbetweenactivities.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.choice1.setOnClickListener(View.OnClickListener {
            //(1) Intents can be used to tell the application a new activity is about to be created.
            val intent = Intent(applicationContext, Choice01Activity::class.java)

            //(2) To start a new activity, the startActivity function should be called.
            //    This function is found in the MainActivity (or any Activity class)
            this.startActivity(intent);
        })

        viewBinding.choice2.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, Choice02Activity::class.java)
            this.startActivity(intent)
        })
    }
}