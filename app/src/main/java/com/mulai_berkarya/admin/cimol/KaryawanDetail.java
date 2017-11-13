package com.mulai_berkarya.admin.cimol;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.mulai_berkarya.admin.server.Config;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class KaryawanDetail extends AppCompatActivity {

    Button btnEdit, btnHapus;
    EditText etNama, etEmail, etPassword, etPasswordKonfirm, etTelp, etBbm, etLine;
    TextView tvPassword, tvPasswordKonfirm;
    ProgressBar progressBar;

    boolean isEdit = false;

    String id, nama, email, telp, bbm, line;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.karyawan_detail);
//        alert
        alertDialogTrial();
        etNama = (EditText) findViewById(R.id.karyawan_nama);
        etEmail = (EditText) findViewById(R.id.karyawan_email);
        etPassword = (EditText) findViewById(R.id.karyawan_password);
        etPasswordKonfirm = (EditText) findViewById(R.id.karyawan_passwordKonfirm);
        etTelp = (EditText) findViewById(R.id.karyawan_telp);
        etBbm = (EditText) findViewById(R.id.karyawan_bbm);
        etLine = (EditText) findViewById(R.id.karyawan_line);

        tvPassword = (TextView) findViewById(R.id.karyawan_passwordTV);
        tvPasswordKonfirm = (TextView) findViewById(R.id.karyawan_passwordKonfirmTV);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnEdit = (Button) findViewById(R.id.karyawan_btnEdit);
        btnHapus = (Button) findViewById(R.id.karyawan_btnHapus);


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading");
        pDialog.setCancelable(true);

        Bundle b = getIntent().getExtras();
        id = b.getString("id");
        nama = b.getString("nama");
        email = b.getString("email");
        telp = b.getString("telp");
        bbm = b.getString("bbm");
        line = b.getString("line");

        etNama.setText(nama);
        etEmail.setText(email);
        etTelp.setText(telp);
        etBbm.setText(bbm);
        etLine.setText(line);

        hideKeyboard(this.getCurrentFocus());

        if (!isEdit) {
            tvPassword.setVisibility(View.GONE);
            tvPasswordKonfirm.setVisibility(View.GONE);
            allSetVisible(etPassword, View.GONE);
            allSetVisible(etPasswordKonfirm, View.GONE);
        }

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hapusDialog(id, nama);
            }
        });
        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isEdit) {
                    etNama.setEnabled(true);
                    allSetEnable(etNama, true);
                    allSetEnable(etEmail, true);
                    allSetEnable(etBbm, true);
                    allSetEnable(etTelp, true);
                    allSetEnable(etLine, true);
                    allSetEnable(etPassword, true);
                    allSetEnable(etPasswordKonfirm, true);

                    tvPassword.setVisibility(View.VISIBLE);
                    tvPasswordKonfirm.setVisibility(View.VISIBLE);
                    allSetVisible(etPassword, View.VISIBLE);
                    allSetVisible(etPasswordKonfirm, View.VISIBLE);
                    isEdit = true;
                    btnEdit.setText("SIMPAN");
                } else {
                    String nama = etNama.getText().toString().trim();
                    String email = etEmail.getText().toString().trim();
                    String bbm = etBbm.getText().toString().trim();
                    String telp = etTelp.getText().toString().trim();
                    String line = etLine.getText().toString().trim();
                    String pass = etPassword.getText().toString().trim();
                    String passKonfirm = etPasswordKonfirm.getText().toString().trim();

                    if (nama.isEmpty() || email.isEmpty() || bbm.isEmpty() || telp.isEmpty()
                            || line.isEmpty() || pass.isEmpty() || passKonfirm.isEmpty()) {
                        Toast.makeText(KaryawanDetail.this, "Ada yang belum di isi!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (isValidEmail(email)) {

                        }
                        editKaryawan(id, nama, email, telp, bbm, line, pass);
                    }

                }
            }
        });


        etPasswordKonfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isEdit) {
                    String sPass = etPassword.getText().toString().trim();
                    String sKonfirm = etPasswordKonfirm.getText().toString().trim();
                    if (sPass.isEmpty()) {
                        Toast.makeText(KaryawanDetail.this, "Password masih kosong!", Toast.LENGTH_SHORT).show();
                    } else {
                        if (sPass.equals(sKonfirm)) {
                            btnEdit.setEnabled(true);
                        } else {
                            etPasswordKonfirm.setError("Password tidak Sama!");
                            btnEdit.setEnabled(false);
                        }
                    }
                }
            }
        });

        etEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String sEmail = etEmail.getText().toString().trim();
                if(isValidEmail(sEmail)) {
                    btnEdit.setEnabled(true);
                }else{
                    btnEdit.setEnabled(false);
                    etEmail.setError("Invalid email!");
                }
            }

        });



    }

    private void editKaryawan(String id, String nama, String email, String telp, String bbm, String line, String pass) {
        pDialog.show();
        progressBar.setVisibility(View.GONE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("id", id);
        params.add("nama", nama);
        params.add("email", email);
        params.add("telp", telp);
        params.add("bbm", bbm);
        params.add("line", line);
        params.add("pass", pass);
        client.post(Config.URL_BASE_API + "/karyawan_edit.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                pDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                String status = null;
                try {
                    status = response.getString("status");
                    if (status.equalsIgnoreCase("berhasil")) {
                        Toast.makeText(KaryawanDetail.this, "Data berhasil di ubah!", Toast.LENGTH_SHORT).show();
                        allSetEnable(etNama, false);
                        allSetEnable(etEmail, false);
                        allSetEnable(etTelp, false);
                        allSetEnable(etBbm, false);
                        allSetEnable(etLine, false);
                        allSetVisible(etPassword, View.GONE);
                        allSetVisible(etPasswordKonfirm, View.GONE);
                        btnEdit.setText("EDIT");
                        isEdit = false;

                        setResult(RESULT_OK);
                        KaryawanDetail.this.finish();

                    } else {
                        Toast.makeText(KaryawanDetail.this, "Data gagal di ubah!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "karyawan detail : " + errorResponse);
                Toast.makeText(KaryawanDetail.this, "Cek koneksi internet!", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "karyawan detail : " + responseString);
                Toast.makeText(KaryawanDetail.this, "Cek koneksi internet!", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
    }

    private void allSetVisible(EditText editText, int visible) {
        editText.setVisibility(visible);
    }

    private void allSetEnable(EditText editText, boolean b) {
        editText.setEnabled(b);
    }

    private void hapusDialog(final String idKaryawan, String nama) {
        android.app.AlertDialog.Builder adib = new android.app.AlertDialog.Builder(this);
        adib.setTitle("Konfirmasi");

        adib
                .setMessage("Apakah yakin anda mau \"" + nama + " \"")
                .setCancelable(true)
                .setPositiveButton("Ya", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        hapusKaryawan(idKaryawan);
                    }
                })
                .setNegativeButton("Tidak", null);
        android.app.AlertDialog aldi = adib.create();
        aldi.show();
    }

    private void hapusKaryawan(String idKaryawan) {
        pDialog.show();
        progressBar.setVisibility(View.GONE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("id_karyawan", idKaryawan);
        client.get(Config.URL_BASE_API + "/karyawan_hapus.php", params, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                pDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                try {
                    String status = response.getString("status");
                    if (status.equalsIgnoreCase("berhasil")) {
                        setResult(RESULT_OK);
                        KaryawanDetail.this.finish();
                        Toast.makeText(KaryawanDetail.this, "ID Karyawan berhasil di hapus!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(KaryawanDetail.this, "ID Karyawan gagal di hapus!", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                Toast.makeText(KaryawanDetail.this, "Terjadi kesalahan. Coba lagi nanti!", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
                Log.e("TAG", "karyawan detail : " + errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                throwable.printStackTrace();
                Toast.makeText(KaryawanDetail.this, "Terjadi kesalahan. Coba lagi nanti!", Toast.LENGTH_SHORT).show();
                Log.e("TAG", "karyawan detail : " + responseString);
            }
        });
    }

    void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(
                    Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    // validating email id
    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
    //    alert dialog trial
    public void alertDialogTrial(){
        AlertDialog.Builder builder=new AlertDialog.Builder(KaryawanDetail.this);
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
