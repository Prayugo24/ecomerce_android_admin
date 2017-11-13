package com.mulai_berkarya.admin.cimol;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mulai_berkarya.admin.entitas.Category;
import com.mulai_berkarya.admin.server.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.mulai_berkarya.admin.server.Config.URL_BASE_API;

public class CategoryActivity extends AppCompatActivity {
    ListView lvbrg;
    JSONObject jsonobject;
    JSONArray jsonarray;
    ArrayList<String> catlist, idList;
    ArrayList<Category> category;
    ProgressBar progressBar;
    FloatingActionButton fabTambah;
    AlertDialog.Builder builder;
    Context context = this;
    private String URLCAT = URL_BASE_API + "/get_cat.php?id_category=";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category);
//        alert
        alertDialogTrial();
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        fabTambah = (FloatingActionButton) findViewById(R.id.kategori_fabTambah);

        catlist = new ArrayList<String>();
        idList = new ArrayList<String>();
        lvbrg = (ListView) findViewById(R.id.listviewnyaa);
        lvbrg.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapter, View v, final int position, long id) {

                android.app.AlertDialog.Builder adib = new android.app.AlertDialog.Builder(context);

                adib.setTitle("Konfirmasi");

                adib
                        .setMessage("Apakah yakin anda mau menghapusnya?")
                        .setCancelable(false)
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                String idcat = idList.get(position);
                                hapusKategori(idcat);
                                //Toast.makeText(CategoryActivity.this, "ID : " + idList.get(position), Toast.LENGTH_SHORT).show();

                            }
                        })
                        .setNegativeButton("Tidak", null);
                android.app.AlertDialog aldi = adib.create();
                aldi.show();


            }
        });
        builder = new AlertDialog.Builder(CategoryActivity.this);

        getAllKategori();
        fabTambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Nama Kategori");
                final EditText input = new EditText(CategoryActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                builder.setView(input);
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String namaKategori = input.getText().toString().trim();
                        buatKategori(namaKategori);
                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                builder.show();
            }
        });
    }

    private void buatKategori(String namaKategori) {
        progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        /*params.add("ket", "buat_kategori");*/
        params.add("category", namaKategori);
        client.post(URL_BASE_API + "/get_cat.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    String status = response.getString("status");
                    if (status.equalsIgnoreCase("ada")) {
                        Toast.makeText(CategoryActivity.this, "Kategori sudah ada!", Toast.LENGTH_SHORT).show();
                    } else if (status.equalsIgnoreCase("berhasil")) {
                        Toast.makeText(CategoryActivity.this, "Berhasil menambahkan Kategori!", Toast.LENGTH_SHORT).show();
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                dialogInterface.dismiss();
                            }
                        });
                        getAllKategori();
                    } else {
                        Toast.makeText(CategoryActivity.this, "Gagal menambahkan Kategori!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CategoryActivity.this, "Cek koneksi internet", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
    }

    private void hapusKategori(String idkategori) {
        progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("id_category", idkategori);
        client.post(Config.URL_BASE_API + "/get_cat.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CategoryActivity.this, "", Toast.LENGTH_SHORT).show();
                try {
                    String status = response.getString("status");
                    if (status.equalsIgnoreCase("berhasil")) {
                        Toast.makeText(CategoryActivity.this, "Berhasil menghapus Kategori!", Toast.LENGTH_SHORT).show();
                        builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
                            @Override
                            public void onDismiss(DialogInterface dialogInterface) {
                                dialogInterface.dismiss();
                            }
                        });
                        getAllKategori();
                    } else {
                        Toast.makeText(CategoryActivity.this, "Gagal menghapus Kategori!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
                progressBar.setVisibility(View.GONE);
                Toast.makeText(CategoryActivity.this, "Cek koneksi internet", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
    }

    private void getAllKategori() {
        if (idList.size() > 0) {
            idList.clear();
            catlist.clear();
        }

        progressBar.setVisibility(View.VISIBLE);
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(URLCAT, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                progressBar.setVisibility(View.GONE);
                try {
                    JSONArray arr = response.getJSONArray("category");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        String nama = obj.getString("category");
                        String idnya = obj.getString("id_category");
                        catlist.add(nama);
                        idList.add(idnya);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CategoryActivity.this, android.R.layout.simple_list_item_1, catlist);
                lvbrg.setAdapter(adapter);

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                progressBar.setVisibility(View.GONE);
                super.onFailure(statusCode, headers, responseString, throwable);
                throwable.printStackTrace();
                Toast.makeText(CategoryActivity.this, "Cek koneksi internet anda", Toast.LENGTH_SHORT).show();
            }
        });

    }
    //    alert dialog trial
    public void alertDialogTrial(){
        AlertDialog.Builder builder=new AlertDialog.Builder(CategoryActivity.this);
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
