package com.example.medihub.interfaces;

import java.util.HashMap;

public interface Model {
    public String getKey();
    public void setKey(String key);
    public HashMap<String, String> validate();
}
