package com.example.wfc;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        //createAccount("example@example.com", "password123");

        Button signupOkButton = findViewById(R.id.signbt);

        signupOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText signupID = findViewById(R.id.idet);
                EditText signupPassword = findViewById(R.id.pwet);

                createAccount(signupID.getText().toString(), signupPassword.getText().toString());
            }
        });

    }

    private void createAccount(String email, String password) {
         if (!email.isEmpty() && !password.isEmpty())
         {
             if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
             {
                 Toast.makeText(MainActivity.this, "이메일 형식으로 입력해주세요!", Toast.LENGTH_SHORT).show();
                 return;
             }

             if(password.length() < 6)
             {
                 Toast.makeText(MainActivity.this, "영문,숫자합 6자리 이상으로 입력해주세요!", Toast.LENGTH_SHORT).show();
                 return;
             }

             auth.createUserWithEmailAndPassword(email, password)
                     .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
                     {
                         @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(MainActivity.this, "계정 생성 완료.", Toast.LENGTH_SHORT).show();
                                    finish(); // 가입창 종료
                                } else {
                                    Toast.makeText(MainActivity.this, "계정 생성 실패", Toast.LENGTH_SHORT).show();
                                }
                         }
                     });
         }
    }
}