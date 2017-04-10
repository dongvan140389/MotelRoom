package com.example.vonga.motelroom;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ThanhToanActivity extends Dialog {

    TextView txtSoKyDien, txtGiaDienThanhToan, txtSoKhoiNuoc, txtGiaNuocThanhToan, txtGiaPhongThanhToan;
    Button btnOKThanhToan;
    Activity activity;

    public ThanhToanActivity(Activity activity) {
        super(activity);
        this.activity = activity;
        setContentView(R.layout.activity_thanh_toan);
        setTitle("Thanh Toán");
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnOKThanhToan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThanhToanActivity.this.cancel();
            }
        });
    }

    private void addControls() {
        txtGiaDienThanhToan = (TextView) findViewById(R.id.txtGiaDienThanhToan);
        txtGiaNuocThanhToan = (TextView) findViewById(R.id.txtGiaNuocThanhToan);
        txtGiaPhongThanhToan = (TextView) findViewById(R.id.txtGiaPhongThanhToan);
        txtSoKhoiNuoc = (TextView) findViewById(R.id.txtSoKhoiNuoc);
        txtSoKyDien = (TextView) findViewById(R.id.txtSoKyDien);
        btnOKThanhToan = (Button) findViewById(R.id.btnOkThanhToan);
    }

}
