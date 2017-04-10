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
import com.example.vonga.model.Khach;
import com.example.vonga.model.Phong;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class UpdateKhachActivity extends Dialog {
    EditText txtTenKhachUpdate, txtCMNDUpdate;
    Button btnUpdateKhach;
    Activity activity;
    public static int maKhachUpdate, maPhongCuaKhach;
    private ProgressDialog pDialog;
    public UpdateKhachActivity(Activity activity) {
        super(activity);
        this.activity = activity;
        setContentView(R.layout.activity_update_khach);
        setTitle("Sửa Thông Tin Khách");
        addControls();
        addEvents();
    }

    private boolean validateInput(){
        if(txtTenKhachUpdate.getText().toString().equals("")){
            //Toast.makeText(activity,"Tên khách không được bỏ trống",Toast.LENGTH_SHORT).show();
            txtTenKhachUpdate.setError("Tên khách không được bỏ trống");
            DangNhapActivity.showError(txtTenKhachUpdate,activity);
            txtTenKhachUpdate.requestFocus();
            return false;
        }
        if(txtCMNDUpdate.getText().toString().equals("")){
            //Toast.makeText(activity,"CMND không được bỏ trống",Toast.LENGTH_SHORT).show();
            txtCMNDUpdate.setError("CMND không được bỏ trống");
            DangNhapActivity.showError(txtCMNDUpdate,activity);
            txtCMNDUpdate.requestFocus();
            return false;
        }
        return true;
    }
    private void addEvents() {
        btnUpdateKhach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateInput()){
                    return;
                }else {
                    String ten = txtTenKhachUpdate.getText().toString();
                    String cmnd = txtCMNDUpdate.getText().toString();
                    Khach k = new Khach(maKhachUpdate, ten, cmnd, maPhongCuaKhach);
                    UpdateKhachTask task = new UpdateKhachTask();
                    task.execute(k);
                    UpdateKhachActivity.this.dismiss();
                }
            }
        });
    }

    private void addControls() {
        txtTenKhachUpdate = (EditText) findViewById(R.id.txtTenKhachUpdate);
        txtCMNDUpdate = (EditText) findViewById(R.id.txtCMNDUpdate);
        btnUpdateKhach = (Button) findViewById(R.id.btnUpdateKhach);
    }

    class UpdateKhachTask extends AsyncTask<Khach,Void,Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(activity);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            ListKhachTask task = new ListKhachTask();
            task.execute(MainActivity.p);
            pDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(Khach... params) {
            try
            {
                Log.e("Ma Khach ",maKhachUpdate+"");
                String ten = URLEncoder.encode(params[0].getTen_Khach(),"UTF-8");
                //String trangthai = URLEncoder.encode(params[1],"UTF-8");
                URL url=new URL(DangNhapActivity.URL_CONNECT+"khach/?makhach="+params[0].getMa_Khach()+"&tenkhach="+ten+"&cmnd="+params[0].getCMND());
                HttpURLConnection connection= (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("PUT");
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
    class ListKhachTask extends AsyncTask<Phong,Void,ArrayList<Khach>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            KhachActivity.adapter.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<Khach> khaches) {
            super.onPostExecute(khaches);
            KhachActivity.adapter.clear();
            KhachActivity.adapter.addAll(khaches);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Khach> doInBackground(Phong... params) {
            ArrayList<Khach> dsKhach = new ArrayList<>();
            try{
                URL url = new URL(DangNhapActivity.URL_CONNECT+"khach/?maphong="+params[0].getMa_Phong());

                //mo ket noi
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type","application/json;charset = utf8");
                InputStreamReader isr = new InputStreamReader(connection.getInputStream(),"UTF-8");
                BufferedReader br = new BufferedReader(isr);
                StringBuilder builder = new StringBuilder();
                String line = br.readLine();
                while(line!=null){
                    builder.append(line);
                    line=br.readLine();
                }

                JSONArray jsonArray = new JSONArray(builder.toString());
                //tien hanh mo hinh hoa nguoc tu jsonArray ve mo hinh lop DiaDiem
                for(int i=0;i<jsonArray.length();i++){
                    try{
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Khach k = new Khach();
                        k.setMa_Khach(jsonObject.getInt("Ma_Khach"));
                        k.setTen_Khach(jsonObject.getString("Ten_Khach"));
                        k.setCMND(jsonObject.getString("CMND"));
                        k.setMa_Phong(jsonObject.getInt("Ma_Phong"));
                        dsKhach.add(k);
                    }catch (Exception ex){
                        Log.e("LOI LIST KHACH 1","i = "+i+ex.toString());
                    }

                }

            }catch (Exception ex){
                Log.e("Loi",ex.toString());
            }
            return dsKhach;

        }
    }
}
