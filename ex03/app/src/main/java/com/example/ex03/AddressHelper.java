package com.example.ex03;

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
        db.execSQL("create table address(_id integer primary key autoincrement, name text, phone text, juso text)");
        db.execSQL("insert into address values(null, '홍길동', '010-1010-1010', '서울시 마포구 도화동 우성아파트')");
        db.execSQL("insert into address values(null, '강감찬', '010-2020-2020', '서울시 강남구 압구정동 현대아파트')");
        db.execSQL("insert into address values(null, '김유신', '010-3030-3030', '경기도 김포시 운양동 푸르지오')");
    }

    // 데이터베이스 버전이 업그레이드될 때 호출되는 메서드
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
