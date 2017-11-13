package com.mulai_berkarya.admin.cimol;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.util.TextUtils;

import static com.mulai_berkarya.admin.server.Config.URL_BASE_API;

public class EditOwnerActivity extends AppCompatActivity implements OnClickListener {
    private static final String LINK_UTK_REGISTRASI = URL_BASE_API + "/edit_owner2.php";
    //private static final String URL = URL_BASE + "/edit_owner.php";
    private static final String TAG_BERHASIL = "sukses";
    private static final String TAG_PESAN = "pesan";
    TextView tvError;
    String idOwner;
    AutoCompleteTextView etAskot;
    // panggil ClassJsonParser ke sisni
    private EditText etNamaPemilik, etNamaToko, etEmail, etTelp, etRek, etPassword2, etKeterangan,
            etAlamat, etBBM, etLine;
    private Button tombolEdit;
    // utk progress bar
    private DatabaseHandler databaseHandler;
    FloatingActionButton fabKaryawan;
    ProgressBar progressBar;
    private ProgressDialog pDialog;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editowner);
//        alert
        alertDialogTrial();

        databaseHandler = new DatabaseHandler(this);
        databaseHandler.createDataBase();

        loading = ProgressDialog.show(this, null, "Please wait...", false, false);
        loading.setCancelable(true);
        loading.dismiss();

        etNamaPemilik = (EditText) findViewById(R.id.editNM);
        etNamaToko = (EditText) findViewById(R.id.editNamaToko);
        etEmail = (EditText) findViewById(R.id.editMail);
        etBBM = (EditText) findViewById(R.id.editBBM);
        etLine = (EditText) findViewById(R.id.editLine);
        etTelp = (EditText) findViewById(R.id.editTelp);
        etRek = (EditText) findViewById(R.id.editRek);
        etAskot = (AutoCompleteTextView) findViewById(R.id.editAskot);
        etKeterangan = (EditText) findViewById(R.id.editKet);
        etAlamat = (EditText) findViewById(R.id.editAlamat);
        fabKaryawan = (FloatingActionButton) findViewById(R.id.editFabAdmin);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        tombolEdit = (Button) findViewById(R.id.bReg);
        tombolEdit.setOnClickListener(this);



        etAskot.setEnabled(true);
        etNamaPemilik.setEnabled(true);
        etNamaToko.setEnabled(true);

        etAlamat.setEnabled(true);
        etRek.setEnabled(true);
        etKeterangan.setEnabled(true);
        etEmail.setEnabled(true);
        etBBM.setEnabled(true);
        etLine.setEnabled(true);
        etTelp.setEnabled(true);
