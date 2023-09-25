package com.damskuy.petfeedermobileapp.data.user;

import com.damskuy.petfeedermobileapp.data.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserDataSource {

    private final DatabaseReference usersRef;
    private static final String FIREBASE_REALTIME_URL =
            "https://petfeeder-71649-default-rtdb.asia-southeast1.firebasedatabase.app";

    public UserDataSource() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_URL);
        usersRef = firebaseDatabase.getReference("users");
    }

    public void fetchUserData(String uid, ValueEventListener callback) {
        usersRef.child(uid).addListenerForSingleValueEvent(callback);
    }

    public void storeUserData(
            String uid,
            User user,
            DatabaseReference.CompletionListener callback
    ) {
        usersRef.child(uid).setValue(user, callback);
    }
}
