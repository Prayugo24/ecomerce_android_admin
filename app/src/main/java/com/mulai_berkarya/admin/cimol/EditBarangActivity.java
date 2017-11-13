package com.mulai_berkarya.admin.cimol;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mulai_berkarya.admin.entitas.Category;
import com.mulai_berkarya.admin.server.Config;
import com.karumi.dexter.Dexter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;


public class EditBarangActivity extends AppCompatActivity implements View.OnClickListener {

    static int position = 0;
    ArrayList<Category> arrCategory;
    ArrayList<String> arrCategoryNama = new ArrayList<>(), arrCategoryId = new ArrayList<>();
    String kategoriDipilih = null;
    SessionManager session;
    String URL_GAMBAR = Config.URL_BASE + "/Gambar";
    Bitmap bitmaps;
    String idBarang, id;
    ProgressBar progressBar;
    ProgressDialog pd;
    AlertDialog.Builder builder;
    private Button btnEdit, btnHapus;
    private ImageView imgBarang, btnUpload;
    private EditText detail_nama, detail_diskon, detail_desk, detail_harga, detail_stok, detail_berat;
    private Spinner spinner;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    //private String UPLOAD_URL = Config.URL_BASE + "/edit_barang.php?id=";
    private String UPLOAD_URL = Config.URL_BASE_API + "/edit_barang.php";
    private String HAPUS_URL = Config.URL_BASE_API + "/hapus_barang.php?id=";
    private String KEY_IMAGE = "gambar";
    private String KEY_NAME = "name";
    private String KEY_BERAT = "berat";
    private String KEY_HARBAR = "harga_barang";
    private String KEY_DESK = "deskripsi";
    private String KEY_STOK = "stok";
    private String KEY_DISKON = "diskon";
    private String KEY_KATEGORI = "category";
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editbarang);
//        alert
        alertDialogTrial();

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        Dexter.initialize(this);

        if (Build.VERSION.SDK_INT > 16) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        session = new SessionManager(getApplicationContext());
        session.checkLogin();
        HashMap<String, String> user = session.getUserDetails();
        id = user.get(SessionManager.KEY_ID);

        btnEdit = (Button) findViewById(R.id.btnEdit);
        btnHapus = (Button) findViewById(R.id.btnHapus);

        detail_berat = (EditText) findViewById(R.id.detail_berat);
        detail_desk = (EditText) findViewById(R.id.detail_desk);
        detail_stok = (EditText) findViewById(R.id.detail_stok);
        detail_nama = (EditText) findViewById(R.id.detail_nama);
        detail_diskon = (EditText) findViewById(R.id.detail_diskon);
        detail_harga = (EditText) findViewById(R.id.detail_harga);
        spinner = (Spinner) findViewById(R.id.spinnerKategori);

        imgBarang = (ImageView) findViewById(R.id.detail_gambar);
        btnUpload = (ImageView) findViewById(R.id.btnUpload);

        btnEdit.setOnClickListener(this);
        btnHapus.setOnClickListener(this);
        btnUpload.setOnClickListener(this);

        int posisi = getIntent().getIntExtra("posisi", 0);
/*
        Barang barang = TerbaruFragment.listBarang.get(posisi);

        detail_nama.setText(barang.getnama_barang());
        detail_harga.setText("Rp. " + barang.getharga());
        detail_berat.setText(barang.getberat() + " gram");
        detail_stok.setText(barang.getStok() + " ");
        detail_diskon.setText(barang.getDiskonPersen() + " ");
        detail_desk.setText(barang.getdeskripsi());
*/

        Bundle bundle = getIntent().getExtras();
        idBarang = bundle.getString("id");
        detail_nama.setText(bundle.getString("nama"));
        detail_desk.setText(bundle.getString("deskripsi"));
        detail_berat.setText(bundle.getString("berat"));
        detail_harga.setText(bundle.getString("harga"));
        detail_stok.setText(bundle.getString("stok"));
        detail_diskon.setText(bundle.getString("diskon"));


        Glide
                .with(this)
                .load(URL_GAMBAR + "/" + bundle.getString("gambar"))
                .asBitmap()
                .fitCenter()
                .override(600, 400)
                .error(R.mipmap.ic_launcher)
                .placeholder(R.mipmap.ic_launcher)
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                        imgBarang.setImageBitmap(resource);
                        bitmaps = resource;
                    }
                });

        loading = ProgressDialog.show(this, null, "Please wait...", false, false);
        loading.setCancelable(true);

        getKategori();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                Category cat = (Category) adapterView.getItemAtPosition(i);
                if (i != 0) {
                    //Category cat = arrCategory.get(i);
                    //kategoriDipilih = cat.getIdcat();
                    kategoriDipilih = arrCategoryId.get(i);
                } else {
                    Toast.makeText(EditBarangActivity.this, "Kategori belum dipilih", Toast.LENGTH_SHORT).show();
                    kategoriDipilih = null;
                }
