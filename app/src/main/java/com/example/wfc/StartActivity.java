package com.example.wfc;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;

public class StartActivity extends AppCompatActivity {

    Button stbt;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        auth = FirebaseAuth.getInstance();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (auth.getCurrentUser() != null) {
                    startActivity(new Intent(StartActivity.this, LogoutActivity.class));
                } else {
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                }
                finish(); // 현재 액티비티를 종료합니다. 이 부분이 없으면, '뒤로 가기' 버튼을 눌렀을 때 이 화면으로 돌아오게 됩니다.
            }

            //stbt = (Button) findViewById(R.id.appstartbt);

        /*stbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(auth.getCurrentUser() != null){
                    startActivity(new Intent(StartActivity.this, LogoutActivity.class));
                } else {
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));
                }
            }
        });*/
        }, 3000);
    }
}