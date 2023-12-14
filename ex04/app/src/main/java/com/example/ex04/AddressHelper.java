package com.example.ex04;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class AddressHelper extends SQLiteOpenHelper {
    public AddressHelper(@Nullable Context context) {
        super(context, "address.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table address(_id integer primary key autoincrement, name text, phone text, juso text, photo text)");
        db.execSQL("insert into address values(null, '홍길동', '010-1010-1010', '인천 서구', '')");
        db.execSQL("insert into address values(null, '성춘향', '010-2020-2020', '서울 용산구', '')");
        db.execSQL("insert into address values(null, '이순신', '010-3030-3030', '인천 부평구', '')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}