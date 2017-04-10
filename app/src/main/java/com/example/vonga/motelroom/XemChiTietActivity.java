package com.example.vonga.motelroom;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class XemChiTietActivity extends Dialog {
    TextView txtChiTietSoDienCu, txtChiTietSoDienMoi, txtChiTietSoNuocCu, txtChiTietSoNuocMoi, txtChiTietGiaPhong;
    Button btnOKChiTiet;
    Activity activity;
    public XemChiTietActivity(Activity activity) {
        super(activity);
        this.activity = activity;
        setContentView(R.layout.activity_xem_chi_tiet);
        setTitle("Chi tiết sinh hoạt");
        addControls();
        addEvents();
    }

    private void addEvents() {
        btnOKChiTiet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XemChiTietActivity.this.dismiss();
            }
        });
    }

    private void addControls() {
        txtChiTietGiaPhong = (TextView) findViewById(R.id.txtChiTietGiaPhong);
        txtChiTietSoDienCu = (TextView) findViewById(R.id.txtChiTietSoDienCu);
        txtChiTietSoDienMoi = (TextView) findViewById(R.id.txtChiTietSoDienMoi);
        txtChiTietSoNuocCu = (TextView) findViewById(R.id.txtChiTietSoNuocCu);
        txtChiTietSoNuocMoi = (TextView) findViewById(R.id.txtChiTietSoNuocMoi);
        btnOKChiTiet = (Button) findViewById(R.id.btnOkChiTiet);
    }

}
