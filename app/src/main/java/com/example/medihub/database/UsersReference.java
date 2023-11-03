package com.example.medihub.database;

import com.example.medihub.interfaces.Model;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class UsersReference extends CustomDatabaseReference {
    public UsersReference() { super("users"); }

    @Override
    public void delete(String key) {}
}
