package com.mulai_berkarya.admin.cimol;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mulai_berkarya.admin.entitas.ListAdapterKom;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

import static com.mulai_berkarya.admin.server.Config.URL_BASE_API;

/**
 * Created by Mastah on 22/09/2016.
 */

public class FeedbackActivity extends AppCompatActivity {
    ListView lvkom;
    Context context = this;
    String url = URL_BASE_API + "/owner_baca_komentar.php?id=";
    ProgressBar progressBar;
    FloatingActionButton fabTambah;
    String idBarang = null;
    SessionManager sessionManager;
    private String[] nama;
    private String[] judul;
    private String[] isi;
    private String listkom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
//        alert
        alertDialogTrial();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        sessionManager = new SessionManager(this);

        lvkom = (ListView) findViewById(R.id.lvkomentar);

/*
        Bundle bundle = getIntent().getExtras();
        idBarang = bundle.getString("id");
*/
        idBarang = getIntent().getStringExtra("id");
        getData(idBarang);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            getData(idBarang);
        }
    }

    public void getData(String idBarang) {
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("id", idBarang);
        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONArray jarray = response.getJSONArray("komentar");
                    nama = new String[jarray.length()];
                    judul = new String[jarray.length()];
                    isi = new String[jarray.length()];

                    for (int i = 0; i < jarray.length(); i++) {
                        nama[i] = jarray.getJSONObject(i).getString("nama_member").toString();
                        judul[i] = jarray.getJSONObject(i).getString("judul_komentar").toString();
                        isi[i] = jarray.getJSONObject(i).getString("isi_komentar").toString();
                    }
                    ListAdapterKom adapter = new ListAdapterKom(getBaseContext(), nama, judul, isi);
                    lvkom.setAdapter(adapter);

                    if (jarray.length() <= 0)
                        Toast.makeText(FeedbackActivity.this, "Tidak ada ulasan", Toast.LENGTH_SHORT).show();
                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(FeedbackActivity.this, "Tidak ada ulasan", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                //super.onFailure(statusCode, headers, responseString, throwable);
                throwable.printStackTrace();
                Toast.makeText(FeedbackActivity.this, "Cek koneksi internet!", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //    alert dialog trial
    public void alertDialogTrial(){
        AlertDialog.Builder builder=new AlertDialog.Builder(FeedbackActivity.this);
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
