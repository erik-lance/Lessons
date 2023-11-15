package com.mobdeve.lecturematerial_05_passingdataback

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.mobdeve.lecturematerial_05_passingdataback.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    // Declaration of static variables needed for Intent / ActivityResultLauncher
    companion object {
        const val NUMBER1_KEY = "NUMBER1_KEY"
        const val NUMBER2_KEY = "NUMBER2_KEY"
    }

    private lateinit var viewBinding: ActivityMainBinding

    // Walk through found at https://medium.com/droid-log/androidx-activity-result-apis-the-new-way-7cfc949a803c
    private val myActivityLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
        // Check to see if the result returned is appropriate (i.e. OK)
        if (result.resultCode == RESULT_OK) {
            val res = result.data!!.getIntExtra(AdditionActivity.RESULT_KEY, 0)
            this.viewBinding.resultTv.text = "RESULT: $res"
        } else if (result.resultCode == RESULT_CANCELED) {
            this.viewBinding.resultTv.text = "RESULT: canceled"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        viewBinding.toAdditionBtn.setOnClickListener(View.OnClickListener {
            // Create the Intent moving from MainActivity to AdditionActivity
            val intent = Intent(applicationContext, AdditionActivity::class.java)

            // Get the 2 numbers from EditTexts and insert to into the Intent object
            intent.putExtra(MainActivity.NUMBER1_KEY, viewBinding.number1Etv.text.toString().toInt())
            intent.putExtra(MainActivity.NUMBER2_KEY, viewBinding.number2Etv.getText().toString().toInt())

            // Launch the Intent expecting a result
            myActivityLauncher.launch(intent)
        })
    }
}