package com.mobdeve.tighee.fakeadex

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SnapHelper
import com.mobdeve.tighee.fakeadex.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Perform View Binding
        val viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Set the adapter (logic has been compressed into one line)
        viewBinding.myRecyclerView.adapter = MyAdapter(DataGenerator.loadData())

        // Create a LinearLayoutManager with a horizontal orientation
        val linearLayoutManager = LinearLayoutManager(this)
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL

        // Set the LayoutManager
        viewBinding.myRecyclerView.layoutManager = linearLayoutManager

        // This next code snippet ensures that scroller snaps in place instead of being free position
        // Taken from https://stackoverflow.com/questions/29134094/recyclerview-horizontal-scroll-snap-in-center
        val snapHelper: SnapHelper = PagerSnapHelper()
        snapHelper.attachToRecyclerView(viewBinding.myRecyclerView)
    }
}