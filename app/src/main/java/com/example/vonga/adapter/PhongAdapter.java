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
import android.widget.ImageView;
import android.widget.TextView;
import com.example.vonga.model.Phong;
import com.example.vonga.motelroom.DangNhapActivity;
import com.example.vonga.motelroom.MainActivity;
import com.example.vonga.motelroom.R;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by VoNga on 12/23/2016.
 */

public class PhongAdapter extends ArrayAdapter {
    Activity context;
    int resource;
    private ProgressDialog pDialog;
    public Phong r;
    public PhongAdapter(Activity context, int resource) {
        super(context, resource);
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            //Create inflater layout
            LayoutInflater inflater = this.context.getLayoutInflater();
            convertView = inflater.inflate(this.resource, null);

            //findViewByID
            holder = new ViewHolder();
            holder.txtTenPhong = (TextView) convertView.findViewById(R.id.txtTenPhong);
            holder.ivPhongTrong = (ImageView) convertView.findViewById(R.id.ivPhongTrong);
            holder.ivPhongThue = (ImageView) convertView.findViewById(R.id.ivPhongThue);
            holder.ivDelete = (ImageView) convertView.findViewById(R.id.ivDelete);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        r = (Phong) getItem(position);

        holder.txtTenPhong.setText(r.getTen_Phong().toString());
        if(r.getTrang_Thai()==0){
            holder.ivPhongThue.setVisibility(View.INVISIBLE);
            holder.ivPhongTrong.setVisibility(View.VISIBLE);
        }else{
            holder.ivPhongTrong.setVisibility(View.INVISIBLE);
            holder.ivPhongThue.setVisibility(View.VISIBLE);
        }

        holder.ivDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder b=new AlertDialog.Builder(context);
                b.setTitle("Thông báo xóa");
                b.setMessage("Bạn có chắc chắn xóa phòng này không?");
                b.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        DeletePhongTask task = new DeletePhongTask();
                        task.execute(r.getMa_Phong());
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
        private TextView txtTenPhong;
        private ImageView ivPhongTrong;
        private ImageView ivPhongThue;
        private ImageView ivDelete;
    }

    class DeletePhongTask extends AsyncTask<Integer,Void,Boolean>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();
        }

        @Override
        protected void onPostExecute(Boolean aVoid) {
            super.onPostExecute(aVoid);
            MainActivity.adapter.remove(r);
            MainActivity.adapter.notifyDataSetChanged();
            pDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected Boolean doInBackground(Integer... params) {
            Log.d("Ma Phong lay dc ",params[0]+"");
            try{
                URL url = new URL(DangNhapActivity.URL_CONNECT+"room/?maphong="+params[0]);

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
