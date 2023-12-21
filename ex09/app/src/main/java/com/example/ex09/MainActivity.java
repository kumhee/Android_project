package com.example.ex09;

import static com.example.ex09.RemoteService.BASE_URL;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();
    RemoteService service = retrofit.create(RemoteService.class);

    EditText uid, upass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Login");

        uid = findViewById(R.id.uid);
        upass = findViewById(R.id.upass);

        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserVO vo = new UserVO();
                vo.setUid(uid.getText().toString());
                vo.setUpass(upass.getText().toString());

                Call<Integer> call = service.login(vo);
                call.enqueue(new Callback<Integer>() {
                    @Override
                    public void onResponse(Call<Integer> call, Response<Integer> response) {
                        AlertDialog.Builder box = new AlertDialog.Builder(MainActivity.this);
                        box.setTitle("🔔");
                        int result = response.body();
                        if(result == 2) {
                            box.setMessage("비밀번호가 일치하지 않습니다.");
                            box.setPositiveButton("확인", null);
                            box.show();
                        } else if (result == 0) {
                            box.setMessage("아이디가 존재하지 않습니다.");
                            box.setPositiveButton("확인", null);
                            box.show();
                        } else {
                            box.setMessage("로그인 성공");
                            box.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(MainActivity.this, ListActivity.class);
                                    startActivity(intent);
                                }
                            });
                            box.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Integer> call, Throwable t) {
                        System.out.println("dㅔ러.............." + t.toString());
                    }
                });
            }
        });
    }
}