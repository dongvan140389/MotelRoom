package com.example.vonga.motelroom;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import com.example.vonga.adapter.PhongAdapter;
import com.example.vonga.model.Phong;
import com.example.vonga.model.QuanLy;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    GridView gvPhong;
    public static PhongAdapter adapter;
    FloatingActionButton fbtnAddRoom;
    public static QuanLy ql = new QuanLy();
    public static Phong p = new Phong();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addControls();
        addEvents();
    }

    private void addEvents() {
        fbtnAddRoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ThemPhongActivity myDialog = new ThemPhongActivity(MainActivity.this);
                myDialog.show();
            }
        });

        gvPhong.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                p = (Phong) adapter.getItem(position);
                Log.d("Phong tu MainActivity",p.getMa_Phong()+"");
                Intent i = new Intent(MainActivity.this,ChiTietPhongActivity.class);
                startActivity(i);

            }
        });
    }

    private void addControls() {
        fbtnAddRoom = (FloatingActionButton) findViewById(R.id.fbtnThemPhong);
        gvPhong = (GridView) findViewById(R.id.gvRoomMain);
        adapter = new PhongAdapter(MainActivity.this,R.layout.room_item);
        gvPhong.setAdapter(adapter);

        Intent i = getIntent();
        ql = (QuanLy) i.getSerializableExtra("QUANLY");
        Log.d("Lấy ra quản lý ",ql.getUsername());
        listPhong task = new listPhong();
        task.execute(ql);
    }

    class listPhong extends AsyncTask<QuanLy,Void,ArrayList<Phong>>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            adapter.clear();
        }

        @Override
        protected void onPostExecute(ArrayList<Phong> phongs) {
            super.onPostExecute(phongs);
            adapter.clear();
            adapter.addAll(phongs);
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected ArrayList<Phong> doInBackground(QuanLy... params) {
            ArrayList<Phong> dsPhong = new ArrayList<>();
            try{
                URL url = new URL(DangNhapActivity.URL_CONNECT+"room/?username="+params[0].getUsername());

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
                //tien hanh mo hinh hoa nguoc tu jsonArray ve mo hinh lop
                for(int i=0;i<jsonArray.length();i++){
                    try{
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        Phong p = new Phong();
                        Log.d("MaPhong thứ ",i+" la "+jsonObject.getInt("Ma_Phong"));
                        p.setMa_Phong(jsonObject.getInt("Ma_Phong"));
                        p.setTen_Phong(jsonObject.getString("Ten_Phong"));
                        p.setTrang_Thai(jsonObject.getInt("Trang_Thai"));
                        dsPhong.add(p);
                        Log.e("Số phòng ",dsPhong.size()+"");
                    }catch (Exception ex){
                        Log.e("LOI LIST PHONG 1","i = "+i+" "+ex.toString());
                    }
                }

            }catch (Exception ex){
                Log.e("LOI LIST PHONG 1",ex.toString());
            }
            return dsPhong;

        }
    }
}
