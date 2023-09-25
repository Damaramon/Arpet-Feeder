package com.damskuy.petfeedermobileapp.data.auth;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AuthDataSource {

    private final FirebaseAuth firebaseAuth;

    public AuthDataSource() { firebaseAuth = FirebaseAuth.getInstance(); }

    public void registerFirebase(
            String email,
            String password,
            OnCompleteListener<AuthResult> callback
    ) {
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(callback);
    }

    public void loginFirebase(
            String email,
            String password,
            OnCompleteListener<AuthResult> callback
    ) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(callback);
    }

    public void loginWithCredentialsFirebase(
            AuthCredential credential,
            OnCompleteListener<AuthResult> callback
    ) {
        firebaseAuth.signInWithCredential(credential).addOnCompleteListener(callback);
    }

    public void logoutFirebase() { firebaseAuth.signOut(); }
}
