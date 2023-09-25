package com.damskuy.petfeedermobileapp.data.session;

import android.content.Context;
import android.content.SharedPreferences;

import com.damskuy.petfeedermobileapp.data.model.AuthenticatedUser;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class SessionManager {

    private final SharedPreferences sharedPreferences;
    private final SharedPreferences.Editor editor;
    public static final String SHARED_PREFERENCE_NAME = "session";
    public static final String USER_SESSION_KEY = "user_session";

    public SessionManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveUserSession(AuthenticatedUser user) {
        Gson gson = new Gson();
        editor.putString(USER_SESSION_KEY, gson.toJson(user)).commit();
    }

    public AuthenticatedUser getUserSession() {
        String userJson = sharedPreferences.getString(USER_SESSION_KEY, null);
        if (userJson == null) return null;
        try {
            JSONObject jsonObject = new JSONObject(userJson);
            String userId = jsonObject.getString("userId");
            String email = jsonObject.getString("email");
            String name = jsonObject.getString("name");
            return new AuthenticatedUser(userId, name, email);
        } catch (JSONException exception) {
            return null;
        }
    }

    public void removeUserSession() { editor.remove(USER_SESSION_KEY).commit(); }
}
