package com.example.wfc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        auth = FirebaseAuth.getInstance();

        Button logoutButton = findViewById(R.id.logoutbutton);

        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            auth.signOut();
        });
    }
}
