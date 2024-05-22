package com.example.secondhandtransaction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import java.util.Timer;
import java.util.TimerTask;

public class HelloActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello);
        FrameLayout view = (FrameLayout) findViewById(R.id.page_cl);
        view.postDelayed(() -> {
            Intent localIntent = new Intent(this, LoginActivity.class);//你要转向的Activity
            startActivity(localIntent);
            finish();
        }, 2000);
    }
}