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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mulai_berkarya.admin.server.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cz.msebera.android.httpclient.Header;

public class LoginAct extends AppCompatActivity {
    Button daftar, login, editAdmin;
    Intent a;
    EditText email, password;
    String url, success, id_owner;
    SessionManager session;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        alertDialogTrial();

        session = new SessionManager(getApplicationContext());

        if (session.isLoggedIn()) {
            startActivity(new Intent(LoginAct.this, MainActivity.class));
        }
        //buat db
        DatabaseHandler dbHelper = new DatabaseHandler(this);
        dbHelper.createDataBase();

        login = (Button) findViewById(R.id.login);
        //daftar = (Button) findViewById(R.id.daftar);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);

        //loading bar
        pDialog = new ProgressDialog(LoginAct.this);
        pDialog.setMessage("Loading...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(true);

        login.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (email.getText().toString().trim().length() > 0 && password.getText().toString().trim().length() > 0) {
                    if (isValidEmail(email.getText().toString().trim())) {
                        login(email.getText().toString().trim(), password.getText().toString().trim());
                    } else {
                        Toast.makeText(LoginAct.this, "Format email salah!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Username/password masih kosong!", Toast.LENGTH_LONG).show();
                }


            }
        });

    }

    public void login(String username, String password) {
        pDialog.show();
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("email_owner", username);
        params.add("password", password);
        client.post(Config.URL_BASE_API + "/login_owner.php", params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                pDialog.dismiss();
                //Log.e("TAG",response.toString());
                try {
                    String success = response.getString("success");
                    if (success.equalsIgnoreCase("1")) {
                        JSONArray array = response.getJSONArray("login");
                        JSONObject object = array.getJSONObject(0);
                        String idOwner = object.getString("id_owner");
                        String namaOwner = object.getString("nama_owner");
                        String emailOwner = object.getString("email_owner");
                        session.createLoginSession(namaOwner, emailOwner, idOwner);

                        a = new Intent(LoginAct.this, MainActivity.class);
                        startActivity(a);
                        finish();

                    } else {
                        Toast.makeText(LoginAct.this, "Cek kembali email dan password!", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONObject errorResponse) {
                pDialog.dismiss();
                throwable.printStackTrace();
                if (null != errorResponse) Log.e("TAG", errorResponse.toString());
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                pDialog.dismiss();
                throwable.printStackTrace();
                if (!responseString.isEmpty()) Log.e("TAG", "Login error : " + responseString);
            }
        });


    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        keluar();
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    long back_pressed;
    Toast toast;

    //notifikasi jika akan keluar
    public void keluar() {
        if (back_pressed + 2000 > System.currentTimeMillis()) {
            toast.cancel();
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_HOME);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            toast = Toast.makeText(getBaseContext(), "Tekan sekali lagi untuk keluar!", Toast.LENGTH_SHORT);
            toast.show();
        }
        back_pressed = System.currentTimeMillis();
    }
    //    alert dialog trial
    public void alertDialogTrial(){
        AlertDialog.Builder builder=new AlertDialog.Builder(LoginAct.this);
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
