package com.mobdeve.s13.tiongquico.erik.fakeadexexercise

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager

// ViewBinding
import com.mobdeve.s13.tiongquico.erik.fakeadexexercise.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // ViewBinding
        val viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(viewBinding.root)

        // Recycler View
        viewBinding.myRecyclerView.adapter = MyAdapter(DataGenerator.loadData())

        // Layout Manager
        val layoutManager = LinearLayoutManager(this)
        viewBinding.myRecyclerView.layoutManager = layoutManager

        layoutManager.orientation = LinearLayoutManager.HORIZONTAL
    }
}