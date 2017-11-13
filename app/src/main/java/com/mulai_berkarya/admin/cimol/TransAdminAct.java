package com.mulai_berkarya.admin.cimol;

import android.app.ProgressDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.mulai_berkarya.admin.adapter.AdapterTrans;
import com.mulai_berkarya.admin.entitas.Trans;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static com.mulai_berkarya.admin.server.Config.URL_BASE_API;

public class TransAdminAct extends AppCompatActivity {
    static ArrayList<Trans> arrtrans;
    ListView lvtrans;
    String tag = "listrans";
    String url = URL_BASE_API + "/semua_trans.php";
    SessionManager session;
    ProgressDialog pDialog;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trans);
//        alert
        alertDialogTrial();

        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        //new GetData().execute(url);
        lvtrans = (ListView) findViewById(R.id.listViewtr);

        pDialog = ProgressDialog.show(this, null, "Please wait....", true);
        pDialog.setCancelable(true);

        listransaksi();
        arrtrans = new ArrayList<Trans>();
        lvtrans.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int i, long id) {
                Trans tt = arrtrans.get(i);
                Trans t = new Trans();
                Bundle b = new Bundle();
                b.putString("id_transaksi", tt.getId_transaksi());
                b.putString("total", tt.getTotal());

                Intent intent = new Intent();
                intent.setClass(v.getContext(), DetailAct.class);
                intent.putExtras(b);
                startActivity(intent);
            }
        });
    }

    private void listransaksi() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //   super.onSuccess(statusCode, headers, response);
                pDialog.dismiss();
                Log.e("TAG", response.toString());
                JSONArray arrBarang = null;
                try {
                    arrBarang = response.getJSONArray("transaksi");
                    for (int i = 0; i < arrBarang.length(); i++) {
                        JSONObject obj = arrBarang.getJSONObject(i);
                        Trans t = new Trans();
                        t.setId_transaksi(obj.getString("id_transaksi"));
                        t.setTanggal(obj.getString("tgl_transaksi"));
                        t.setTotal(obj.getString("total"));
                        t.setStatus(obj.getString("status"));
                        t.setKonf_bay(obj.getString("konf_bay"));
                        t.setNo_resi(obj.getString("no_resi"));
                        arrtrans.add(t);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                AdapterTrans adapter = new AdapterTrans(TransAdminAct.this, R.layout.trans_admin_adapter, arrtrans);
                lvtrans.setAdapter(adapter);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                pDialog.dismiss();

                throwable.printStackTrace();
                Toast.makeText(TransAdminAct.this, "Cek koneksi internet anda!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pDialog.dismiss();
                throwable.printStackTrace();
                Toast.makeText(TransAdminAct.this, "Cek koneksi internet anda!", Toast.LENGTH_SHORT).show();
            }
        });

    }

    //    alert dialog trial
    public void alertDialogTrial(){
        AlertDialog.Builder builder=new AlertDialog.Builder(TransAdminAct.this);
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
