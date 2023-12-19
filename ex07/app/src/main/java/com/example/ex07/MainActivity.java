package com.example.ex07;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    EditText edtEmail, edtPassword;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth =  FirebaseAuth.getInstance(); //mAuth로 회원가입을 하거나 새로운 회원을 등록할 수 있음.

        getSupportActionBar().setTitle("로그인");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        edtEmail=(EditText)findViewById(R.id.edtEmail);
        edtPassword=(EditText)findViewById(R.id.edtPassword);

        findViewById(R.id.btnJoin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if(password.length() < 8){
                    Toast.makeText(MainActivity.this,"비밀번호를 8자리이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if(!email.contains("@")) {
                    Toast.makeText(MainActivity.this, "이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                } else {
                    onJoin(email, password);
                }
            }
        });

        //로그인
        findViewById(R.id.btnLogin).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = edtEmail.getText().toString();
                String password = edtPassword.getText().toString();

                if(password.length() < 8){
                    Toast.makeText(MainActivity.this,"비밀번호를 8자리이상 입력해주세요.", Toast.LENGTH_SHORT).show();
                } else if(!email.contains("@")) {
                    Toast.makeText(MainActivity.this, "이메일 형식이 아닙니다.", Toast.LENGTH_SHORT).show();
                } else {
                    onLogin(email, password);
                }
            }
        });
    }//onCreate

    //로그인
    public void onLogin(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "로그인성공", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, MenuActivity.class);
                            startActivity(intent);
                            //finish();
                        } else {
                            Toast.makeText(MainActivity.this, "로그인실패", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //회원가입
    public void onJoin(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(MainActivity.this, new OnCompleteListener<AuthResult>() {
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(MainActivity.this, "사용자등록성공", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "사용자등록실패", Toast.LENGTH_SHORT).show();
                        }
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