package com.example.vonga.motelroom;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

public class ChiTietPhongActivity extends AppCompatActivity {
    ImageView ivKhach, ivDien, ivChiTietSinhHoat, ivThanhToan;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_phong);
        addControls();
        addEvents();
    }

    private void addControls() {
        ivKhach = (ImageView) findViewById(R.id.ivKhach);
        ivDien = (ImageView) findViewById(R.id.ivDien);
        ivChiTietSinhHoat = (ImageView) findViewById(R.id.ivChiTietSinhHoat);
        ivThanhToan = (ImageView) findViewById(R.id.ivThanhToan);
    }

    private void addEvents() {
        ivKhach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietPhongActivity.this,KhachActivity.class);
                startActivity(intent);
            }
        });
        ivDien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietPhongActivity.this,PhiSinhHoatActivity.class);
                startActivity(intent);
            }
        });
        ivChiTietSinhHoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietPhongActivity.this,ChiTietPhiSinhHoatActivity.class);
                startActivity(intent);
            }
        });
        ivThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ChiTietPhongActivity.this,ThangThanhToanActivity.class);
                startActivity(intent);
            }
        });
    }
}
