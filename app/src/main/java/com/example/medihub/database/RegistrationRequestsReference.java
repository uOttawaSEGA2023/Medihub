package com.example.medihub.database;

public class RegistrationRequestsReference extends CustomDatabaseReference {
    public RegistrationRequestsReference()  { super("registration_requests"); }

    @Override
    public void delete(String key) {}
}
