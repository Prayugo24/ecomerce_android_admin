package com.mulai_berkarya.admin.cimol;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class BarangPerKategori extends AppCompatActivity {
    ArrayList<Barang> listBarang;
    ProgressBar progressBar;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftarbarang);
//        alert
        alertDialogTrial();
        listView = (ListView) findViewById(R.id.lvbarang);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        listBarang = new ArrayList<>();
        String idKategori = getIntent().getStringExtra("id_kategori");
        if (null != idKategori) loadData(idKategori);
        else Toast.makeText(this, "Gagal meload data", Toast.LENGTH_SHORT).show();


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
                bundle.putString("stok", bb.getStok());
                bundle.putString("harga", bb.getharga());
                bundle.putString("gambar", bb.getgambar());
                bundle.putString("diskon", bb.getDiskonPersen());
                bundle.putString("terjual", bb.getTerjual());


                //b.setid_barang(bb.getid_barang());
                //b.setnama_barang(bb.getnama_barang());
                //b.setdeskripsi(bb.getdeskripsi());
                //b.setharga(bb.getharga());
                //b.setgambar(bb.getgambar());
                //b.setberat(bb.getberat());

                Intent intent = new Intent();
                intent.setClass(BarangPerKategori.this, EditBarangActivity.class);
                //intent.putExtra("posisi", i);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


    }


    private void loadData(String id_kategori) {
        progressBar.setVisibility(View.VISIBLE);
        listBarang.clear();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("id_kategori", id_kategori);
        Log.e("TAG", "id_kategori : " + id_kategori);
        client.get(Config.URL_BASE_API + "/barang_cari.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.e("TAG", response.toString());
                progressBar.setVisibility(View.GONE);
                JSONArray arrBarang = null;
                try {
                    arrBarang = response.getJSONArray("kategori");
                    for (int i = 0; i < arrBarang.length(); i++) {
                        JSONObject obj = arrBarang.getJSONObject(i);
                        Barang b = new Barang();
                        Toast.makeText(BarangPerKategori.this, "ID :  " + obj.getString("id"), Toast.LENGTH_SHORT).show();

                        b.setid_barang(obj.getString("id"));
                        b.setgambar(obj.getString("gambar"));
                        b.setberat(obj.getString("berat"));
                        b.setStok(obj.getString("stok"));
                        b.setharga(obj.getString("harga_barang"));
                        b.setdeskripsi(obj.getString("deskripsi"));
                        b.setnama_barang(obj.getString("name"));
                        b.setDiskonPersen(obj.getString("diskon"));
                        b.setHargaDiskon(obj.getString("harga_diskon"));
                        b.setTerjual(obj.getString("terjual"));
                        listBarang.add(b);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (listBarang.size() <= 0)
                    Toast.makeText(BarangPerKategori.this, "Tidak ada barang", Toast.LENGTH_SHORT).show();

                AdapterBarang adapter = new AdapterBarang(BarangPerKategori.this, R.layout.activity_listbarang_item, listBarang);
                listView.setAdapter(adapter);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                throwable.printStackTrace();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(BarangPerKategori.this, "Cek koneksi internet anda!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //    alert dialog trial
    public void alertDialogTrial(){
        AlertDialog.Builder builder=new AlertDialog.Builder(BarangPerKategori.this);
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