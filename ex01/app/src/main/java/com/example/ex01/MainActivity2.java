package com.example.ex01;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class MainActivity2 extends AppCompatActivity {

    int count = 0;
    TextView txtCount,txtFruit; //txtFruit

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        txtCount = findViewById(R.id.count);
        txtFruit = findViewById(R.id.fruit);//txtFruit

        findViewById(R.id.btnin).setOnClickListener(onClick);
        findViewById(R.id.btnde).setOnClickListener(onClick);

        findViewById(R.id.btnin).setOnLongClickListener((onLongClick));
        findViewById(R.id.btnde).setOnLongClickListener((onLongClick));

        //orange버튼 이벤트추가
        findViewById(R.id.orange).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtFruit.setText("orange");
            }
        });
        //apple 이벤트추가
        findViewById(R.id.apple).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                txtFruit.setText("apple");
            }
        });
    }

    View.OnClickListener onClick = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(v.getId() == R.id.btnin) {
                count ++;
            } else if (v.getId() == R.id.btnde) {
                count --;
            }
            txtCount.setText(String.valueOf(count));
        }
    };

    View.OnLongClickListener onLongClick = new View.OnLongClickListener() {
        @Override
        public boolean onLongClick(View v) {
            if(v.getId() == R.id.btnin) {
                count = 100;
            } else {
                count = 0;
            }
            txtCount.setText(String.valueOf(count));
            return true;
        }
    };
}