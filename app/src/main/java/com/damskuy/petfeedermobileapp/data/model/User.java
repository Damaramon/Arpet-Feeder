package com.damskuy.petfeedermobileapp.data.model;

import com.google.firebase.database.IgnoreExtraProperties;
import com.google.firebase.database.PropertyName;

@IgnoreExtraProperties
public class User {

    private String name;

    public User() {}

    public User(String name) {
        this.name = name;
    }

    @PropertyName("name")
    public String getName() { return name; }

    @PropertyName("name")
    public void setName(String name) { this.name = name; }
}