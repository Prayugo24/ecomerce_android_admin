package com.mulai_berkarya.admin.cimol;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mulai_berkarya.admin.adapter.AdapterDetail;
import com.mulai_berkarya.admin.adapter.AdapterOrder;
import com.mulai_berkarya.admin.entitas.Detail;
import com.mulai_berkarya.admin.entitas.Order;
import com.mulai_berkarya.admin.server.JSONParser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.mulai_berkarya.admin.server.Config.URL_BASE_API;

public class DetailAct extends AppCompatActivity {
    String tag = "listdet";
    Button bresi;
    EditText eresi;
    ListView lvdetail;
    TextView tidtrans, ttanggal, tongkir, ttotal, tresi, tjumlah, talamat,
            tkec, tkab, tprov, tkode, ttelp, tnama, tnamarek, tnorek, tjmlrek, tkurir;
    Context context = this;
    SessionManager session;
    String url = URL_BASE_API + "/detail_admin.php?id_transaksi=";
    String url2 = URL_BASE_API + "/transaksi_detail.php?id_transaksi=";
    String url_resi = URL_BASE_API + "/edit_resi.php";
    ProgressDialog progress;
    JSONArray jarray;
    ArrayList<Detail> details;
    ArrayList<Order> arrOrder;
    ProgressBar progressBar;
    String id, name, qty, jumlah, ket, gambar;
    private String listdetail, idtrans;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
//        alert
        alertDialogTrial();

        if (android.os.Build.VERSION.SDK_INT > 14) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        details = new ArrayList<>();

        Intent i = getIntent();
        idtrans = i.getStringExtra("id_transaksi");
        lvdetail = (ListView) findViewById(R.id.listviewdetail22);
        lvdetail.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                lvdetail.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });
        //new GetData().execute(url+idtrans);
        progress = new ProgressDialog(this);
        arrOrder = new ArrayList<Order>();
        view();
        loadetail();
        eresi = (EditText) findViewById(R.id.edittextResi);
        bresi = (Button) findViewById(R.id.btnresi);
        bresi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                String resinya = eresi.getText().toString().trim();
                inputresi(resinya);
                eresi.setText(null);
            }
        });
    }

    private void view() {
        final ProgressDialog pDialog = ProgressDialog.show(this, null, "Please wait....", true);
        tidtrans = (TextView) findViewById(R.id.textViewidtransaksi22);
        ttanggal = (TextView) findViewById(R.id.textviewtgl_trans22);
        tongkir = (TextView) findViewById(R.id.textViewongkir22);
        ttotal = (TextView) findViewById(R.id.textViewttl22);
        tresi = (TextView) findViewById(R.id.textviResi22);
        tjumlah = (TextView) findViewById(R.id.textViewsubt22);
        talamat = (TextView) findViewById(R.id.textViewAlamat2);
        tkec = (TextView) findViewById(R.id.textviewkec2);
        tkab = (TextView) findViewById(R.id.textViewkab2);
        tprov = (TextView) findViewById(R.id.textViewprof2);
        tkode = (TextView) findViewById(R.id.textViewkodepos2);
        ttelp = (TextView) findViewById(R.id.textViewtlp2);
        tnama = (TextView) findViewById(R.id.textatasnama2);
        tkurir = (TextView) findViewById(R.id.textViewkurir);
        tnamarek = (TextView) findViewById(R.id.textViewnamarek);
        tnorek = (TextView) findViewById(R.id.textViewnorek);
        tjmlrek = (TextView) findViewById(R.id.textViewjmlrek);

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        //params.add();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                super.onSuccess(statusCode, headers, response);
                pDialog.dismiss();
                try {
                    JSONParser parser = new JSONParser();
                    JSONObject obj = parser.getJSONFromUrl(url2 + idtrans);
                    jarray = obj.getJSONArray("transaksi_detail");
                    for (int i = 0; i < jarray.length(); i++) {
                        JSONObject c = jarray.getJSONObject(i);
                        Detail detail = new Detail();
                        tidtrans.setText(c.getString("id_transaksi"));
                        ttanggal.setText(c.getString("tgl_transaksi"));
                        tongkir.setText(c.getString("ongkir"));
                        ttotal.setText(c.getString("total"));
                        tresi.setText(c.getString("no_resi"));
                        tjumlah.setText(c.getString("jumlah"));
                        talamat.setText(c.getString("alamat"));
                        tkode.setText(c.getString("kode_pos"));
                        tkec.setText(c.getString("kecamatan"));
                        tprov.setText(c.getString("provinsi"));
                        tkab.setText(c.getString("kab_kot"));
                        ttelp.setText(c.getString("telepon"));
                        tnama.setText(c.getString("atas_nama"));
                        tkurir.setText(c.getString("agen_pengiriman"));
                        tnamarek.setText(c.getString("nama"));
                        tnorek.setText(c.getString("no_rek"));
                        tjmlrek.setText(c.getString("jumlah_rek"));
                        details.add(detail);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AdapterDetail adapter = new AdapterDetail(DetailAct.this,
                        R.layout.activity_listbarang_item, details);
                //listView.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                pDialog.dismiss();
                throwable.printStackTrace();
                Toast.makeText(getBaseContext(), "Cek koneksi internet anda!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadetail() {
        final ProgressDialog pDialog = ProgressDialog.show(this, null, "Please wait....", true);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("id_transaksi", idtrans);
        client.get(URL_BASE_API + "/detail_admin.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                pDialog.dismiss();
                try {
                    JSONArray arr = response.getJSONArray("detail");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        Order d = new Order();
                        d.setIdbrg(obj.getString("id"));
                        d.setNamabrg(obj.getString("name"));
                        d.setHargabrg(obj.getString("jumlah"));
                        d.setQty(obj.getString("qty"));
                        d.setKet(obj.getString("ket"));
                        d.setGambar(obj.getString("gambar"));
                        arrOrder.add(d);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                AdapterOrder adapter = new AdapterOrder(DetailAct.this, R.layout.detail_pemesanan, arrOrder);
                lvdetail.setAdapter(adapter);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                pDialog.dismiss();
                throwable.printStackTrace();

            }
        });
    }

    public void inputresi(String resi) {
        final ProgressDialog pDialog = ProgressDialog.show(this, null, "Please wait....", true);
        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        params.put("id_transaksi", idtrans);
        params.put("no_resi", resi);
        params.put("judul_pesan", "Informasi Pengiriman Barang");
        params.put("isi_pesan", "No Resi : " + eresi.getText().toString());

        client.post(url_resi, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                pDialog.dismiss();
                view();
                try {
                    String res = response.getString("status");
                    if (res.equalsIgnoreCase("berhasil")) {
                        Toast.makeText(DetailAct.this, "Order telah selesai diproses!", Toast.LENGTH_SHORT).show();

/*
                                etEmail.setText("");
                                etKeterangan.setText("");
                                etPassword1.setText("");
                                etPassword2.setText("");
                                etNamaToko.setText("");
                                etNamaPemilik.setText("");
                                etTelp.setText("");

*/

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    //			Toast.makeText(DetailAct.this, "Terjadi Kesalahan. Cek kembali koneksi internet anda", Toast.LENGTH_SHORT).show();
                }
                view();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pDialog.dismiss();
                throwable.printStackTrace();
                Toast.makeText(DetailAct.this, "Terjadi Kesalahan. Cek kembali koneksi internet anda", Toast.LENGTH_SHORT).show();
            }

        });
    }
    //    alert dialog trial
    public void alertDialogTrial(){
        AlertDialog.Builder builder=new AlertDialog.Builder(DetailAct.this);
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

