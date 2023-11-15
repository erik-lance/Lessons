package com.mobdeve.tighee.samplesqliteapp;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AddContactActivity extends AppCompatActivity {

    // Views needed for the activity
    private EditText firstNameEtv, lastNameEtv, numberEtv;
    private ImageView tempImageIv;
    private Button selectBtn, addBtn;
    private TextView titleTv;

    // To keep track of the changing image uri because I can't think of another way to get the URI
    // from the ImageView once it's set.
    private Uri imageUri;

    // The launcher needed to handle the incoming intent from the image picker
    private ActivityResultLauncher<Intent> myActivityResultLauncher = registerForActivityResult(
        new ActivityResultContracts.StartActivityForResult(),
        new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == Activity.RESULT_OK){
                    try {
                        if(result.getData() != null) {
                            // Get the path of the image
                            imageUri = result.getData().getData();
                            // Load the image into the tempImageIv using the path
                            Picasso.get().load(imageUri).into(tempImageIv);
                        }
                    } catch(Exception exception){
                        Log.d("TAG",""+exception.getLocalizedMessage());
                    }
                }
            }
        });

    // Variables needed for running any DB calls on a thread separate from the UI/main thread
    private MyDbHelper myDbHelper;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    // The tempContact is used when an edit operation is performed so I have a copy of the original
    // copy of the contact. I want to know the original contact so I can optimize the update
    // statement and only include the columns that were changed. If there are no changed, the DB
    // operation doesn't need to push through.
    private Contact tempContact;

    // The viewHolderPosition is when I'm editing a certain item. I need to know this so that I can
    // update the item when I return to the MainActivity, as well as update the right ViewHolder in
    // the RecyclerView.
    private int viewHolderPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        // View initialization
        this.firstNameEtv = findViewById(R.id.firstNameEtv);
        this.lastNameEtv = findViewById(R.id.lastNameEtv);
        this.numberEtv = findViewById(R.id.numberEtv);
        this.tempImageIv = findViewById(R.id.tempImageIv);
        this.selectBtn = findViewById(R.id.selectBtn);
        this.addBtn = findViewById(R.id.addBtn);
        this.titleTv = findViewById(R.id.titleTv);

        // Check if an intent is there (i.e. if its coming for an EDIT operation, as no intent is
        // coming when an ADD operation is performed)
        Intent i = getIntent();

        // To know where the operation is an ADD or an EDIT, I check if the intent has a contact ID.
        // If it doesn't, then it will return a default value of -1, which the DB would never have
        // generated. If there is an ID, then proceed with placing the contact's information into
        // the appropriate views. Other changes are made to make it obvious to the user that an EDIT
        // is being performed. Otherwise, don't do anything as the ADD starts with a fresh activity.
        if(i.getLongExtra(IntentKeys.CONTACT_ID_KEY.name(), -1) != -1) {
            // Save the original information passed in. See comment at variable declaration for more
            // info.
            this.tempContact = new Contact(
                    i.getStringExtra(IntentKeys.LAST_NAME_KEY.name()),
                    i.getStringExtra(IntentKeys.FIRST_NAME_KEY.name()),
                    i.getStringExtra(IntentKeys.NUMBER_KEY.name()),
                    Uri.parse(i.getStringExtra(IntentKeys.IMAGE_URI_KEY.name())),
                    i.getLongExtra(IntentKeys.CONTACT_ID_KEY.name(), -1)
            );

            // Set the views.
            this.firstNameEtv.setText(tempContact.getFirstName());
            this.lastNameEtv.setText(tempContact.getLastName());
            this.numberEtv.setText(tempContact.getNumber());
            this.imageUri = tempContact.getImageUri();
            Picasso.get().load(this.imageUri).into(tempImageIv);

            // Modify parts of the activity so that its obvious an EDIT is taking place.
            this.addBtn.setText("EDIT CONTACT");
            this.titleTv.setText("Edit Contact");

            // Get the position of the ViewHolder we're editing.
            this.viewHolderPosition = i.getIntExtra(IntentKeys.VIEW_HOLDER_POSITION_KEY.name(), -1);
        }

        // Logic for selecting an image from the image picker
        this.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_OPEN_DOCUMENT);
                myActivityResultLauncher.launch(Intent.createChooser(i, "Select Picture"));
            }
        });

        // While this is the "addBtn", this is for both adding and editing.
        this.addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // First, make sure to check if all fields have entries. Its important to this app
                // that all entries (including the image) are present.
                if(doAllFieldHaveEntries()) {
                    // To determine if an EDIT or ADD is being performed, I check if the tempContact
                    // variable is null or not. It won't be null if an edit is being performed.
                    if(AddContactActivity.this.tempContact != null) { // LOGIC FOR EDIT/UPDATE
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                // Get the DB
                                myDbHelper = MyDbHelper.getInstance(AddContactActivity.this);
                                // Perform the update method we defined in the DB helper class. For
                                // more info, check the MyDbHelper class.
                                myDbHelper.updateContact(
                                        tempContact,
                                        new Contact(
                                            lastNameEtv.getText().toString(),
                                            firstNameEtv.getText().toString(),
                                            numberEtv.getText().toString(),
                                            AddContactActivity.this.imageUri,
                                            tempContact.getId()
                                        )
                                );

                                // After performing the DB operation, run the code below on the
                                // UI/main thread. While I don't think all the intent code needs to
                                // be in the thread, I'm calling this specifically because finish()
                                // is best called on the main thread. There are many ways to do this
                                // but I decided to just use the runOnUiThread(). We'll discuss more
                                // on this in the Process Management module.
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent();
                                        i.putExtra(IntentKeys.FIRST_NAME_KEY.name(), firstNameEtv.getText().toString());
                                        i.putExtra(IntentKeys.LAST_NAME_KEY.name(), lastNameEtv.getText().toString());
                                        i.putExtra(IntentKeys.NUMBER_KEY.name(), numberEtv.getText().toString());
                                        i.putExtra(IntentKeys.IMAGE_URI_KEY.name(), AddContactActivity.this.imageUri.toString());
                                        i.putExtra(IntentKeys.CONTACT_ID_KEY.name(), tempContact.getId());

                                        // Notice that we place the ViewHolder's position here.
                                        i.putExtra(IntentKeys.VIEW_HOLDER_POSITION_KEY.name(), viewHolderPosition);

                                        // We set the result code based on our own standard
                                        setResult(ResultCodes.EDIT_RESULT.ordinal(), i);

                                        finish();
                                    }
                                });
                            }
                        });
                    } else { // LOGIC FOR ADD
                        executorService.execute(new Runnable() {
                            @Override
                            public void run() {
                                // Get the DB instance
                                myDbHelper = MyDbHelper.getInstance(AddContactActivity.this);
                                // Perform the insert contact operation. For more info, see the
                                // MyDbHelper class. Please note that we get the ID of the inserted
                                // row since we have the ID on autoincrement and we need to know the
                                // Id so we can create the appropriate context in the MainActivity.
                                long _id = myDbHelper.insertContact(new Contact(
                                        lastNameEtv.getText().toString(),
                                        firstNameEtv.getText().toString(),
                                        numberEtv.getText().toString(),
                                        AddContactActivity.this.imageUri
                                ));

                                // Same explanation as in the EDIT operation.
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Intent i = new Intent();
                                        i.putExtra(IntentKeys.FIRST_NAME_KEY.name(), firstNameEtv.getText().toString());
                                        i.putExtra(IntentKeys.LAST_NAME_KEY.name(), lastNameEtv.getText().toString());
                                        i.putExtra(IntentKeys.NUMBER_KEY.name(), numberEtv.getText().toString());
                                        i.putExtra(IntentKeys.IMAGE_URI_KEY.name(), AddContactActivity.this.imageUri.toString());

                                        // Notice we're passing in the ID of the newly created row
                                        i.putExtra(IntentKeys.CONTACT_ID_KEY.name(), _id);

                                        // We set the result code based on our own standard
                                        setResult(ResultCodes.ADD_RESULT.ordinal(), i);

                                        finish();
                                    }
                                });
                            }
                        });
                    }
                } else {
                    Toast.makeText(
                            AddContactActivity.this,
                            "Please make sure to enter every field and image.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    // Just a method that helps with readability of the code. Used in addBtn.setOnClickListener.
    private boolean doAllFieldHaveEntries() {
        return !firstNameEtv.getText().toString().isEmpty() &&
                !lastNameEtv.getText().toString().isEmpty() &&
                !numberEtv.getText().toString().isEmpty() &&
                !imageUri.toString().isEmpty();
    }
}