package com.mobdeve.lecturematerial_04_movingbetweenactivities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.lecturematerial_04_movingbetweenactivities.databinding.ActivityChoice01Binding

class Choice01Activity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityChoice01Binding = ActivityChoice01Binding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.choice1Next.setOnClickListener {
            val intent = Intent(applicationContext, FinalActivity::class.java)

            //(1) It is possible to get a string resource from the string.xml through code.
            val comment = resources.getString(R.string.script_04_scene_commend_1)

            //(2) Through intents, it is possible to pass values to another activity as well.
            intent.putExtra("COMMENT", comment)
            this.startActivity(intent)
        }
    }
}