package com.mulai_berkarya.admin.cimol;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
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

import static com.mulai_berkarya.admin.cimol.R.id.progressBar;

public class ListCariAct extends Activity {
    String tag = "listbrg";
    ListView lvbrg;
    EditText etCari;
    ArrayList<Barang> arrBarang;
    ImageButton imgCari;
    Button bfil;
    String stokting;
    String stokrend;
    String diskonting;
    String diskonrend;
    String harga_rendah;
    String harga_tinggi;
    String jualting;
    String jualrend;
    String keywordCari;
    ProgressBar progressDialog;

    String url = Config.URL_BASE_API + "/barang_cari_filter_admin.php";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caribrg);
//        alert
        alertDialogTrial();

        arrBarang = new ArrayList<>();
        final AlertDialog.Builder menuFilter = new AlertDialog.Builder(this);

        bfil = (Button) findViewById(R.id.btfilter);
        lvbrg = (ListView) findViewById(R.id.lvbarang);
        etCari = (EditText) findViewById(R.id.edt_cari);
        imgCari = (ImageButton) findViewById(R.id.img_cari);
        progressDialog = (ProgressBar) findViewById(progressBar);

        imgCari.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keywordCari = etCari.getText().toString().trim();
                if (TextUtils.isEmpty(keywordCari)) {
                    Toast.makeText(ListCariAct.this, "Masukkan kata kunci", Toast.LENGTH_SHORT).show();
                } else {
                    cariBarang(keywordCari, stokting, stokrend, diskonting, diskonrend, harga_tinggi, harga_rendah, jualting, jualrend);
                }
            }
        });

        lvbrg.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Barang bb = arrBarang.get(i);
                Barang b = new Barang();
                Bundle bundle = new Bundle();
                bundle.putString("id", bb.getid_barang());
                bundle.putString("nama", bb.getnama_barang());
                bundle.putString("deskripsi", bb.getdeskripsi());
                bundle.putString("berat", bb.getberat());
                bundle.putString("harga", bb.getharga());
                bundle.putString("stok", bb.getStok());
                //bundle.putString("harga_diskon" , bb.getHargaDiskon());
                bundle.putString("diskon", bb.getDiskonPersen());
                bundle.putString("gambar", bb.getgambar());
                bundle.putString("terjual", bb.getTerjual());

                Intent intent = new Intent();
                intent.setClass(view.getContext(), EditBarangActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        final String items[] = new String[]{"Kategori"};
        menuFilter.setTitle("Filter");
        menuFilter.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface item, int position) {
                progressDialog.setVisibility(View.VISIBLE);
                String pesan = "";
                switch (position) {
                    case 0:
                        stokting = "stok";
                        stokrend = "";
                        diskonting = "";
                        diskonrend = "";
                        harga_tinggi = "";
                        harga_rendah = "";
                        jualting = "";
                        jualrend = "";
                        keywordCari = etCari.getText().toString();
                        cariBarang(keywordCari, stokting, stokrend, diskonting, diskonrend, harga_tinggi, harga_rendah, jualting, jualrend);
                        pesan = items[position];
                        break;
                    case 1:
                        stokting = "";
                        stokrend = "stok";
                        diskonting = "";
                        diskonrend = "";
                        harga_tinggi = "";
                        harga_rendah = "";
                        jualting = "";
                        jualrend = "";
                        keywordCari = etCari.getText().toString();
                        cariBarang(keywordCari, stokting, stokrend, diskonting, diskonrend, harga_tinggi, harga_rendah, jualting, jualrend);
                        pesan = items[position];
                        break;
                    case 2:
                        stokting = "";
                        stokrend = "";
                        diskonting = "diskon";
                        diskonrend = "";
                        harga_tinggi = "";
                        harga_rendah = "";
                        jualting = "";
                        jualrend = "";
                        keywordCari = etCari.getText().toString();
                        cariBarang(keywordCari, stokting, stokrend, diskonting, diskonrend, harga_tinggi, harga_rendah, jualting, jualrend);
                        pesan = items[position];
                        break;
                    case 3:
                        stokting = "";
                        stokrend = "";
                        diskonting = "";
                        diskonrend = "diskon";
                        harga_tinggi = "";
                        harga_rendah = "";
                        jualting = "";
                        jualrend = "";
                        keywordCari = etCari.getText().toString();
                        cariBarang(keywordCari, stokting, stokrend, diskonting, diskonrend, harga_tinggi, harga_rendah, jualting, jualrend);
                        pesan = items[position];
                        break;
                    case 4:
                        stokting = "";
                        stokrend = "";
                        diskonting = "";
                        diskonrend = "";
                        harga_tinggi = "harga_barang";
                        harga_rendah = "";
                        jualting = "";
                        jualrend = "";
                        keywordCari = etCari.getText().toString();
                        cariBarang(keywordCari, stokting, stokrend, diskonting, diskonrend, harga_tinggi, harga_rendah, jualting, jualrend);
                        pesan = items[position];
                        break;
                    case 5:
                        stokting = "";
                        stokrend = "";
                        diskonting = "";
                        diskonrend = "";
                        harga_tinggi = "";
                        harga_rendah = "harga_barang";
                        jualting = "";
                        jualrend = "";
                        keywordCari = etCari.getText().toString();
                        cariBarang(keywordCari, stokting, stokrend, diskonting, diskonrend, harga_tinggi, harga_rendah, jualting, jualrend);
                        pesan = items[position];
                        break;
                    case 6:
                        stokting = "";
                        stokrend = "";
                        diskonting = "";
                        diskonrend = "";
                        harga_tinggi = "";
                        harga_rendah = "";
                        jualting = "terjual";
                        jualrend = "";
                        keywordCari = etCari.getText().toString();
                        cariBarang(keywordCari, stokting, stokrend, diskonting, diskonrend, harga_tinggi, harga_rendah, jualting, jualrend);
                        pesan = items[position];
                        break;
                    case 7:
                        stokting = "";
                        stokrend = "";
                        diskonting = "";
                        diskonrend = "";
                        harga_tinggi = "";
                        harga_rendah = "";
                        jualting = "";
                        jualrend = "terjual";
                        keywordCari = etCari.getText().toString();
                        cariBarang(keywordCari, stokting, stokrend, diskonting, diskonrend, harga_tinggi, harga_rendah, jualting, jualrend);
                        pesan = items[position];
                        break;
                    case 8:
                        Intent i = new Intent(ListCariAct.this, CategoryActivity.class);
                        startActivity(i);
                        pesan = items[position];
                        break;
                }
                Toast.makeText(ListCariAct.this, pesan, Toast.LENGTH_SHORT).show();
            }
        });

        bfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                menuFilter.create().show();
            }
        });
    }


    private void cariBarang(String keywordCari, String stokting, String stokrend, String diskonting, String diskonrend, String harga_tinggi, String harga_rendah, String jualting, String jualrend) {
        arrBarang.clear();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("name", keywordCari);
        client.get(url + "?name=" + keywordCari + "&stokting=" + stokting + "&stokrend=" + stokrend + "&diskonting=" + diskonting + "&diskonrend=" + diskonrend + "&hargating=" + harga_tinggi + "&hargarend=" + harga_rendah + "&jualting=" + jualting + "&jualrend=" + jualrend, null, new JsonHttpResponseHandler() {
            //client.get(url+"?name="+keywordCari, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressDialog.setVisibility(View.GONE);
                try {
                    JSONArray arr = response.getJSONArray("barang");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        Barang b = new Barang();
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
                        arrBarang.add(b);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AdapterBarang adapter = new AdapterBarang(ListCariAct.this, R.layout.activity_listbarang_item, arrBarang);
                lvbrg.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressDialog.setVisibility(View.GONE);
                super.onFailure(statusCode, headers, responseString, throwable);
                throwable.printStackTrace();
                Toast.makeText(ListCariAct.this, "Cek koneksi internet", Toast.LENGTH_SHORT).show();

            }
        });

    }
    //    alert dialog trial
    public void alertDialogTrial(){
        AlertDialog.Builder builder=new AlertDialog.Builder(ListCariAct.this);
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
