package com.mulai_berkarya.admin.cimol;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
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

public class KaryawanTambah extends AppCompatActivity {

    Button btnEdit, btnHapus;
    EditText etNama, etEmail, etPassword, etPasswordKonfirm, etTelp, etBbm, etLine;
    ProgressBar progressBar;
    ProgressDialog pDialog;



    String id, nama, email, telp, bbm, line;

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

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        btnEdit = (Button) findViewById(R.id.karyawan_btnEdit);
        btnEdit.setText("TAMBAH");
        btnHapus = (Button) findViewById(R.id.karyawan_btnHapus);
        btnHapus.setVisibility(View.GONE);

        //set semua enable
        allSetEnable(etNama, true);
        allSetEnable(etEmail, true);
        allSetEnable(etPassword, true);
        allSetEnable(etPasswordKonfirm, true);
        allSetEnable(etTelp, true);
        allSetEnable(etBbm, true);
        allSetEnable(etLine, true);

        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading");
        pDialog.setCancelable(true);

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nama = etNama.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String bbm = etBbm.getText().toString().trim();
                String telp = etTelp.getText().toString().trim();
                String line = etLine.getText().toString().trim();
                String pass = etPassword.getText().toString().trim();
                String passKonfirm = etPasswordKonfirm.getText().toString().trim();

                if (nama.isEmpty() || email.isEmpty() || bbm.isEmpty() || telp.isEmpty()
                        || line.isEmpty() || pass.isEmpty() || passKonfirm.isEmpty()) {
                    Toast.makeText(KaryawanTambah.this, "Ada yang belum di isi!", Toast.LENGTH_SHORT).show();
                } else {

                    boolean semuaValid = false;
                    if (isValidEmail(email)) {
                        semuaValid = true;
                    } else {
                        semuaValid = false;
                        Toast.makeText(KaryawanTambah.this, "Email tidak valid!", Toast.LENGTH_SHORT).show();
                    }
                    if (pass.equals(passKonfirm)) {
                        semuaValid = true;
                    } else {
                        semuaValid = false;
                        Toast.makeText(KaryawanTambah.this, "Password tidak sama!", Toast.LENGTH_SHORT).show();
                    }
                    if (semuaValid) {
                        tambahKaryawan(nama, email, telp, bbm, line, pass);
                    }
                }

            }
        });

/*
        etPasswordKonfirm.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String sPass = etPassword.getText().toString().trim();
                String sKonfirm = etPasswordKonfirm.getText().toString().trim();
                if (sPass.isEmpty()) {
                    Toast.makeText(KaryawanTambah.this, "Password masih kosong!", Toast.LENGTH_SHORT).show();
                } else {
                    if (sPass.equals(sKonfirm)) {
                        btnEdit.setEnabled(true);
                    } else {
                        btnEdit.setEnabled(false);
                        etPasswordKonfirm.setError("Password tidak sama!");
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
                String sEmail= etEmail.getText().toString().trim();
                if(!sEmail.isEmpty()){

                }
            }

        });
*/


    }

    private void tambahKaryawan(String nama, String email, String telp, String bbm, String line, String pass) {
        pDialog.show();
        progressBar.setVisibility(View.GONE);
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
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
                        Toast.makeText(KaryawanTambah.this, "Berhasil!", Toast.LENGTH_SHORT).show();
                        setResult(Activity.RESULT_OK);
                        KaryawanTambah.this.finish();

                    } else {
                        Toast.makeText(KaryawanTambah.this, "Username sudah ada!", Toast.LENGTH_SHORT).show();
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
                Toast.makeText(KaryawanTambah.this, "Cek koneksi internet!", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pDialog.dismiss();
                progressBar.setVisibility(View.GONE);
                Log.e("TAG", "karyawan detail : " + responseString);
                Toast.makeText(KaryawanTambah.this, "Cek koneksi internet!", Toast.LENGTH_SHORT).show();
                throwable.printStackTrace();
            }
        });
    }

    private void allSetEnable(EditText editText, boolean b) {
        editText.setEnabled(b);
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
        AlertDialog.Builder builder=new AlertDialog.Builder(KaryawanTambah.this);
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
