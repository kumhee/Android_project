package com.example.ex04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class BookActivity extends AppCompatActivity {
    List<HashMap<String, Object>> list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book);

        getSupportActionBar().setTitle("도서검색");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        new KakaoThread().execute();
    }

    //카카오API를 호출하기 위한 Thread
    class KakaoThread extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... strings) {
            String url = "https://dapi.kakao.com/v3/search/book?target=title&query=안드로이드";
            String result = KakaoAPI.connect(url);
            System.out.println("............." + result);
            return result;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            bookParser(s);
            System.out.println("...........데이터갯수:" + list.size());
        }

        public void bookParser(String result) {
            try {
                JSONArray array = new JSONObject(result).getJSONArray("documents");
                for(int i=0; i<array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    HashMap<String, Object> map = new HashMap<>();
                    map.put("title", obj.getString("title"));
                    map.put("image", obj.getString("thumbnail"));
                    map.put("price", obj.getInt("price"));
                    map.put("contents", obj.getString("contents"));
                    map.put("publisher", obj.getString("publisher"));
                    map.put("authors", obj.getString("authors"));
                    list.add(map);
                }
            } catch (Exception e) {
                System.out.print("파싱:" + e.toString());
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

}