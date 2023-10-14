package com.example.medihub.interfaces;

import java.util.HashMap;

public interface Model {
    public HashMap<String, String> validate();
    public HashMap<String, Object> toMap();
}
