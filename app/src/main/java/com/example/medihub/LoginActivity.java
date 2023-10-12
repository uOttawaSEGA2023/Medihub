package com.example.medihub;

import androidx.appcompat.app.AppCompatActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import android.os.Bundle;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private Button button5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        button5 = findViewById(R.id.button5); // Use 'findViewById' directly

        button5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegistrationOptionActivity();
            }
        });
    }

    public void openRegistrationOptionActivity() {
        Intent intent = new Intent(this, RegistrationOptionActivity.class);
        startActivity(intent);
    }
}
