package com.example.vonga.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.vonga.model.Khach;
import com.example.vonga.motelroom.DangNhapActivity;
import com.example.vonga.motelroom.KhachActivity;
import com.example.vonga.motelroom.MainActivity;
import com.example.vonga.motelroom.R;
import com.example.vonga.motelroom.UpdateKhachActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by VoNga on 1/8/2017.
 */

public class KhachAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    private ProgressDialog pDialog;
    public Khach k;
    public KhachAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final KhachAdapter.ViewHolder holder;
        if (convertView == null) {
            //Create inflater layout
            LayoutInflater inflater = this.context.getLayoutInflater();
            convertView = inflater.inflate(this.resource, null);

            //findViewByID
            holder = new ViewHolder();
            holder.txtTenKhachList = (TextView) convertView.findViewById(R.id.txtTenKhachList);
            holder.txtCMNDList = (TextView) convertView.findViewById(R.id.txtCMNDList);
            holder.btnSuaKhach = (Button) convertView.findViewById(R.id.btnSuaKhach);
            holder.ivDeleteKhach = (ImageView) convertView.findViewById(R.id.ivDeleteKhach);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        k = (Khach) getItem(position);
        holder.txtTenKhachList.setText(k.getTen_Khach().toString());
        holder.txtCMNDList.setText(k.getCMND().toString());
        holder.btnSuaKhach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UpdateKhachActivity myDialog = new UpdateKhachActivity(context);
                TextView ten = (TextView) myDialog.findViewById(R.id.txtTenKhachUpdate);
                TextView cmnd = (TextView) myDialog.findViewById(R.id.txtCMNDUpdate);

                ten.setText(k.getTen_Khach());
                cmnd.setText(k.getCMND());
                myDialog.maKhachUpdate = k.getMa_Khach();
                myDialog.maPhongCuaKhach = k.getMa_Phong();
                myDialog.show();
            }
        });

        holder.ivDeleteKhach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b=new AlertDialog.Builder(context);
                b.setTitle("Thông báo xóa");
                b.setMessage("Bạn có chắc chắn xóa khách này không?");
                b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeleteKhachTask task = new DeleteKhachTask();
                        task.execute(k.getMa_Khach());
                        MainActivity.adapter.notifyDataSetChanged();
                    }
                });
                b.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                b.create().show();
            }
        });

        return convertView;
    }

    class ViewHolder {
        private TextView txtTenKhachList;
        private TextView txtCMNDList;
        private Button btnSuaKhach;
        private ImageView ivDeleteKhach;
    }

    class DeleteKhachTask extends AsyncTask<Integer,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean bool) {
            super.onPostExecute(bool);
            KhachActivity.adapter.remove(k);
            KhachActivity.adapter.notifyDataSetChanged();
            //KhachActivity.adapter.notifyDataSetChanged();
            pDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            Log.d("Ma Khach lay dc ",params[0]+"");
            try{
                URL url = new URL(DangNhapActivity.URL_CONNECT+"khach/?makhach="+params[0]);

                //mo ket noi
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("DELETE");
                connection.setRequestProperty("Content-Type","application/json;charset = utf8");
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

            }catch (Exception ex){
                Log.e("Loi Delete ",ex.toString());
            }
            return null;
        }
    }
}