//                Toast.makeText(UploadActivity.this, "id : " + cat.getIdcat()+"\n" +"nama : " + cat.getCat(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                //Category cat = arrCategory.get(0);
                //kategoriDipilih = cat.getIdcat();
            }
        });
        builder = new AlertDialog.Builder(this);


    }

    public String getStringImage(Bitmap bmp) {
        if (null != bmp) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        } else {
            return null;
        }
    }

    private void uploadImageAsync(String idBarang, String name, String berat, String harga_barang, String deskripsi, String stok, String diskon, String image) {
        loading.show();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("id", idBarang);
        params.put(KEY_NAME, name);
        params.put(KEY_BERAT, berat);
        params.put(KEY_HARBAR, harga_barang);
        params.put(KEY_DESK, deskripsi);
        params.put(KEY_STOK, stok);
        params.put(KEY_DISKON, diskon);
        params.put(KEY_KATEGORI, kategoriDipilih);
        if (image != null) params.put(KEY_IMAGE, image);

        client.post(UPLOAD_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                loading.dismiss();
                throwable.printStackTrace();
                Toast.makeText(EditBarangActivity.this, "Upload gagal! Cek koneksi internet anda!",
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                loading.dismiss();
                if (null != responseString) {
                    Toast.makeText(EditBarangActivity.this, responseString, Toast.LENGTH_SHORT).show();
                    Intent in = new Intent(EditBarangActivity.this, MainActivity.class);
                    startActivity(in);
                    Toast.makeText(EditBarangActivity.this, "Barang berhasil di upload!", Toast.LENGTH_SHORT)
                            .show();
                    //broadcastNotifikasi("Produk Baru!", editTextName.getText().toString().trim());
                } else {
                    Toast.makeText(EditBarangActivity.this, "Upload gagal!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgBarang.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public void hapusBarang(String idBarang) {
        loading.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("id", idBarang);
        client.get(Config.URL_BASE_API + "/hapus_barang.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                loading.dismiss();
                try {
                    String status = response.getString("status");
                    if (status.equalsIgnoreCase("berhasil")) {
                        Toast.makeText(EditBarangActivity.this, "Berhasil menghapus produk!", Toast.LENGTH_SHORT).show();
                        Intent in = new Intent(getBaseContext(), MainActivity.class);
                        startActivity(in);
                    } else {
                        Toast.makeText(EditBarangActivity.this, "Gagal menghapus produk!", Toast.LENGTH_SHORT).show();
                        String error = response.getString("error");
                        Log.e("TAG", "error hapus barang  : " + error);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                loading.dismiss();
                throwable.printStackTrace();
                Toast.makeText(EditBarangActivity.this, "Terjadi kesalahan! Cek koneksi internet!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == btnUpload) {
            showFileChooser();
        }
        if (v == btnHapus) {
            builder.setMessage("Apakah anda yakin menghapus barang ini ? ");
            builder.setPositiveButton("IYA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    hapusBarang(idBarang);
                }
            }).setNegativeButton("TIDAK", null);
            builder.show();
        }
        if (v == btnEdit) {
            builder.setMessage("Apakah anda yakin mengubah keterangan barang ini? ");
            builder.setPositiveButton("IYA", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    String diskon = detail_diskon.getText().toString().trim();
                    String name = detail_nama.getText().toString().trim();
                    String harga_barang = detail_harga.getText().toString().trim();
                    String berat = detail_berat.getText().toString().trim();
                    String stok = detail_stok.getText().toString().trim();
                    String deskripsi = detail_desk.getText().toString().trim();
                    String image;
                    if (null == diskon) diskon = "0";
                    if (null != bitmap) {
                        image = getStringImage(bitmap);
                    } else {
                        image = null;
                    }

                    if (isIsi(name) && isIsi(berat) && isIsi(harga_barang) && isIsi(deskripsi) && isIsi(stok) && null != kategoriDipilih) {
                        uploadImageAsync(idBarang, name, berat, harga_barang, deskripsi, stok, diskon, image);
                    } else {
                        Toast.makeText(EditBarangActivity.this, "Ada yang belum di isi!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).setNegativeButton("TIDAK", null);
            builder.show();

        }
    }

    public void getKategori() {
        if (!arrCategoryNama.isEmpty() || !arrCategoryId.isEmpty()) {
            arrCategoryId.clear();
            arrCategoryNama.clear();
        }
        loading.show();
        //arrCategory = new ArrayList<Category>();
        arrCategoryNama.add("Pilih Kategori");
        arrCategoryId.add("");

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Config.URL_BASE_API + "/get_cat.php?id_category=", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //                super.onSuccess(statusCode, headers, response);
                loading.dismiss();
                try {
                    //untuk baris pertama

                    JSONArray arr = response.getJSONArray("category");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
//                        k = new Category();
//                        k.setCat(obj.getString("category"));
//                        k.setIdcat(obj.getString("id_category"));
//                        arrCategory.add(k);
                        arrCategoryNama.add(obj.getString("category"));
                        arrCategoryId.add(obj.getString("id_category"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                //AdapterCategory adapterCategory = new AdapterCategory(UploadActivity.this, R.layout.activity_upload_foto_item, arrCategory);
                ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(EditBarangActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, arrCategoryNama);
                adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapterCategory);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
                loading.dismiss();
                throwable.printStackTrace();
                Toast.makeText(EditBarangActivity.this, "Cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                kategoriDipilih = null;
            }
        });
    }

    public boolean isIsi(String nama) {
        if (TextUtils.isEmpty(nama)) {
            return false;
        }
        return true;
    }
    //    alert dialog trial
    public void alertDialogTrial(){
        AlertDialog.Builder builder=new AlertDialog.Builder(EditBarangActivity.this);
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