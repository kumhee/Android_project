package com.example.ex03;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    // AddressHelper 클래스의 인스턴스와 SQLiteDatabase 인스턴스 선언
    AddressHelper helper;
    SQLiteDatabase db;
    String sql = "select _id, name, phone, juso, photo from address";
    JusoAdapter jusoAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 액션바(앱의 상단바) 설정
        getSupportActionBar().setTitle("주소관리"); // 앱 제목 설정
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // 뒤로가기 버튼 표시

        // AddressHelper를 사용하여 데이터베이스를 열기
        helper = new AddressHelper(this);
        db = helper.getReadableDatabase();

        //데이터 생성
        Cursor cursor = db.rawQuery(sql, null);

        //어댑터 생성
        jusoAdapter = new JusoAdapter(this, cursor);

        //ListView에 어댑터 연결
        ListView list = findViewById(R.id.list);
        list.setAdapter(jusoAdapter);

        //버튼 클릭하면 주소등록으로 이동
        findViewById(R.id.btnInsert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, InsertActivity.class);
                startActivity(intent);
            }
        });

    }//onCreate

    // 액션바의 아이템(여기서는 뒤로가기 버튼) 클릭 시 호출되는 메서드
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        sql = "select _id, name, phone, juso, photo from address";

        // 뒤로가기 버튼이 클릭되었을 때
        if(item.getItemId() == android.R.id.home) {
            finish();
        }else if(item.getItemId() == R.id.name) {
            sql += " order by name";
        }else if(item.getItemId() == R.id.phone) {
            sql += " order by phone";
        }else if(item.getItemId() == R.id.juso) {
            sql += " order by juso";
        }else if(item.getItemId() == R.id.photo) {
            sql += " order by photo";
        }
        onRestart();

        return super.onOptionsItemSelected(item);
    }
    class JusoAdapter extends CursorAdapter {

        //생성자
        public JusoAdapter(Context context, Cursor c) {
            super(context, c);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            View view = getLayoutInflater().inflate(R.layout.item_address, parent, false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {

            int id = cursor.getInt(0);

            TextView name = view.findViewById(R.id.name);
            name.setText(cursor.getString(1));

            TextView phone = view.findViewById(R.id.phone);
            phone.setText(cursor.getString(2));

            TextView juso = view.findViewById(R.id.juso);
            juso.setText(cursor.getString(3));

            CircleImageView photo = view.findViewById(R.id.photo);
            String strPhoto = cursor.getString(4);
            if(strPhoto.equals("")) {
                // 만약 사진이 없다면 기본 이미지를 설정
                photo.setImageResource(R.drawable.baseprofile);
            }else {
                photo.setImageURI(Uri.parse(strPhoto));
            }


            //Delete버튼
            view.findViewById(R.id.btnDelete).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("질의")
                            .setMessage(id + "번 주소를 삭제하시겠습니까?")
                            .setPositiveButton("예", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String sql = "delete from address where _id = " + id;
                                    db.execSQL(sql);
                                    onRestart();
                                }
                            })
                            .setNegativeButton("아니오", null)
                            .show();
                }
            });
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, UpdateActivity.class);
                    intent.putExtra("id", id);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Cursor cursor = db.rawQuery(sql, null);
        jusoAdapter.changeCursor(cursor);
    }

    //메뉴등록
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchView searchView = (SearchView)menu.findItem(R.id.search).getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                sql = "select _id, name, phone, juso from address ";
                sql += "where name like '%" + query + "%' or ";
                sql += "phone like '%" + query + "%' or ";
                sql += "juso like '%" + query + "%' or ";
                sql += "photo like '%" + query + "%'";
                onRestart();
                return false;
            }


            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
}//Activity
