package com.mobdeve.lecturematerial_05_passingdataback

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.lecturematerial_05_passingdataback.databinding.ActivityAdditionBinding

class AdditionActivity : AppCompatActivity() {
    companion object {
        const val RESULT_KEY = "RESULT_KEY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewBinding: ActivityAdditionBinding = ActivityAdditionBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // In Java, we would use the getIntent() method to retrieve the sent intent; however, in
        // Kotlin, the naming convention of the method makes it a little more straight forward to
        // access the passed intent by using this.intent or simply intent
        val number1 = this.intent.getIntExtra(MainActivity.NUMBER1_KEY, 0)
        val number2 = this.intent.getIntExtra(MainActivity.NUMBER2_KEY, 0)
        val results = number1 + number2

        viewBinding.logTv.text = "LOG\nNumber 1: $number1\nNumber 2: $number2\nResult: $results"

        viewBinding.toMainActivity.setOnClickListener(View.OnClickListener {
            // Create an empty intent to send back
            val returnIntent = Intent()
            // Place the sum of the numbers in the intent
            returnIntent.putExtra(RESULT_KEY, results)
            // Set the result of the return intent to OK
            setResult(RESULT_OK, returnIntent)
            // Finish or remove the activity from the activity stack
            finish()
        })
    }
}