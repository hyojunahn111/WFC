package com.example.wfc;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        Button signupButton = findViewById(R.id.signupButton);
        signupButton.setOnClickListener(v -> startActivity(new Intent(LoginActivity.this, MainActivity.class)));

        EditText idEditText = findViewById(R.id.idEditText);
        EditText passwordEditText = findViewById(R.id.passwordEditText);

        Button loginButton = findViewById(R.id.loginButton);
        loginButton.setOnClickListener(v -> signIn(idEditText.getText().toString(), passwordEditText.getText().toString()));


    }

/*    @Override
    public void onStart() {
        super.onStart();

        // 로그아웃하지 않을 시 자동 로그인 , 회원가입시 바로 로그인 됨
        moveMainPage(auth.getCurrentUser());
    }*/

    // 로그인
    private void signIn(String email, String password) {

        if (!email.isEmpty() && !password.isEmpty()) {
            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginActivity.this, "로그인에 성공 하였습니다.", Toast.LENGTH_SHORT).show();
                            moveMainPage(auth.getCurrentUser());
                        } else {
                            Toast.makeText(LoginActivity.this, "로그인에 실패 하였습니다.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    // 유저정보 넘겨주고 메인 액티비티 호출
    private void moveMainPage(FirebaseUser user){
        if(user != null){
            startActivity(new Intent(this, LogoutActivity.class));
            finish();
        }
    }
}
