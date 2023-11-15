package com.mobdeve.lecturematerial_04_movingbetweenactivities

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.lecturematerial_04_movingbetweenactivities.databinding.ActivityFinalBinding

class FinalActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding : ActivityFinalBinding = ActivityFinalBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        //(1) By getting the intent of the Activity, it is possible to retrieve
        //    messages passed from one activity to the other.
        val comment = this.intent.getStringExtra("COMMENT")
        // Please note that this.intent is equivalent to this.getIntent() of the Activity

        //(2) This message can then be manipulated as needed by the program.
        viewBinding.commentText.text = comment
    }
}

