package com.mobdeve.lecturematerial_04_movingbetweenactivities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mobdeve.lecturematerial_04_movingbetweenactivities.databinding.ActivityChoice02Binding

class Choice02Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityChoice02Binding = ActivityChoice02Binding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.choice2Next.setOnClickListener(View.OnClickListener {
            val intent = Intent(applicationContext, FinalActivity::class.java)

            //(1) It is possible to get a string resource from the string.xml through code.
            val comment = resources.getString(R.string.script_04_scene_commend_2)

            //(2) Through intents, it is possible to pass values to another activity as well.
            intent.putExtra("COMMENT", comment)
            this.startActivity(intent)
        })
    }
}