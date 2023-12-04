package com.example.wfc;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.Toast;

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


        //Button logoutButton = findViewById(R.id.logoutbutton);

        allcockbt1 = (Button) findViewById(R.id.allcockbt);

        findViewById(R.id.allmenubt2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                final PopupMenu popupMenu = new PopupMenu(getApplicationContext(),view);
                getMenuInflater().inflate(R.menu.popup,popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        if (menuItem.getItemId() == R.id.action_menu1){

                            Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);

                            auth.signOut();

                            Toast.makeText(LogoutActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                        }else if (menuItem.getItemId() == R.id.action_menu2){
                            Toast.makeText(LogoutActivity.this, "기능없음", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(LogoutActivity.this, "기능없음", Toast.LENGTH_SHORT).show();
                        }

                        return false;
                    }
                });
                popupMenu.show();
            }
        });

        ImageView goMainImage = findViewById(R.id.gomainim2);
        goMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LogoutActivity.this, LogoutActivity.class); // CurrentActivity는 현재 액티비티의 이름으로 대체해주세요.
                startActivity(intent);
            }
        });


        /*logoutButton.setOnClickListener(v -> {
            Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);

            auth.signOut();

        });*/

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


        WebView webView = findViewById(R.id.webView1);
        String video = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/qYlzca11Tbc?si=Ag-quM45oygS79z5\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView.loadData(video, "text/html", "utf-8");
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());

        WebView webView2 = findViewById(R.id.webView2);
        String video2 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/FtemVq2qyco?si=IztkxZ3DIF8YCk-l\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView2.loadData(video2, "text/html", "utf-8");
        webView2.getSettings().setJavaScriptEnabled(true);
        webView2.setWebViewClient(new WebViewClient());

        WebView webView3 = findViewById(R.id.webView3);
        String video3 = "<iframe width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/W8BKuiwiy20?si=7JesEvcms93J6rtF\" title=\"YouTube video player\" frameborder=\"0\" allow=\"accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share\" allowfullscreen></iframe>";
        webView3.loadData(video3, "text/html", "utf-8");
        webView3.getSettings().setJavaScriptEnabled(true);
        webView3.setWebViewClient(new WebViewClient());

    }
}
