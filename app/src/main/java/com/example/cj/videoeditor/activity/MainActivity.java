package com.example.cj.videoeditor.activity;

import android.Manifest.permission;
import android.content.Intent;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.cj.videoeditor.R;

public class MainActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button recordBtn = (Button) findViewById(R.id.record_activity);
        Button selectBtn = (Button) findViewById(R.id.select_activity);
        Button audioBtn = (Button) findViewById(R.id.audio_activity);
        Button videoBtn = (Button) findViewById(R.id.video_connect);

        recordBtn.setOnClickListener(this);
        selectBtn.setOnClickListener(this);
        audioBtn.setOnClickListener(this);
        videoBtn.setOnClickListener(this);

        if (VERSION.SDK_INT >= 23) {
            requestPermissions(new String[]{permission.RECORD_AUDIO, permission.CAMERA, permission.WRITE_EXTERNAL_STORAGE,
                permission.READ_EXTERNAL_STORAGE}, 1001);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.record_activity:
                startActivity(new Intent(MainActivity.this , RecordedActivity.class));
                break;
            case R.id.select_activity:
                VideoSelectActivity.openActivity(this);
                break;
            case R.id.audio_activity:
                startActivity(new Intent(MainActivity.this , AudioEditorActivity.class));
                break;
            case R.id.video_connect:
//                Toast.makeText(this,"该功能还未完成！！！",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this , VideoConnectActivity.class));
                break;
        }
    }
}
