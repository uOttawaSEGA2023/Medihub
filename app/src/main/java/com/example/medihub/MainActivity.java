package com.example.medihub;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.FirebaseApp;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseApp.initializeApp(this);
    }
}