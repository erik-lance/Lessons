package com.mobdeve.tighee.samplemysqlliteapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultCallback
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {
    private var contacts: ArrayList<Contact>? = null
    private var recyclerView: RecyclerView? = null
    private var myAdapter: MyAdapter? = null
    private var addContactBtn: FloatingActionButton? = null
    private val myActivityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        StartActivityForResult(),
        object : ActivityResultCallback<ActivityResult?> {
            override fun onActivityResult(result: ActivityResult) {
                if (result.resultCode == RESULT_OK) {
                    if (result.data != null) {
                        contacts!!.add(
                            0, Contact(
                                result.data!!.getStringExtra(IntentKeys.LAST_NAME_KEY.name)!!,
                                result.data!!.getStringExtra(IntentKeys.FIRST_NAME_KEY.name)!!,
                                result.data!!.getStringExtra(IntentKeys.NUMBER_KEY.name)!!,
                                result.data!!.getStringExtra(IntentKeys.IMAGE_URI_KEY.name)!!
                            )
                        )
                        myAdapter!!.notifyItemChanged(0)
                    }
                }
            }
        })

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        contacts = ArrayList()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.setLayoutManager(LinearLayoutManager(this))
        myAdapter = MyAdapter(contacts!!)
        recyclerView.setAdapter(myAdapter)
        addContactBtn = findViewById(R.id.addContactBtn)
        addContactBtn.setOnClickListener(View.OnClickListener {
            val i = Intent(this@MainActivity, AddContactActivity::class.java)
            myActivityResultLauncher.launch(i)
        })
    }
}