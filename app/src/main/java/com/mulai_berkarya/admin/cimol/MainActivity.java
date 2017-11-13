package com.mulai_berkarya.admin.cimol;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.mulai_berkarya.admin.server.Config;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.mikepenz.actionitembadge.library.ActionItemBadge;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import cz.msebera.android.httpclient.Header;

import static com.mulai_berkarya.admin.server.Config.URL_BASE_API;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    static Button notifCount;
    static int mNotifCount = 0;
    static MenuItem item;
    static ArrayList<String> arrNoHP = new ArrayList<>();
    // String tag = "listbrg";
    ListView lvbrg;
    Button co1;
    //String tag = ListBarangActivity.class.getSimpleName();
    String url = URL_BASE_API + "/daftar_barang.php";
    SessionManager session;
    String id_owner;
    private TextView mncount;
    private TabLayout tabLayout;
    private Intent i;
    private ViewPager viewPager;
    private String[] idbarang;
    private String[] namabarang;
    private String[] hargabarang;
    private String[] desk;
    private String[] berat;
    //	KelasSM kelasm;
    private String[] gambar;
    private String listbar;
    private TextView textView;

    static void setNotifCount(int count) {
        mNotifCount = count;
        if (mNotifCount > 0) {
            ActionItemBadge.update(item, mNotifCount);
        } else {
            ActionItemBadge.hide(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        alert
        alertDialogTrial();

        //buat db
        DatabaseHandler databaseHandler = new DatabaseHandler(this);
        databaseHandler.createDataBase();

        if (arrNoHP.size() <= 0)
            getNoHP();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, UploadActivity.class);
                startActivity(i);
            }


        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(), FragmentPagerItems.with(this)
                .add("Terbaru", TerbaruFragment.class)
                .add("Diskon", DiskonFragment.class)
                .add("Populer", PopulerFragment.class)
                .create());

        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);

        SmartTabLayout viewPagerTab = (SmartTabLayout) findViewById(R.id.viewpagerTab);
        viewPagerTab.setViewPager(viewPager);

        ListView listView = (ListView) findViewById(R.id.lvbarang);

        //notif
        session = new SessionManager(getApplicationContext());
        session.checkLogin();

        HashMap<String, String> user = session.getUserDetails();

        id_owner = user.get(SessionManager.KEY_ID);
        FirebaseMessaging.getInstance().subscribeToTopic("news");
        if (!session.getStatusFCM()) {
            String token = FirebaseInstanceId.getInstance().getToken();
            if (null != token) {
                //Log.e("token", token);
                sendRegistrationToServer(token);
            }
        }
        //over

    }

    //notif
    private void sendRegistrationToServer(String token) {
        // TODO: Implement this method to send token to your app server.
        AsyncHttpClient clients = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.add("fcm_id", token);
        params.add("id_owner", id_owner);
        clients.post(Config.URL_BASE_API + "/register_fcm_owner.php", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                String error = responseBody.toString();
                Log.e("TAG", error);
                //Log.e("TAG", "ok");
                //Toast.makeText(MainActivity.this, "welcome to CimoL", Toast.LENGTH_SHORT).show();
                session.setStatusFCM(true);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                error.printStackTrace();
            }
        });

    }
    //over

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            keluar();
        }
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        // item = menu.findItem(R.id.action_settings);

        //ActionItemBadge.update(menu.findItem(R.id.action_settings), 10);
        // ActionItemBadge.update(this, item,
        //        Entypo.Icon.ent_news, ActionItemBadge.BadgeStyles.YELLOW, mNotifCount);
        //ActionItemBadge.hide(menu.findItem(R.id.action_settings));

        //  if (mNotifCount > 0) {
        //     ActionItemBadge.update(item, mNotifCount);
        // } else {
//            ActionItemBadge.hide(item);
        //     ActionItemBadge.update(item, mNotifCount);
        // }


        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_search) {
            Intent i = new Intent(MainActivity.this, ListCariAct.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_dafbar) {
            Intent bi = new Intent(this, UploadActivity.class);
            startActivity(bi);

        } else if (id == R.id.nav_category) {
            Intent in = new Intent(this, CategoryActivity.class);
            startActivity(in);

        } else if (id == R.id.nav_pesan) {
            Intent ui = new Intent(this, Feedback.class);
            startActivity(ui);

        } else if (id == R.id.nav_resi) {
            Intent ig = new Intent(this, TransAdminAct.class);
            startActivity(ig);


        } else if (id == R.id.nav_about) {
            Intent ig = new Intent(this, EditOwnerActivity.class);
            startActivity(ig);

        } else if (id == R.id.nav_tentang) {
            Intent ui = new Intent(this, TentangActivity.class);
            startActivity(ui);

        } else if (id == R.id.nav_setting) {
            session.logoutUser();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void getNoHP() {
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Config.URL_BASE_API + "/daftar_owner.php", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                //Log.e("TAG", response.toString());
                try {
                    JSONArray arr = response.getJSONArray("owner");
                    for (int i = 0; i < arr.length(); i++) {
                        JSONObject obj = arr.getJSONObject(i);
                        String no = obj.getString("telp_owner");
                        arrNoHP.add(no);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
    }

    //    alert dialog trial
    public void alertDialogTrial(){
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
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

