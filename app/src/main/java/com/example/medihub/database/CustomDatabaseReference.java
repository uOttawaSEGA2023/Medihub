package com.example.medihub.database;

import com.example.medihub.interfaces.Model;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.util.HashMap;

public class CustomDatabaseReference {
    private static DatabaseReference reference = null;
    private String tableName = null;

    public CustomDatabaseReference(String tableName) {
        this.tableName = tableName;
        instantiate();
    }

    /**
     * Gets an instance of the table being referenced to
     *
     * @return the database reference
     */
    private DatabaseReference instantiate() {
        if (tableName == null)
            return null;

        if (reference == null)
            reference = FirebaseDatabase.getInstance().getReference(tableName);

        return reference;
    }

    /**
     * Gets the object in the database corresponding to the key
     *
     * @param key The key to search
     * @return The database reference or null if not found
     */
    public DatabaseReference get(String key) {
        DatabaseReference ref = null;

        if (reference != null)
            ref = reference.child(key);

        return ref;
    }

    public Query where(String column, String value) {
        Query query = null;

        if (reference != null)
            reference.orderByChild(column).equalTo(value);

        return query;
    }
    public Query where(String column, boolean value) {
        Query query = null;

        if (reference != null)
            reference.orderByChild(column).equalTo(value);

        return query;
    }
    public Query where(String column, double value) {
        Query query = null;

        if (reference != null)
            reference.orderByChild(column).equalTo(value);

        return query;
    }

    /**
     * Creates a database object given a java object and auto-generates a unique key
     *
     * @param model The java object to push to the database
     */
    public void create(Model model) {
        String key = reference.push().getKey();
        reference.child(key).setValue(model);
    }

    /**
     * Creates a database object given a java object and a key
     *
     * @param key The key to push the object to
     * @param model The java object to push to the database
     */
    public void create(String key, Model model) {
        reference.child(key).setValue(model);
    }

    /**
     * Updates the data given the key and map of updates
     *
     * @param key The database object to update
     * @param updates The map of updates to implement
     */
    public void patch(String key, HashMap<String, Object> updates) {
        reference.child(key).updateChildren(updates);
    }

    /**
     * Deletes the database object from the corresponding key
     *
     * @param key The key to delete
     */
    public void delete(String key) {
        DatabaseReference ref = reference.child(key);
        ref.removeValue();
    }
}
