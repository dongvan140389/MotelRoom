package com.example.vonga.motelroom;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
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

public class ThemKhachActivity extends Dialog{

    EditText txtTenKhachThem, txtCMNDThem, txtDienThoaiThem;
    Button btnThemKhach;
    ImageView ivKhachThem;
    Activity activity;
    private ProgressDialog pDialog;

    public ThemKhachActivity(Activity activity) {
        super(activity);
        this.activity = activity;
        setContentView(R.layout.activity_them_nhan_khau);
        setTitle("Thêm Khách");
        addControls();
        addEvents();
    }

    private boolean validateInput(){
        if(txtTenKhachThem.getText().toString().equals("")){
            //Toast.makeText(activity,"Tên khách không được bỏ trống",Toast.LENGTH_SHORT).show();
            txtTenKhachThem.setError("Tên khách không được bỏ trống");
            DangNhapActivity.showError(txtTenKhachThem,activity);
            txtTenKhachThem.requestFocus();
            return false;
        }
        if(txtCMNDThem.getText().toString().equals("")){
            //Toast.makeText(activity,"CMND không được bỏ trống",Toast.LENGTH_SHORT).show();
            txtCMNDThem.setError("CMND không được bỏ trống");
            DangNhapActivity.showError(txtCMNDThem,activity);
            txtCMNDThem.requestFocus();
            return false;
        }
        return true;
    }

    private void addControls() {
        txtTenKhachThem = (EditText) findViewById(R.id.txtTenKhachThem);
        txtCMNDThem = (EditText) findViewById(R.id.txtCMNDThem);
        txtDienThoaiThem = (EditText) findViewById(R.id.txtDienThoaiThem);
        btnThemKhach = (Button) findViewById(R.id.btnThemKhach);
        ivKhachThem = (ImageView) findViewById(R.id.ivKhachThem);
    }

    private void addEvents() {
        btnThemKhach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!validateInput()){
                    return;
                }else {
                    String ten = txtTenKhachThem.getText().toString();
                    String cmnd = txtCMNDThem.getText().toString();
                    ThemKhachTask task = new ThemKhachTask();
                    task.execute(ten, cmnd, MainActivity.p.getMa_Phong() + "");
                    MainActivity.adapter.notifyDataSetChanged();
                    ThemKhachActivity.this.cancel();
                }
            }
        });
    }

    class ThemKhachTask extends AsyncTask<String,Void,Boolean>{
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
            ListKhachTask task = new ListKhachTask();
            task.execute(MainActivity.p);
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
                Log.e("Ma phong: ",params[2]);
                Log.e("Ten Khach: ",params[0]);
                Log.e("cmnd: ",params[1]);
                String ten = URLEncoder.encode(params[0],"UTF-8");
                //String trangthai = URLEncoder.encode(params[1],"UTF-8");
                URL url=new URL(DangNhapActivity.URL_CONNECT+"khach/?tenkhach="+ten+"&cmnd="+params[1]+
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
                Log.e("LOI Them Khach activity",ex.toString());
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
