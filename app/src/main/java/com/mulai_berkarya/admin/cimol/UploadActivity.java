package com.mulai_berkarya.admin.cimol;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mulai_berkarya.admin.server.Config;
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

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

public class UploadActivity extends AppCompatActivity implements View.OnClickListener {

    //ArrayList<Category> arrCategory;
    ArrayList<String> arrCategoryNama, arrCategoryId;
    String kategoriDipilih = null;
    ProgressDialog loading;
    private Button buttonChoose;
    private Button buttonUpload;
    private ImageView imageView;
    private EditText editTextName;
    private EditText editTextName1;
    private EditText editTextName2;
    private EditText editTextName3;
    private EditText editTextName4;
    private EditText editTextName5;
    private Spinner spinner;
    private Bitmap bitmap;
    private int PICK_IMAGE_REQUEST = 1;
    private String UPLOAD_URL = Config.URL_BASE_API + "/Upload.php";
    private String KEY_IMAGE = "gambar";
    private String KEY_NAME = "name";
    private String KEY_BERAT = "berat";
    private String KEY_HARBAR = "harga_barang";
    private String KEY_DESK = "deskripsi";
    private String KEY_STOK = "stok";
    private String KEY_DISKON = "diskon";
    private String KEY_KATEGORI = "kategori";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
//alert
        alertDialogTrial();

        buttonChoose = (Button) findViewById(R.id.buttonChoose);
        buttonUpload = (Button) findViewById(R.id.buttonUpload);

        editTextName = (EditText) findViewById(R.id.editText);
        editTextName1 = (EditText) findViewById(R.id.editText1);
        editTextName2 = (EditText) findViewById(R.id.editText2);
        editTextName3 = (EditText) findViewById(R.id.editText3);
        editTextName4 = (EditText) findViewById(R.id.editText4);
        editTextName5 = (EditText) findViewById(R.id.editText5);
        spinner = (Spinner) findViewById(R.id.spinnerKategori);

        buttonUpload.setEnabled(true);

        imageView = (ImageView) findViewById(R.id.imageView);

        loading = ProgressDialog.show(this, null, "Please wait...", false, false);
        loading.setCancelable(true);
        loading.dismiss();

        arrCategoryNama = new ArrayList<String>();
        arrCategoryId = new ArrayList<String>();

        buttonChoose.setOnClickListener(this);
        buttonUpload.setOnClickListener(this);

        getKategori();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //                Category cat = (Category) adapterView.getItemAtPosition(i);
                if (i != 0) {
                    //Log.e("TAG", "Kategori : " + arrCategoryId.get(i));
                    //Log.e("TAG", "nama Kategori : " + arrCategoryNama.get(i));
                    kategoriDipilih = arrCategoryId.get(i);

                } else {
                    Toast.makeText(UploadActivity.this, "Kategori belum di pilih!", Toast.LENGTH_SHORT)
                            .show();
                    kategoriDipilih = null;
                }
                //                Toast.makeText(UploadActivity.this, "id : " + cat.getIdcat()+"\n" +"nama : " + cat.getCat(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                kategoriDipilih = null;
            }
        });
    }

    public String getStringImage(Bitmap bmp) {
        if (bmp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            return encodedImage;
        } else {
            return null;
        }
    }

    private void uploadImageAsync() {
        loading.show();
        buttonUpload.setEnabled(false);

        String image = getStringImage(bitmap);
        String name = editTextName.getText().toString().trim();
        String berat = editTextName1.getText().toString().trim();
        String harga_barang = editTextName2.getText().toString().trim();
        String deskripsi = editTextName3.getText().toString().trim();
        String stok = editTextName4.getText().toString().trim();
        String diskon = editTextName5.getText().toString().trim();

        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put(KEY_IMAGE, image);
        params.put(KEY_NAME, name);
        params.put(KEY_BERAT, berat);
        params.put(KEY_HARBAR, harga_barang);
        params.put(KEY_DESK, deskripsi);
        params.put(KEY_STOK, stok);
        params.put(KEY_DISKON, diskon);
        params.put(KEY_KATEGORI, kategoriDipilih);
        client.post(UPLOAD_URL, params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                loading.dismiss();
                throwable.printStackTrace();
                Toast.makeText(UploadActivity.this, "Upload gagal! Cek koneksi internet anda!",
                        Toast.LENGTH_SHORT).show();

                buttonUpload.setEnabled(true);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                loading.dismiss();
                buttonUpload.setEnabled(true);

                if (null != responseString && responseString.contains("Successfully Uploaded")) {
                    Toast.makeText(UploadActivity.this, responseString, Toast.LENGTH_SHORT).show();
                    //Intent in = new Intent(UploadActivity.this, MainActivity.class);
                    //startActivity(in);
                    Toast.makeText(UploadActivity.this, "Barang berhasil di upload!", Toast.LENGTH_SHORT)
                            .show();
                    //broadcastNotifikasi("Produk Baru!", editTextName.getText().toString().trim());
                    Intent in = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(in);
                } else {
                    Toast.makeText(UploadActivity.this, "Upload gagal!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void broadcastNotifikasi(String judul, String isi) {
        loading.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("judul_pesan", judul);
        params.add("isi_pesan", isi);
        client.post(Config.URL_BASE_API + "/push_bc_product.php", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                loading.dismiss();
                Intent in = new Intent(UploadActivity.this, MainActivity.class);
                startActivity(in);
                throwable.printStackTrace();
                Log.d("TAG", responseString);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, String responseString) {
                loading.dismiss();
                Intent in = new Intent(getBaseContext(), MainActivity.class);
                startActivity(in);
                Log.e("TAG", responseString);
                Toast.makeText(UploadActivity.this, "Produk berhasil di upload!", Toast.LENGTH_SHORT)
                        .show();
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

        if (requestCode == PICK_IMAGE_REQUEST
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onClick(View v) {

        if (v == buttonChoose) {
            showFileChooser();
        }

        if (v == buttonUpload) {
            loading.show();
            String name = editTextName.getText().toString().trim();
            String berat = editTextName1.getText().toString().trim();
            String harga_barang = editTextName2.getText().toString().trim();
            String deskripsi = editTextName3.getText().toString().trim();
            String stok = editTextName4.getText().toString().trim();
            String diskon = editTextName5.getText().toString().trim();
            String gambar = getStringImage(bitmap);
            if (isIsi(name) && isIsi(berat) && isIsi(harga_barang) && isIsi(deskripsi) && isIsi(stok)
                    && null != bitmap && null != kategoriDipilih) {
                //uploadImage();
                loading.dismiss();
                uploadImageAsync();
            } else {
                Toast.makeText(this, "Ada yang belum di isi!", Toast.LENGTH_SHORT).show();
                loading.dismiss();
            }
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
                ArrayAdapter<String> adapterCategory = new ArrayAdapter<String>(UploadActivity.this,
                        android.R.layout.simple_spinner_dropdown_item, arrCategoryNama);
                adapterCategory.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner.setAdapter(adapterCategory);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString,
                                  Throwable throwable) {
                //super.onFailure(statusCode, headers, responseString, throwable);
                if (!responseString.isEmpty()) Log.e("TAG", "upload : " + responseString);
                loading.dismiss();
                throwable.printStackTrace();
                Toast.makeText(UploadActivity.this, "Cek koneksi internet anda", Toast.LENGTH_SHORT).show();
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
        AlertDialog.Builder builder=new AlertDialog.Builder(UploadActivity.this);
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