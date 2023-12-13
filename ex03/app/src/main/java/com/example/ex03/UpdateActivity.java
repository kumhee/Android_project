package com.example.ex03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateActivity extends AppCompatActivity {

    AddressHelper helper;
    SQLiteDatabase db;
    EditText name, phone, juso;
    CircleImageView photo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert);

        getSupportActionBar().setTitle("정보수정");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기
        Button btnUpdate = findViewById(R.id.btnInsert);
        btnUpdate.setText("수정");

        // 정보수정으로 갔을 때 읽어온 값 넣어주기
        Intent intent = getIntent();
        int id = intent.getIntExtra("id", 0);

        helper = new AddressHelper(this);
        db = helper.getWritableDatabase();

        // 결과 넣어주기
        Cursor cursor = db.rawQuery("select _id, name, phone, juso, photo from address where _id=" + id, null);

        name = findViewById(R.id.name);
        phone = findViewById(R.id.phone);
        juso = findViewById(R.id.juso);
        photo = findViewById(R.id.photo);

        if (cursor.moveToFirst()) {
            name.setText(cursor.getString(1));
            phone.setText(cursor.getString(2));
            juso.setText(cursor.getString(3));

            // 사진 처리 추가
            String strPhoto = cursor.getString(4);
            if(strPhoto.equals("")) {
                photo.setImageResource(R.drawable.baseprofile);
            }else {
                photo.setImageURI(Uri.parse(cursor.getString(4)));
            }
        }

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UpdateActivity.this)
                        .setTitle("질의")
                        .setMessage(id + "번 정보를 수정하시겠습니까?")
                        .setPositiveButton("예", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String sql = "update address set ";
                                sql += "name='" + name.getText().toString() + "',";
                                sql += "phone='" + phone.getText().toString() + "',";
                                sql += "juso='" + juso.getText().toString() + "' ";
                                sql += "where _id=" + id;
                                db.execSQL(sql);
                                finish();
                            }
                        })
                        .setNegativeButton("아니오", null)
                        .show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
