package com.damskuy.petfeedermobileapp.data.model;

public class AuthenticatedUser {

    private final String userId;

    private final String name;

    private final String email;

    public AuthenticatedUser(String userId, String name, String email) {
        this.userId = userId;
        this.name = name;
        this.email =  email;
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }
}
