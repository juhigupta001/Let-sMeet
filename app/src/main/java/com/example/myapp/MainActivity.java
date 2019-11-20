package com.example.myapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    Button mregisterBtn,mLoginBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("tag", "inside");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mregisterBtn=findViewById(R.id.register_btn);
        mLoginBtn=findViewById(R.id.login_btn);

        mregisterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //start register Activity
                startActivity(new Intent(MainActivity.this,RegisterActivity.class));
            }
        });
        mLoginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("tag", "login button clicked");
                //start register Activity
                startActivity(new Intent(MainActivity.this,LoginActivity.class));
            }
        });
    }
}
