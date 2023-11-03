package com.example.medihub.database;

public class RegistrationRequestReference extends CustomDatabaseReference {
    public RegistrationRequestReference()  { super("registration_request"); }

    @Override
    public void delete(String key) {}
}
