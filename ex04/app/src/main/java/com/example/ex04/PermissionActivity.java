package com.example.ex04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.example.ex04.MainActivity;

import java.util.ArrayList;

public class PermissionActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);

        // 앱이 실행될 때 권한을 확인하고 부여되지 않은 경우 권한 요청
        if (checkAndRequestPermission()) {
            // 권한이 이미 모두 부여되었으면 MainActivity로 이동
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
    }

    // 권한을 확인하고 권한이 부여되어 있지 않으면 권한을 요청한다.
    private boolean checkAndRequestPermission() {
        String[] permissions = {
                Manifest.permission.READ_MEDIA_IMAGES,
                Manifest.permission.READ_MEDIA_VIDEO,
                Manifest.permission.READ_MEDIA_AUDIO
        };
        ArrayList<String> permissionNeeded = new ArrayList<>();

        // 권한이 부여되지 않은 항목을 확인하고 목록에 추가
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionNeeded.add(permission);
            }
        }

        if (!permissionNeeded.isEmpty()) {
            // 요청할 권한이 있는 경우 권한 요청
            ActivityCompat.requestPermissions(this,
                    permissionNeeded.toArray(new String[permissionNeeded.size()]), 100);
            return false; // 권한이 부여되지 않았음을 나타냄
        }
        return true; // 모든 권한이 이미 부여됨을 나타냄
    }

    // 권한 요청 결과를 처리하는 메소드
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 모든 권한이 부여되었는지 여부 확인
        boolean isAllGranted = true;
        for (int i = 0; i < permissions.length; i++) {
            if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                isAllGranted = false;
            }
        }

        if (isAllGranted) {
            // 권한이 모두 부여되었으면 MainActivity로 이동
            Intent intent = new Intent(PermissionActivity.this, MainActivity.class);
            startActivity(intent);
        } else {
            // 일부 또는 모든 권한이 부여되지 않았을 때 사용자에게 알림 표시
            new AlertDialog.Builder(this)
                    .setTitle("알림")
                    .setMessage("권한 설정이 필요합니다.")
                    .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // 권한 설정이 필요하다는 알림을 확인한 후 현재 액티비티를 종료
                            finish();
                        }
                    })
                    .show();
        }
    }
}