//        if (etNamaToko.equals("")) {
//            etNamaToko.setEnabled(true);
//        } else {
//            etNamaToko.setEnabled(false);
//        }

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading");
        pDialog.setCancelable(true);

        getDataOwner();
        hideKeyboard(this.getCurrentFocus());

        etAskot.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, databaseHandler.getAllKabNama()));
        etAskot.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String dipilih = adapterView.getItemAtPosition(i).toString();
                //int posisi = Arrays.asList(arrKab).indexOf(dipilih);
                etAskot.setText(dipilih);
            }
        });

        fabKaryawan.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EditOwnerActivity.this, KaryawanList.class));
            }
        });

    }

    private void getDataOwner() {
        progressBar.setVisibility(View.GONE);
        pDialog.show();

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(LINK_UTK_REGISTRASI, null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                pDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                try {
                    JSONObject obj = response.getJSONObject(0);
                    idOwner = obj.getString("id_owner");
                    etNamaPemilik.setText(obj.getString("nama_owner"));
                    etNamaToko.setText(obj.getString("nama_toko"));
                    etEmail.setText(obj.getString("email_owner"));
                    etBBM.setText(obj.getString("bbm"));
                    etLine.setText(obj.getString("line"));
                    etTelp.setText(obj.getString("telp_owner"));
                    etAlamat.setText(obj.getString("alamat_owner"));
                    etKeterangan.setText(obj.getString("keterangan"));
                    etRek.setText(obj.getString("rekening"));
                    etAskot.setText(obj.getString("asal_kota"));


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pDialog.dismiss();
                Toast.makeText(EditOwnerActivity.this, "Cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
                //finish();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pDialog.dismiss();
                Toast.makeText(EditOwnerActivity.this, "Cek koneksi internet anda", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
                //finish();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

//    @Override
//    public void onClick(View v) {
//
//        String namaPemilik = etNamaPemilik.getText().toString().trim();
//        String namaToko = etNamaToko.getText().toString().trim();
//        String email = etEmail.getText().toString().trim();
//        String bbm = etBBM.getText().toString().trim();
//        //String line = etLine.getText().toString().trim();
//        String line = "";
//        String telp = etTelp.getText().toString().trim();
//        String rek = etRek.getText().toString().trim();
//        String askot = etAskot.getText().toString().trim();
//        String ket = etKeterangan.getText().toString().trim();
//        String alamat = etAlamat.getText().toString().trim();
//
//        if (isIsi(namaPemilik) && isIsi(namaToko) && isIsi(email)
//                && isIsi(askot) && isIsi(telp) && isIsi(alamat)) {
//            if (!isIsi(ket)) {
//                ket = "";
//            } else {
//
//                if (isValidEmail(email)) {
//                    progressBar.setVisibility(View.VISIBLE);
//                    loading.show();
//
//                    AsyncHttpClient client = new AsyncHttpClient();
//                    RequestParams params = new RequestParams();
//                    params.add("id_owner", idOwner);
//
//                    params.add("nama_owner", namaPemilik);
//                    params.add("nama_toko", namaToko);
//                    params.add("email_owner", email);
//                    params.add("telp_owner", telp);
//                    params.add("alamat_owner", alamat);
//                    params.add("asal_kota", askot);
//                    params.add("keterangan", ket);
//                    params.add("rekening", rek);
//                    params.add("bbm", bbm);
//                    params.add("line", line);
//
//                    client.post(LINK_UTK_REGISTRASI, params, new JsonHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
//                            progressBar.setVisibility(View.GONE);
//                            loading.dismiss();
//
//                            try {
//                                String res = response.getString("status");
//                                if (res.equalsIgnoreCase("berhasil")) {
//                                    Toast.makeText(EditOwnerActivity.this, "Edit berhasil!", Toast.LENGTH_SHORT).show();
//
//                                    etAskot.setEnabled(false);
//                                    etNamaPemilik.setEnabled(false);
//                                    etNamaToko.setEnabled(false);
//                                    etAlamat.setEnabled(false);
//                                    etRek.setEnabled(false);
//                                    etKeterangan.setEnabled(false);
//                                    etEmail.setEnabled(false);
//                                    etBBM.setEnabled(false);
//                                    etLine.setEnabled(false);
//                                    etTelp.setEnabled(false);
//                                }
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                                Toast.makeText(EditOwnerActivity.this, "Terjadi Kesalahan. Cek kembali koneksi internet anda", Toast.LENGTH_SHORT).show();
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
//                            loading.dismiss();
//                            throwable.printStackTrace();
//                            Toast.makeText(EditOwnerActivity.this, "Terjadi Kesalahan...", Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
//                            throwable.printStackTrace();
//                            Toast.makeText(EditOwnerActivity.this, "Terjadi Kesalahan...", Toast.LENGTH_SHORT).show();
//                            progressBar.setVisibility(View.GONE);
//                            loading.dismiss();
//
//                        }
//                    });
//                } else {
//                    Toast.makeText(this, "Format email salah!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
public void onClick(View v){
    alertDialogTrial();
}

    public boolean isIsi(String nama) {
        if (!TextUtils.isEmpty(nama)) return true;
        else return false;
    }

    void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
        }
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    //    alert dialog trial
    public void alertDialogTrial(){
        AlertDialog.Builder builder=new AlertDialog.Builder(EditOwnerActivity.this);
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
