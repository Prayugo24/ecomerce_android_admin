package com.mulai_berkarya.admin.cimol;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mulai_berkarya.admin.adapter.AdapterBarang;
import com.mulai_berkarya.admin.entitas.Barang;
import com.mulai_berkarya.admin.server.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by Mastah on 05/10/2016.
 */

public class Feedback extends AppCompatActivity {
    static ArrayList<Barang> listBarang;
    ProgressBar progressBar;
    ListView listView;
    SwipeRefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listfb);
//        alert
        alertDialogTrial();
        listBarang = new ArrayList<Barang>();
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        listView = (ListView) findViewById(R.id.lvbarang2);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);

        loadData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Barang bb = listBarang.get(i);
                Barang b = new Barang();
                Bundle bundle = new Bundle();
                bundle.putString("id", bb.getid_barang());
                bundle.putString("nama", bb.getnama_barang());
                bundle.putString("deskripsi", bb.getdeskripsi());
                bundle.putString("berat", bb.getberat());
                bundle.putString("harga", bb.getharga());
                bundle.putString("stok", bb.getStok());
                bundle.putString("gambar", bb.getgambar());

                //b.setid_barang(bb.getid_barang());
                //b.setnama_barang(bb.getnama_barang());
                //b.setdeskripsi(bb.getdeskripsi());
                //b.setharga(bb.getharga());
                //b.setgambar(bb.getgambar());
                //b.setberat(bb.getberat());

                Intent intent = new Intent();
                intent.setClass(view.getContext(), FeedbackActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                loadData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData();
//                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });
    }

    private void loadData() {
        swipeRefreshLayout.setRefreshing(false);
        listBarang.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Config.URL_BASE_API + "/barang_terjual.php", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                super.onSuccess(statusCode, headers, response);
                swipeRefreshLayout.setRefreshing(true);

                Log.e("TAG", response.toString());
                progressBar.setVisibility(View.GONE);
                JSONArray arrBarang = null;
                try {
                    arrBarang = response.getJSONArray("komentar");
                    for (int i = 0; i < arrBarang.length(); i++) {
                        JSONObject obj = arrBarang.getJSONObject(i);
                        Barang b = new Barang();
                        b.setid_barang(obj.getString("id"));
                        b.setgambar(obj.getString("gambar"));
                        b.setberat(obj.getString("berat"));
                        b.setStok(obj.getString("stok"));
                        b.setharga(obj.getString("harga_barang"));
                        b.setdeskripsi(obj.getString("deskripsi"));
                        b.setnama_barang(obj.getString("name"));
                        b.setTerjual(obj.getString("terjual"));
                        listBarang.add(b);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AdapterBarang adapter = new AdapterBarang(getApplicationContext(), R.layout.activity_listbarang_item, listBarang);
                listView.setAdapter(adapter);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                swipeRefreshLayout.setRefreshing(true);

                throwable.printStackTrace();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(), "Cek koneksi internet anda!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //    alert dialog trial
    public void alertDialogTrial(){
        AlertDialog.Builder builder=new AlertDialog.Builder(Feedback.this);
        View view= LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_alert_dialog,null);

        TextView title=(TextView)view.findViewById(R.id.tv_title);
        ImageButton imagebutton=(ImageButton) view.findViewById(R.id.image_pop);

        title.setText("Peringatan");

        imagebutton.setImageResource(R.drawable.trial);
        builder.setPositiveButton("Saya, mengerti", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getApplicationContext(),"Terimakasih Banyak !!",Toast.LENGTH_SHORT).show();
            }
        });
        builder.setView(view);
        builder.show();
    }
}