package com.example.vonga.motelroom;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class DangKyActivity extends AppCompatActivity {

    EditText txtUserName, txtPassword, txtEmail;
    Button btnDangKyActivity;
    public static String username;
    String pass,email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);
        addControls();
        addEvents();
    }

    private boolean validateInput(){
        if(txtUserName.getText().toString().equals("")){
            //Toast.makeText(DangKyActivity.this,"Tài khoản không được bỏ trống",Toast.LENGTH_SHORT).show();
            txtUserName.setError("Tài khoản không được bỏ trống");
            DangNhapActivity.showError(txtUserName,this);
            txtUserName.requestFocus();
            return false;
        }
        if(txtPassword.getText().toString().equals("")){
            //Toast.makeText(DangKyActivity.this,"Mật khẩu không được bỏ trống",Toast.LENGTH_SHORT).show();
            txtPassword.setError("Mật khẩu không được bỏ trống");
            DangNhapActivity.showError(txtPassword,this);
            txtPassword.requestFocus();
            return false;
        }
        if(txtEmail.getText().toString().equals("")){
            //Toast.makeText(DangKyActivity.this,"Email không được bỏ trống",Toast.LENGTH_SHORT).show();
            txtEmail.setError("Email không được bỏ trống");
            DangNhapActivity.showError(txtEmail,this);
            txtEmail.requestFocus();
            return false;
        }
        return true;
    }

    private void addControls() {
        txtUserName = (EditText) findViewById(R.id.txtUserNameDK);
        txtPassword = (EditText) findViewById(R.id.txtPasswordDK);
        txtEmail = (EditText) findViewById(R.id.txtEmailDK);
        txtUserName.setText("");
        txtPassword.setText("");
        txtEmail.setText("");
        btnDangKyActivity = (Button) findViewById(R.id.btnDangKyActivity);
    }

    private void addEvents() {
        btnDangKyActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateInput()){
                    return;
                }else{
                    username = txtUserName.getText().toString();
                    pass = txtPassword.getText().toString();
                    email = txtEmail.getText().toString();
                    LoginTask task= new LoginTask();
                    task.execute(username,pass,email);

                }
            }
        });
    }

    class LoginTask extends AsyncTask<String,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (aBoolean==false){
                Toast.makeText(DangKyActivity.this,"Tên tài khoản đã tồn tại",Toast.LENGTH_SHORT).show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(DangKyActivity.this);
                builder.setTitle("Thông báo");
                builder.setMessage("Bạn đã đăng ký thành công với tài khoản: "+txtUserName.getText());
                builder.setIcon(R.drawable.success_icon);

                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                        Intent myIntent = new Intent(DangKyActivity.this,DangNhapActivity.class);
                        startActivity(myIntent);
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try
            {
                URL url=new URL(DangNhapActivity.URL_CONNECT+"quanly/?username="+params[0]+"&pass="+params[1]+"&email="+params[2]);
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
