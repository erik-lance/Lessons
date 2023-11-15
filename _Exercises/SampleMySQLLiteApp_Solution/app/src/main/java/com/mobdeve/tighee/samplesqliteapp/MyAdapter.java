package com.mobdeve.tighee.samplesqliteapp;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.result.ActivityResultLauncher;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MyAdapter extends RecyclerView.Adapter<MyViewHolder> {
    // Dynamic copy of our data
    private ArrayList<Contact> contacts;

    // Launcher used for the edit operation.
    private ActivityResultLauncher<Intent> myActivityResultLauncher;

    // Variables needed for running any DB calls on a thread separate from the UI/main thread
    private MyDbHelper myDbHelper;
    private ExecutorService executorService = Executors.newSingleThreadExecutor();

    // Aside from needing a reference to the ArrayList, we also need the activity launcher that is
    // used when starting the AddContactActivity for an EDIT operation.
    public MyAdapter(ArrayList<Contact> contacts, ActivityResultLauncher<Intent> myActivityResultLauncher) {
        this.contacts = contacts;
        this.myActivityResultLauncher = myActivityResultLauncher;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item's layout and create a MyViewHolder object.
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_layout, parent, false);
        MyViewHolder myViewHolder = new MyViewHolder(v);

        // Logic for performing the delete operation.
        myViewHolder.setDeleteBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Run the DB operation on a separate thread from the UI/main thread.
                executorService.execute(new Runnable() {
                    @Override
                    public void run() {
                        myDbHelper = MyDbHelper.getInstance(view.getContext());
                        myDbHelper.deleteContact(contacts.get(myViewHolder.getAdapterPosition()));

                        // This is a little tricky, first... we need to run any UI updates on the
                        // UI thread. Its not good to update any UI elements outside of the main
                        // thread. We'll learn more about this in the module for Process Management.
                        // Hence, the runOnUiThread is needed here; however, we're in the Adapter
                        // class and don't have easy access to the MainActivity. To gain access,
                        // we typecast the context for which the delete button was created under
                        // (i.e. the MainActivity) to an Activity reference. From here, we can now
                        // call runOnUiThread. There are other ways to do run the UI update on the
                        // UI thread, but this is the way I wanted to do it.
                        ((Activity) view.getContext()).runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // Remove the appropriate contact from the contacts ArrayList
                                contacts.remove(myViewHolder.getAdapterPosition());
                                // Notify the adapter that the specific item has been removed.
                                notifyItemRemoved(myViewHolder.getAdapterPosition());
                            }
                        });
                    }
                });
            }
        });

        // Logic for the edit operation. Basically... pass all the information within the selected
        // contact to the AddContactActivity so that the views there can be properly displayed.
        // also, we need to pass the view holder position so to avoid calling notifyDatasetChanged
        // when updating the RecyclerView. We could remove the need for the ViewHolder's position
        // if we simply call notifyDatasetChanged when the entire edit operation has finished... but
        // I wanted to be extra and just update the specific position.
        myViewHolder.setEditBtnOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(view.getContext(), AddContactActivity.class);
                i.putExtra(IntentKeys.FIRST_NAME_KEY.name(), contacts.get(myViewHolder.getAdapterPosition()).getFirstName());
                i.putExtra(IntentKeys.LAST_NAME_KEY.name(), contacts.get(myViewHolder.getAdapterPosition()).getLastName());
                i.putExtra(IntentKeys.NUMBER_KEY.name(), contacts.get(myViewHolder.getAdapterPosition()).getNumber());
                i.putExtra(IntentKeys.IMAGE_URI_KEY.name(), contacts.get(myViewHolder.getAdapterPosition()).getImageUri().toString());
                i.putExtra(IntentKeys.CONTACT_ID_KEY.name(), contacts.get(myViewHolder.getAdapterPosition()).getId());
                i.putExtra(IntentKeys.VIEW_HOLDER_POSITION_KEY.name(), myViewHolder.getAdapterPosition());
                myActivityResultLauncher.launch(i);
            }
        });

        return myViewHolder;
    }

    // A mysterious onBindViewHolder... so mysterious...
    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.bindData(this.contacts.get(position));
    }

    // The even more mysterious getItemCount... much awe... very enigma...
    @Override
    public int getItemCount() {
        return this.contacts.size();
    }
}
