package com.example.wfc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;

public class LogoutActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    Button allcockbt1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logout);

        auth = FirebaseAuth.getInstance();


        Button logoutButton = findViewById(R.id.logoutbutton);

        allcockbt1 = (Button) findViewById(R.id.allcockbt);

        logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            auth.signOut();

        });

        Button cocknewaddbt = findViewById(R.id.cocknewaddbt);
        cocknewaddbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddNewCocktail.class);
                startActivity(intent);
            }
        }); //칵테일 등록하는 버튼 -> 칵테일 등록하는 곳으로 이동




        allcockbt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AllcocktailActivity.class);
                startActivity(intent);
            } //칵테일 전체 목록을 보는 버튼 -> 칵테일 전체 목록으로 이동
        });


    }
}
