package com.example.vonga.motelroom;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.vonga.model.QuanLy;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DangNhapActivity extends AppCompatActivity {

    public static String URL_CONNECT = "http://192.168.56.1/motelroom/api/";

    EditText txtTaiKhoanDangNhap, txtPasswordDangNhap;
    Button btnDangNhapActivity;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_nhap);
        addControls();
        addEvents();
    }

    private void addControls() {
        txtTaiKhoanDangNhap = (EditText) findViewById(R.id.txtTaiKhoanDangNhap);
        txtPasswordDangNhap = (EditText) findViewById(R.id.txtPasswordDangNhap);
        btnDangNhapActivity = (Button) findViewById(R.id.btnDangNhapActivity);
        txtTaiKhoanDangNhap.requestFocus();
    }

    private void addEvents() {
        btnDangNhapActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateInput()){
                    return;
                }else{
                    String username = txtTaiKhoanDangNhap.getText().toString();
                    String pass = txtPasswordDangNhap.getText().toString();
//                    Toast.makeText(DangNhapActivity.this,"Đăng nhập thành công",Toast.LENGTH_SHORT).show();
//                    Intent myIntent = new Intent(DangNhapActivity.this,MainActivity.class);
//                    startActivity(myIntent);
                     DangNhapTask task = new DangNhapTask();
                     task.execute(username,pass);
                }
            }
        });
    }

    private boolean validateInput(){
        if(txtTaiKhoanDangNhap.getText().toString().equals("")){
            txtTaiKhoanDangNhap.setError("Tài khoản không được bỏ trống");
            showError(txtTaiKhoanDangNhap,this);
            txtTaiKhoanDangNhap.requestFocus();
            return false;
        }
        if(txtPasswordDangNhap.getText().toString().equals("")){
            txtPasswordDangNhap.setError("Mật khẩu không được bỏ trống");
            showError(txtPasswordDangNhap,this);
            txtPasswordDangNhap.requestFocus();
            return false;
        }
        return true;
    }

    public static void showError(EditText txtError, Activity activity) {
        Animation shake = AnimationUtils.loadAnimation(activity, R.anim.shake_anim);
        txtError.startAnimation(shake);
    }


    class DangNhapTask extends AsyncTask<String,Void,QuanLy>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(DangNhapActivity.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(QuanLy quanLy) {
            super.onPostExecute(quanLy);

            if(quanLy==null){
                Toast.makeText(DangNhapActivity.this,"Sai Tên tài khoản hay mật khẩu rồi",Toast.LENGTH_SHORT).show();
                pDialog.dismiss();
            }else{

                pDialog.dismiss();Intent i = new Intent(DangNhapActivity.this,MainActivity.class);
                i.putExtra("QUANLY",quanLy);
                startActivity(i);
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected QuanLy doInBackground(String... params) {
            try
            {
                URL url=new URL(URL_CONNECT+"quanly/?username="+params[0]+"&pass="+params[1]);
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
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
                Log.d("User ",builder.toString());
                JSONObject jsonObject = new JSONObject(builder.toString());
                QuanLy ql = new QuanLy();
                ql.setUsername(jsonObject.getString("User_Name"));
                ql.setPassword(jsonObject.getString("Pass_Word"));
                ql.setEmail(jsonObject.getString("Email"));
                Log.d("User ",ql.getUsername().toString());
                Log.d("Pass ",ql.getPassword().toString());
                return ql;
            }
            catch (Exception ex)
            {
                Log.e("LOI",ex.toString());
            }
            return null;
        }
    }
}
