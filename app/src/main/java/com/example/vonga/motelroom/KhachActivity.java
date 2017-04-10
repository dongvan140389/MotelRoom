package com.example.vonga.motelroom;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import com.example.vonga.adapter.KhachAdapter;
import com.example.vonga.model.Khach;
import com.example.vonga.model.Phong;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class KhachActivity extends AppCompatActivity {
    public static int UPDATE_KHACH_THANH_CONG = 5;
    ListView lvKhach;
    public static KhachAdapter adapter;
    FloatingActionButton fbtnAddKhach;
    private ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_khach);
        addControls();
        addEvents();
    }

    private void addControls() {
        fbtnAddKhach = (FloatingActionButton) findViewById(R.id.fbtnThemKhach);
        lvKhach = (ListView) findViewById(R.id.lvListKhach);
        adapter = new KhachAdapter(KhachActivity.this,R.layout.khach_item);
        lvKhach.setAdapter(adapter);

        ListKhachTask task = new ListKhachTask();
        task.execute(MainActivity.p);
    }

    private void addEvents() {
        fbtnAddKhach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemKhachActivity myDialog = new ThemKhachActivity(KhachActivity.this);
                myDialog.show();
            }
        });
    }

    class ListKhachTask extends AsyncTask<Phong,Void,ArrayList<Khach>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapter.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<Khach> khaches) {
            super.onPostExecute(khaches);
            adapter.clear();
            adapter.addAll(khaches);
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
