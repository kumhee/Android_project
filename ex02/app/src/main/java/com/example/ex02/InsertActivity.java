package com.example.ex02;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class InsertActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        getSupportActionBar().setTitle("상품등록");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}