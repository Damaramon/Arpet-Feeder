package com.damskuy.petfeedermobileapp.data.auth;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.damskuy.petfeedermobileapp.data.model.Result;
import com.damskuy.petfeedermobileapp.data.model.User;
import com.damskuy.petfeedermobileapp.data.model.AuthenticatedUser;
import com.damskuy.petfeedermobileapp.data.session.SessionManager;
import com.damskuy.petfeedermobileapp.data.user.UserDataSource;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class AuthRepository {

    private final AuthDataSource authDataSource;
    private final UserDataSource userDataSource;
    private static AuthRepository instance;
    private AuthenticatedUser user = null;

    private AuthRepository() {
        this.authDataSource = new AuthDataSource();
        this.userDataSource = new UserDataSource();
    }

    public static synchronized AuthRepository getInstance() {
        if (instance == null) instance = new AuthRepository();
        return instance;
    }

    public AuthenticatedUser getAuthenticatedUser() { return user; }

    public boolean isAuthenticated(Context context) {
        checkUserSession(context);
        return user != null;
    }

    public void checkUserSession(Context context) {
        AuthenticatedUser loggedInUser = new SessionManager(context).getUserSession();
        setLoggedInUser(loggedInUser);
    }

    public void createUserSession(Context context, AuthenticatedUser user) {
        SessionManager sessionManager = new SessionManager(context);
        sessionManager.saveUserSession(user);
    }

    public void clearUserSession(Context context) { new SessionManager(context).removeUserSession(); }

    private void setLoggedInUser(AuthenticatedUser user) { this.user = user; }

    public void register(
            String name,
            String email,
            String password,
            MutableLiveData<Result<AuthenticatedUser>> result
    ) {
        authDataSource.registerFirebase(email, password, task -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (task.isSuccessful() && user != null) {
                User userEntity = new User(name);
                storeUserToRealtimeDB(user, userEntity, result);
            }
            else result.postValue(new Result.Error<>(task.getException()));
        });
    }

    public void login(
            String email,
            String password,
            MutableLiveData<Result<AuthenticatedUser>> result
    ) {
        authDataSource.loginFirebase(email, password, task -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (task.isSuccessful() && user != null) fetchUserFromRealtimeDB(user, result);
            else result.postValue(new Result.Error<>(new Exception("Wrong credentials")));
        });
    }

    public void loginWithGoogle(
            GoogleSignInAccount account,
            MutableLiveData<Result<AuthenticatedUser>> result
    ) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        authDataSource.loginWithCredentialsFirebase(credential, task -> {
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
            if (task.isSuccessful() && user != null) fetchOrCreateUserToRealtimeDB(user, result);
            else result.postValue(new Result.Error<>(task.getException()));
        });
    }

    public void logout(Context context) {
        authDataSource.logoutFirebase();
        clearUserSession(context);
        setLoggedInUser(null);
    }

    private void fetchOrCreateUserToRealtimeDB(
            FirebaseUser user,
            MutableLiveData<Result<AuthenticatedUser>> result
    ) {
        userDataSource.fetchUserData(user.getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userEntity = snapshot.getValue(User.class);
                if (userEntity == null) {
                    String firstname = "User";
                    if (user.getDisplayName() != null)
                        firstname = user.getDisplayName().split(" ")[0];
                    userEntity = new User(firstname);
                    storeUserToRealtimeDB(user, userEntity, result);
                } else {
                    AuthenticatedUser authUser = new AuthenticatedUser(
                            user.getUid(),
                            userEntity.getName(),
                            user.getEmail()
                    );
                    setLoggedInUser(authUser);
                    result.postValue(new Result.Success<>(authUser));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                result.postValue(new Result.Error<>(error.toException()));
            }
        });
    }

    private void storeUserToRealtimeDB(
            FirebaseUser user,
            User userEntity,
            MutableLiveData<Result<AuthenticatedUser>> result
    ) {
        userDataSource.storeUserData(user.getUid(), userEntity, (error, ref) -> {
            if (error == null) {
                AuthenticatedUser authUser = new AuthenticatedUser(
                        user.getUid(),
                        userEntity.getName(),
                        user.getEmail()
                );
                setLoggedInUser(authUser);
                result.postValue(new Result.Success<>(authUser));
            }
            else result.postValue(new Result.Error<>(error.toException()));
        });
    }

    private void fetchUserFromRealtimeDB(
            FirebaseUser user,
            MutableLiveData<Result<AuthenticatedUser>> result
    ) {
        userDataSource.fetchUserData(user.getUid(), new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userEntity = snapshot.getValue(User.class);
                if (userEntity != null) {
                    AuthenticatedUser authUser = new AuthenticatedUser(
                            user.getUid(),
                            userEntity.getName(),
                            user.getEmail()
                    );
                    setLoggedInUser(authUser);
                    result.postValue(new Result.Success<>(authUser));
                }
                else result.postValue(new Result.Error<>(new Exception("Failed to get user")));
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                result.postValue(new Result.Error<>(new Exception("Something went wrong!")));
            }
        });
    }
}