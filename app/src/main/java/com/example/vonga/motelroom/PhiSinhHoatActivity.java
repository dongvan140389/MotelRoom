package com.example.vonga.motelroom;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class PhiSinhHoatActivity extends AppCompatActivity {
    EditText txtSoDienCu, txtSoDienMoi, txtGiaDien, txtSoNuocCu, txtSoNuocMoi, txtGiaNuoc
            ,txtGiaPhong,txtThang;
    Button btnThang, btnThemSinhHoat;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat sdf = new SimpleDateFormat("MM/yyyy");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phi_sinh_hoat);
        addControls();
        addEvents();
    }

    private boolean validateInput(){
        if(txtSoDienCu.getText().toString().equals("")){
            //Toast.makeText(PhiSinhHoatActivity.this,"Số điện cũ không được bỏ trống",Toast.LENGTH_SHORT).show();
            txtSoDienCu.setError("Số điện cũ không được bỏ trống");
            DangNhapActivity.showError(txtSoDienCu,this);
            txtSoDienCu.requestFocus();
            return false;
        }
        if(txtSoDienMoi.getText().toString().equals("")){
            //Toast.makeText(PhiSinhHoatActivity.this,"Số điện mới không được bỏ trống",Toast.LENGTH_SHORT).show();
            txtSoDienMoi.setError("Số điện mới không được bỏ trống");
            DangNhapActivity.showError(txtSoDienMoi,this);
            txtSoDienMoi.requestFocus();
            return false;
        }
        if(txtGiaDien.getText().toString().equals("")){
            //Toast.makeText(PhiSinhHoatActivity.this,"Giá điện không được bỏ trống",Toast.LENGTH_SHORT).show();
            txtGiaDien.setError("Giá điện không được bỏ trống");
            DangNhapActivity.showError(txtGiaDien,this);
            txtGiaDien.requestFocus();
            return false;
        }
        if(txtSoNuocCu.getText().toString().equals("")){
            //Toast.makeText(PhiSinhHoatActivity.this,"Số nước cũ không được bỏ trống",Toast.LENGTH_SHORT).show();
            txtSoNuocCu.setError("Số nước cũ không được bỏ trống");
            DangNhapActivity.showError(txtSoNuocCu,this);
            txtSoNuocCu.requestFocus();
            return false;
        }
        if(txtSoNuocMoi.getText().toString().equals("")){
            //Toast.makeText(PhiSinhHoatActivity.this,"Số nước mới không được bỏ trống",Toast.LENGTH_SHORT).show();
            txtSoNuocMoi.setError("Số nước mới không được bỏ trống");
            DangNhapActivity.showError(txtSoNuocMoi,this);
            txtSoNuocMoi.requestFocus();
            return false;
        }
        if(txtGiaNuoc.getText().toString().equals("")){
            //Toast.makeText(PhiSinhHoatActivity.this,"Giá nước không được bỏ trống",Toast.LENGTH_SHORT).show();
            txtGiaNuoc.setError("Giá nước không được bỏ trống");
            DangNhapActivity.showError(txtGiaNuoc,this);
            txtGiaNuoc.requestFocus();
            return false;
        }
        if(txtGiaPhong.getText().toString().equals("")){
            //Toast.makeText(PhiSinhHoatActivity.this,"Giá phòng không được bỏ trống",Toast.LENGTH_SHORT).show();
            txtGiaPhong.setError("Giá phòng không được bỏ trống");
            DangNhapActivity.showError(txtGiaPhong,this);
            txtGiaPhong.requestFocus();
            return false;
        }
        if(txtThang.getText().toString().equals("")){
            //Toast.makeText(PhiSinhHoatActivity.this,"Vui lòng chọn tháng",Toast.LENGTH_SHORT).show();
            txtThang.setError("Vui lòng chọn tháng");
            DangNhapActivity.showError(txtThang,this);
            txtThang.requestFocus();
            return false;
        }
        return true;
    }
    private void addControls() {
        txtGiaDien = (EditText) findViewById(R.id.txtGiaDien);
        txtGiaNuoc = (EditText) findViewById(R.id.txtGiaNuoc);
        txtGiaPhong = (EditText) findViewById(R.id.txtGiaPhong);
        txtSoDienCu = (EditText) findViewById(R.id.txtSoDienCu);
        txtSoDienMoi = (EditText) findViewById(R.id.txtSoDienMoi);
        txtSoNuocCu = (EditText) findViewById(R.id.txtSoNuocCu);
        txtSoNuocMoi = (EditText) findViewById(R.id.txtSoNuocMoi);
        txtThang = (EditText) findViewById(R.id.txtThang);
        btnThang = (Button) findViewById(R.id.btnThang);
        btnThemSinhHoat = (Button) findViewById(R.id.btnThemSinhHoat);
    }

    private void addEvents() {
        btnThang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });
        btnThemSinhHoat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateInput()){
                    return;
                }else {
                    ThemDienTask task1 = new ThemDienTask();
                    ThemNuocTask task2 = new ThemNuocTask();
                    ThemGiaTask task3 = new ThemGiaTask();
                    String thang = txtThang.getText().toString();
                    String maphong = MainActivity.p.getMa_Phong() + "";

                    task1.execute(thang, txtSoDienCu.getText().toString(), txtSoDienMoi.getText().toString(), maphong, txtGiaDien.getText().toString());
                    task2.execute(thang, txtSoNuocCu.getText().toString(), txtSoNuocMoi.getText().toString(), maphong, txtGiaNuoc.getText().toString());
                    task3.execute(thang, txtGiaPhong.getText().toString(), maphong);
                    Intent i = new Intent(PhiSinhHoatActivity.this, ChiTietPhongActivity.class);
                    startActivity(i);
                }
            }
        });
    }

    private void showDatePicker() {
        final DatePickerDialog.OnDateSetListener callBack = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                calendar.set(year, monthOfYear, dayOfMonth);
                txtThang.setText(sdf.format(calendar.getTime()));
            }
        };
        DatePickerDialog dialog = new DatePickerDialog(PhiSinhHoatActivity.this,
                callBack,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));
        dialog.show();
    }

    class ThemDienTask extends AsyncTask<String,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try
            {
                Log.d("thang: ",params[0]);
                Log.d("sodiencu: ",params[1]);
                Log.d("sodienmoi: ",params[2]);
                Log.d("maphong: ",params[3]);

                //String trangthai = URLEncoder.encode(params[1],"UTF-8");
                URL url=new URL(DangNhapActivity.URL_CONNECT+"dien/?thang="+params[0]+
                        "&sodiencu="+Integer.parseInt(params[1])+
                        "&sodienmoi="+Integer.parseInt(params[2])+
                        "&maphong="+Integer.parseInt(params[3])+
                        "&giadien="+Double.parseDouble(params[4]));
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
                InputStream inputStream=connection.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"UTF-8");
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                StringBuilder builder=new StringBuilder();
                String line=bufferedReader.readLine();
                while (line!=null)
                {
                    builder.append(line);
                    line=bufferedReader.readLine();
                }
                boolean kt=builder.toString().contains("true");
                return kt;
            }
            catch (Exception ex)
            {
                Log.e("LOI",ex.toString());
            }
            return null;
        }
    }

    class ThemNuocTask extends AsyncTask<String,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try
            {
                Log.d("thang: ",params[0]);
                Log.d("sonuoccu: ",params[1]);
                Log.d("sonuocmoi: ",params[2]);
                Log.d("maphong: ",params[3]);

                //String trangthai = URLEncoder.encode(params[1],"UTF-8");
                URL url=new URL(DangNhapActivity.URL_CONNECT+"nuoc/?thang="+params[0]+
                        "&sonuoccu="+Integer.parseInt(params[1])+
                        "&sonuocmoi="+Integer.parseInt(params[2])+
                        "&maphong="+Integer.parseInt(params[3])+
                        "&gianuoc="+Double.parseDouble(params[4]));
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
                InputStream inputStream=connection.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"UTF-8");
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                StringBuilder builder=new StringBuilder();
                String line=bufferedReader.readLine();
                while (line!=null)
                {
                    builder.append(line);
                    line=bufferedReader.readLine();
                }
                boolean kt=builder.toString().contains("true");
                return kt;
            }
            catch (Exception ex)
            {
                Log.e("LOI",ex.toString());
            }
            return null;
        }
    }

    class ThemGiaTask extends AsyncTask<String,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try
            {
                Log.d("thang: ",params[0]);
                Log.d("gia: ",params[1]);

                Log.d("maphong: ",params[2]);

                //String trangthai = URLEncoder.encode(params[1],"UTF-8");
                URL url=new URL(DangNhapActivity.URL_CONNECT+"giaphong/?thang="+params[0]+
                        "&gia="+Integer.parseInt(params[1])+
                        "&maphong="+Integer.parseInt(params[2]));
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type","application/json;charset=UTF-8");
                InputStream inputStream=connection.getInputStream();
                InputStreamReader inputStreamReader=new InputStreamReader(inputStream,"UTF-8");
                BufferedReader bufferedReader=new BufferedReader(inputStreamReader);
                StringBuilder builder=new StringBuilder();
                String line=bufferedReader.readLine();
                while (line!=null)
                {
                    builder.append(line);
                    line=bufferedReader.readLine();
                }
                boolean kt=builder.toString().contains("true");
                return kt;
            }
            catch (Exception ex)
            {
                Log.e("LOI",ex.toString());
            }
            return null;
        }
    }
}
