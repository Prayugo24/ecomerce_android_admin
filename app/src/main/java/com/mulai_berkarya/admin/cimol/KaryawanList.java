package com.mulai_berkarya.admin.cimol;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mulai_berkarya.admin.entitas.Karyawan;
import com.mulai_berkarya.admin.server.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class KaryawanList extends AppCompatActivity {
    SwipeRefreshLayout swipeRefreshLayout;
    ListView listView;
    ArrayList<Karyawan> karyawanArrayList;
    ArrayList<String> karyawanArr;
    ProgressBar progressBar;
    final int CODE_ACT = 1;
    FloatingActionButton fabTambah;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karyawan_list);
//        alert
        alertDialogTrial();

        listView = (ListView) findViewById(R.id.karyawan_lv);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        fabTambah = (FloatingActionButton) findViewById(R.id.karyawan_fabTambah);

        karyawanArrayList = new ArrayList<>();
        karyawanArr = new ArrayList<>();

        loadAllKaryawan();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Karyawan karyawan = karyawanArrayList.get(position);
                Intent i = new Intent(KaryawanList.this, KaryawanDetail.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", karyawan.getId());
                bundle.putString("nama", karyawan.getNama());
                bundle.putString("email", karyawan.getEmail());
                bundle.putString("telp", karyawan.getTelp());
                bundle.putString("bbm", karyawan.getBbm());
                bundle.putString("line", karyawan.getLine());

                i.putExtras(bundle);
                //startActivity(i);
                startActivityForResult(i, CODE_ACT);    //1 hapus
            }
        });


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                loadData();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadAllKaryawan();
//                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 2500);
            }
        });
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {
                boolean enable = false;
                if (listView != null && listView.getChildCount() > 0) {
                    boolean firstItemVisible = listView.getFirstVisiblePosition() == 0;
                    boolean topOfFirstItemVisible = listView.getChildAt(0).getTop() == 0;
                    enable = firstItemVisible && topOfFirstItemVisible;
                }
                swipeRefreshLayout.setEnabled(enable);
            }
        });

        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(KaryawanList.this, KaryawanTambah.class);
                startActivityForResult(i, CODE_ACT);
            }
        });

    }

    public void loadAllKaryawan() {
        if (karyawanArr.size() > 0 || karyawanArrayList.size() > 0) {
            karyawanArr.clear();
            karyawanArrayList.clear();
        }
        swipeRefreshLayout.setEnabled(false);
        progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        client.post(Config.URL_BASE_API + "/karyawan_list.php", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setEnabled(true);
                swipeRefreshLayout.setRefreshing(false);
                try {
                    for (int i = 0; i < response.length(); i++) {
                        JSONObject obj = response.getJSONObject(i);
                        Karyawan karyawan = new Karyawan();

                        karyawan.setId(obj.getString("id_owner"));
                        karyawan.setNama(obj.getString("nama_owner"));
                        karyawan.setEmail(obj.getString("email_owner"));
                        karyawan.setBbm(obj.getString("bbm"));
                        karyawan.setLine(obj.getString("line"));
                        karyawan.setTelp(obj.getString("telp_owner"));

                        karyawanArrayList.add(karyawan);
                        karyawanArr.add(obj.getString("nama_owner"));

                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(KaryawanList.this, android.R.layout.simple_list_item_1, karyawanArr);
                    listView.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setEnabled(true);
                swipeRefreshLayout.setRefreshing(false);
                listView.setAdapter(null);

                throwable.printStackTrace();
                Log.e("TAG", errorResponse.toString());
                Toast.makeText(KaryawanList.this, "Cek koneksi internet anda!", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                swipeRefreshLayout.setEnabled(true);
                progressBar.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                listView.setAdapter(null);
                throwable.printStackTrace();
                Log.e("TAG", "Karyawan list : " + responseString);
                if (responseString.contains("null")) {
                    Toast.makeText(KaryawanList.this, "Tidak ada karyawaN!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(KaryawanList.this, "Cek koneksi internet anda!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CODE_ACT && resultCode == Activity.RESULT_OK) {
            loadAllKaryawan();
        }
    }
    //    alert dialog trial
    public void alertDialogTrial(){
        AlertDialog.Builder builder=new AlertDialog.Builder(KaryawanList.this);
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
