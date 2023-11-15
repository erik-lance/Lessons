package com.mobdeve.tighee.samplesqliteapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {

    // Data holder needed for the application
    private ArrayList<Contact> contacts;

    // RecyclerView components
    private RecyclerView recyclerView;
    private MyAdapter myAdapter;

    // Add... contacts... button
    private FloatingActionButton addContactBtn;

    // General purpose activity launcher -- launches in both MainActivity and MyAdapter
    // In both cases, the intent is coming from the AddContactActivity
    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent data = result.getData();
                if (data != null){
                    // Define a temporary contact object to reduce redundant code
                    Contact tempContact = new Contact(
                        data.getStringExtra(IntentKeys.LAST_NAME_KEY.name()),
                        data.getStringExtra(IntentKeys.FIRST_NAME_KEY.name()),
                        data.getStringExtra(IntentKeys.NUMBER_KEY.name()),
                        Uri.parse(data.getStringExtra(IntentKeys.IMAGE_URI_KEY.name())),
                        data.getLongExtra(IntentKeys.CONTACT_ID_KEY.name(), -1)
                    );

                    // ADD OPERATION PERFORMED, ELSE EDIT OPERATION PERFORMED
                    if (result.getResultCode() == ResultCodes.ADD_RESULT.ordinal()){
                        // Add contact to contacts ArrayList and place at top of the RecyclerView
                        contacts.add(0, tempContact);
                        myAdapter.notifyItemInserted(0);
                    } else if (result.getResultCode() == ResultCodes.EDIT_RESULT.ordinal()) {
                        // Get the ViewHolder position that was edited
                        int viewHolderPosition = data.getIntExtra(
                                IntentKeys.VIEW_HOLDER_POSITION_KEY.name(),
                                -1);
                        // Update the specific location in the contacts ArrayList and notify the
                        // adapter of the change made
                        contacts.set(viewHolderPosition, tempContact);
                        myAdapter.notifyItemChanged(viewHolderPosition);
                    }
                }
            }
        });

    // Variables needed for running any DB calls on a thread separate from the UI/main thread
    private MyDbHelper myDbHelper;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize the RecyclerView
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                // Get all contacts from the database
                myDbHelper = MyDbHelper.getInstance(MainActivity.this);
                contacts = myDbHelper.getAllContactsDefault();
                printAllContacts();

                // I know this wasn't discussed... but its best that any updates to UI elements
                // happen in the UI/main thread; hence, calling runOnUiThread(), which is a method
                // of an Activity.
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // Pass in the contacts to the needed components and set the adapter
                        myAdapter = new MyAdapter(contacts, myActivityResultLauncher);
                        recyclerView.setAdapter(myAdapter);
                    }
                });
            }
        });

        // Logic for the add button (move from MainActivity to AddContactActivity via launcher)
        this.addContactBtn = findViewById(R.id.addContactBtn);
        this.addContactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, AddContactActivity.class);
                myActivityResultLauncher.launch(i);
            }
        });
    }

    // Just a function I had to print all the contacts in the logcat
    private void printAllContacts() {
        for(Contact c : contacts) {
            Log.d("MainActivity", "printAllContacts: " + c.toString());
        }
    }
}