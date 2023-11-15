package com.mobdeve.tighee.samplesqliteapp;

import android.net.Uri;

public class Contact {
    private long id;
    private String lastName, firstName, number;
    private Uri imageUri;

    // Constructor without ID. This isn't exactly advised as you'll have a hard
    // time trying to update the data without the ID reference
    public Contact(String lastName, String firstName, String number, Uri imageUri) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.number = number;
        this.imageUri = imageUri;
    }

    // This is the more appropriate constructor to use because we have a reference
    // of the Contact's id from the DB
    public Contact(String lastName, String firstName, String number, Uri imageUri, long id) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.number = number;
        this.imageUri = imageUri;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getNumber() {
        return number;
    }

    public Uri getImageUri() {
        return imageUri;
    }

    @Override
    public String toString() {
        return "Contact{" +
                "id=" + id +
                ", lastName='" + lastName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", number='" + number + '\'' +
                ", imageUri='" + imageUri.toString() + '\'' +
                '}';
    }
}
