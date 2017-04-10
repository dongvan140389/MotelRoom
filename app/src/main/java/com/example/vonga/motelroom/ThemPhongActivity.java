package com.example.vonga.motelroom;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.example.vonga.model.Phong;
import com.example.vonga.model.QuanLy;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class ThemPhongActivity extends Dialog {
    EditText txtTenPhongDialog, txtGiaPhongDialog;
    Button btnThemDialog, btnThoatDialog;
    Activity activity;

    private ProgressDialog pDialog;

    public ThemPhongActivity(Activity activity) {
        super(activity);
        this.activity = activity;
        setContentView(R.layout.activity_them_phong);
        setTitle("Thêm Phòng");
        addControls();
        addEvents();
    }

    private void addControls() {
        txtTenPhongDialog = (EditText) findViewById(R.id.txtTenPhongDialog);
        btnThemDialog = (Button) findViewById(R.id.btnThemDialog);
        btnThoatDialog = (Button) findViewById(R.id.btnThoatDialog);
    }

    private boolean validateInput(){
        if(txtTenPhongDialog.getText().toString().equals("")){
            //Toast.makeText(activity,"Tên phòng không được bỏ trống",Toast.LENGTH_SHORT).show();
            txtTenPhongDialog.setError("Tên phòng không được bỏ trống");
            DangNhapActivity.showError(txtTenPhongDialog,activity);
            txtTenPhongDialog.requestFocus();
            return false;
        }
        return true;
    }

    private void addEvents() {
        btnThoatDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemPhongActivity.this.cancel();
            }
        });
        btnThemDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateInput()){
                    return;
                }else {
                    String ten = txtTenPhongDialog.getText().toString();
                    ThemPhomgTask task = new ThemPhomgTask();
                    task.execute(MainActivity.ql.getUsername(), ten, "0");
                    ThemPhongActivity.this.cancel();
                }
            }
        });
    }

    class ThemPhomgTask extends AsyncTask<String,Void,Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            listPhong task = new listPhong();
            task.execute(MainActivity.ql);
            pDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(String... params) {
            try
            {
                Log.d("User_name: ",params[0]);
                Log.d("Ten Phong: ",params[1]);
                Log.d("Trang Thai: ",params[2]);
                String ten = URLEncoder.encode(params[1],"UTF-8");
                //String trangthai = URLEncoder.encode(params[1],"UTF-8");
                URL url=new URL(DangNhapActivity.URL_CONNECT+"room/?username="+params[0]+"&tenphong="+ten+"&trangthai="+params[2]);
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

    class listPhong extends AsyncTask<QuanLy,Void,ArrayList<Phong>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            MainActivity.adapter.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<Phong> phongs) {
            super.onPostExecute(phongs);
            MainActivity.adapter.clear();
            MainActivity.adapter.addAll(phongs);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Phong> doInBackground(QuanLy... params) {
            ArrayList<Phong> dsPhong = new ArrayList<>();
            try {
                URL url = new URL(DangNhapActivity.URL_CONNECT+"room/?username="+params[0].getUsername());

                //mo ket noi
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json;charset = utf8");
                InputStreamReader isr = new InputStreamReader(connection.getInputStream(), "UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = br.readLine();
                while (line != null) {
                    builder.append(line);
                    line = br.readLine();
                }

                JSONArray jsonArray = new JSONArray(builder.toString());
                //tien hanh mo hinh hoa nguoc tu jsonArray ve mo hinh lop
                for (int i = 0; i < jsonArray.length(); i++) {
                    try {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Phong p = new Phong();
                        p.setMa_Phong(jsonObject.getInt("Ma_Phong"));
                        p.setTen_Phong(jsonObject.getString("Ten_Phong"));
                        p.setTrang_Thai(jsonObject.getInt("Trang_Thai"));
                        dsPhong.add(p);
                    } catch (Exception ex) {
                        Log.e("LOI LISTPHONG 2", "i = " + i + ex.toString());
                    }

                }

            } catch (Exception ex) {
                Log.e("LOI LISTPHONG 2", ex.toString());
            }
            return dsPhong;

        }
    }
}
