package com.mobdeve.lecturematerial_06_lifecycle

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.mobdeve.lecturematerial_06_lifecycle.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    companion object {
        private const val TAG : String = "MainActivity"
    }

    private lateinit var viewBinding : ActivityMainBinding

    private var createVal = 0
    private var startVal = 0
    private var resumeVal = 0
    private var pauseVal = 0
    private var stopVal = 0
    private var destroyVal = 0

    //(1) onCreate is the function called when the activity is created.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(this.viewBinding.root)

        Log.d(TAG, "onCreate called.")

        this.viewBinding.button.setOnClickListener(View.OnClickListener {
            val intent = Intent(this@MainActivity, SecondActivity::class.java)
            this@MainActivity.startActivity(intent)
        })

        this.createVal++
        this.viewBinding.onCreateField.setText(this.createVal.toString())
    }

    //(2) onStart is the function called when the activity is about to become visible.
    override fun onStart() {
        super.onStart()

        Log.d(TAG, "onStart called.")

        this.startVal++
        this.viewBinding.onStartField.setText(this.startVal.toString())
    }

    //(3) onResume is the function called when the activity is going to resume
    //    from another activity.
    override fun onResume() {
        super.onResume()

        Log.d(TAG, "onResume called.")

        this.resumeVal++
        this.viewBinding.onResumeField.setText(this.resumeVal.toString())
    }

    //(4) onPause is the function called when the activity is about to start
    //    another activity.
    override fun onPause() {
        super.onPause()

        Log.d(TAG, "onPause called.")

        this.pauseVal++
        this.viewBinding.onPauseField.setText(this.pauseVal.toString())
    }

    //(5) onStop is the function called when the activity is no longer visible
    override fun onStop() {
        super.onStop()

        Log.d(TAG, "onStop called.")

        this.stopVal++
        this.viewBinding.onStopField.setText(this.stopVal.toString())
    }

    //(6) onDestroy is the function called when the activity is destroyed
    override fun onDestroy() {
        super.onDestroy()

        Log.d(TAG, "onDestroy called.")

        this.destroyVal++
        this.viewBinding.onDestroyField.setText(this.destroyVal.toString())
    }

    //(7) To be able to save the information and retain the state when
    //    going on about different views. Use the onSaveInstanceState
    //    function and store the information.
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)

        Log.d(TAG, "onSaveInstanceState called.")

        outState.putInt(MyKeys.ON_CREATE_KEY.name, this.createVal);
        outState.putInt(MyKeys.ON_START_KEY.name, this.startVal);
        outState.putInt(MyKeys.ON_RESUME_KEY.name, this.resumeVal);
        outState.putInt(MyKeys.ON_PAUSE_KEY.name, this.pauseVal);
        outState.putInt(MyKeys.ON_STOP_KEY.name, this.stopVal);
        outState.putInt(MyKeys.ON_DESTROY_KEY.name, this.destroyVal);
    }

    //(8) Called when the information is restored. For now, only called
    //    when changing from portrait to landscape
    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)

        Log.d(TAG, "onRestoreInstanceState called.")

        this.createVal = savedInstanceState.getInt(MyKeys.ON_CREATE_KEY.name);
        this.startVal = savedInstanceState.getInt(MyKeys.ON_START_KEY.name);
        this.resumeVal = savedInstanceState.getInt(MyKeys.ON_RESUME_KEY.name);
        this.pauseVal = savedInstanceState.getInt(MyKeys.ON_PAUSE_KEY.name);
        this.stopVal = savedInstanceState.getInt(MyKeys.ON_STOP_KEY.name);
        this.destroyVal = savedInstanceState.getInt(MyKeys.ON_DESTROY_KEY.name);

        this.viewBinding.onCreateField.setText(this.createVal.toString());
        this.viewBinding.onStartField.setText(this.startVal.toString());
        this.viewBinding.onResumeField.setText(this.resumeVal.toString());
        this.viewBinding.onPauseField.setText(this.pauseVal.toString());
        this.viewBinding.onStopField.setText(this.stopVal.toString());
        this.viewBinding.onDestroyField.setText(this.destroyVal.toString());
    }
}
