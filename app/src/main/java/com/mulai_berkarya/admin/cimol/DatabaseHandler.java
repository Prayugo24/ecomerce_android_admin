package com.mulai_berkarya.admin.cimol;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DB_VER = 6;
    //private static final String DB_NAME = "ongkir.db";
    private static final String DB_NAME = "rajaongkir.sqlite";
    private static final String tag = "ongkir";
    /*
        private static final String TABLE_ONG = "tb_ongkir";
        private static final String COL_PROV = "provinsi";
        private static final String COL_KAB = "kabupaten_kota";
    */
    //private static final String DB_PATH = "/data/data/com.cimol_trial.member.cimol/databases/";
    private static String DB_PATH;//="/data/data/com.cimol_trial.member.cimol/databases/";
    private Context context;
    private SQLiteDatabase db;
    private Cursor cursor;

    public DatabaseHandler(Context ctx) {
        super(ctx, DB_NAME, null, DB_VER);
        // TODO Auto-generated constructor stub
        context = ctx;
        if (android.os.Build.VERSION.SDK_INT >= 17) {
            DB_PATH = context.getApplicationInfo().dataDir + "/databases/";
        } else {
            DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
        }

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void createDataBase() {
        if (DataBaseisExist()) {
            //do nothing - database already exist
            //Toast.makeText(context, "Database Sudah Ada", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "Database sudah ada");
        } else {
            //By calling this method and empty database will be created into the default system path
            //of your application so we are gonna be able to overwrite that database with our database.
            this.getReadableDatabase();
            importdb();
        }
    }


    private boolean DataBaseisExist() {
        SQLiteDatabase checkDB = null;
        try {
            String Path = DB_PATH + DB_NAME;
            checkDB = SQLiteDatabase.openDatabase(Path, null, SQLiteDatabase.OPEN_READONLY);
        } catch (SQLiteException e) {
            //database does't exist yet.
            //Toast.makeText(context, "Database Tidak Ada", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "Database tidak ada");
            e.printStackTrace();
        }
        if (checkDB != null) {
            checkDB.close();
        }
        if (checkDB != null) return true;
        else return false;
    }

    public void importdb() {
        try {
            InputStream fis = context.getAssets().open(DB_NAME);
            // Path to the just created empty db
            String outFileName = DB_PATH + DB_NAME;
            //Open the empty db as the output stream
            OutputStream fos = new FileOutputStream(outFileName);
            //transfer bytes from the inputfile to the outputfile
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                fos.write(buffer, 0, length);
            }
            fos.flush();
            fos.close();
            fis.close();
            //Toast.makeText(context, "Import DB OK! :)", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "Import database berhasil");

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Toast.makeText(context, "Import DB ER! :(", Toast.LENGTH_SHORT).show();
            Log.e("TAG", "Import database gagal");
        }
    }

/*

    public List<String> getAllLabels() {
        List<String> list = new ArrayList<String>();

        // Select All Query  
        String selectQuery = "SELECT  * FROM " + TABLE_ONG + " group by provinsi order by provinsi asc";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments  

        // looping through all rows and adding to list  
        if (cursor != null && cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(1));//adding 2nd column data  
            } while (cursor.moveToNext());
        }
        // closing connection  
        cursor.close();
        db.close();

        // returning lables  
        return list;
    }

    public List<String> getAllLabels2(String prov) {
        List<String> list = new ArrayList<String>();

        String selectQuery = "SELECT  * FROM " + TABLE_ONG + " where " + COL_PROV + " like '%" + prov + "%'group by kabupaten_kota order by kabupaten_kota asc";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments  

        // looping through all rows and adding to list  
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(2));//adding 2nd column data  
            } while (cursor.moveToNext());
        }
        // closing connection  
        cursor.close();
        db.close();

        // returning lables  
        return list;
    }

    public List<String> getAllLabels3(String kab) {
        List<String> list = new ArrayList<String>();

        // Select All Query  
        String selectQuery = "SELECT  * FROM " + TABLE_ONG + " where " + COL_KAB + " like '%" + kab + "%' order by kecamatan asc";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments  

        // looping through all rows and adding to list  
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(3));//adding 2nd column data  
            } while (cursor.moveToNext());
        }
        // closing connection  
        cursor.close();
        db.close();

        // returning lables  
        return list;
    }

*/

    public ArrayList<String> getAllKabNama() {
        ArrayList<String> arrayList = new ArrayList<>();
        db = DatabaseHandler.this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from domesticCities", null);
        c.moveToFirst();
        //arrayList.add(null);
        do {
            String kab = c.getString(c.getColumnIndex("kabupaten"));
            arrayList.add(kab);

        } while (c.moveToNext());

        return arrayList;
    }

    public ArrayList<String> getAllKabId() {
        ArrayList<String> arrayList = new ArrayList<>();
        db = DatabaseHandler.this.getReadableDatabase();
        Cursor c = db.rawQuery("select * from domesticCities", null);
        c.moveToFirst();
        do {
            String ids = c.getString(c.getColumnIndex("id"));
            arrayList.add(ids);
        } while (c.moveToNext());

        return arrayList;
    }

    @Override
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }
}