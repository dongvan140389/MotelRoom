package com.example.vonga.motelroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

public class StartActivity extends AppCompatActivity {

    Button btnDangNhap, btnDangKy;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        addControls();
        addEvents();
    }


    private void addControls() {
        btnDangNhap = (Button) findViewById(R.id.btnDangNhap);
        btnDangKy = (Button) findViewById(R.id.btnDangKy);
    }

    private void addEvents() {
        btnDangNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOpenDangNhapActivity();
            }
        });

        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOpenDanhKyActivity();
            }
        });
    }

    private void doOpenDangNhapActivity() {
        Intent myIntent = new Intent(StartActivity.this,DangNhapActivity.class);
        startActivity(myIntent);
    }

    private void doOpenDanhKyActivity() {
        Intent myIntent = new Intent(StartActivity.this,DangKyActivity.class);
        startActivity(myIntent);
    }

}