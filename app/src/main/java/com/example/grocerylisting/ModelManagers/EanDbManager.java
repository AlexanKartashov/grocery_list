package com.example.grocerylisting.ModelManagers;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.grocerylisting.BuildConfig;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EanDbManager extends  SQLiteOpenHelper {

    private Context mycontext;
    private static String DB_NAME = "ean_db.db";
    private static String DB_PATH ="/data/data/"+ BuildConfig.APPLICATION_ID+"/databases/";

    public EanDbManager(Context context) {
        super(context,DB_NAME,null,1);
        this.mycontext=context;
        boolean dbexist = checkdatabase();
        if (!(dbexist)) {
            System.out.println("Database doesn't exist");
            createdatabase();
        }
    }

    public String getTitleByEan(String ean) {
        String result = "";
        String query = "SELECT * FROM ean";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String eanInTable = cursor.getString(0);
                if (eanInTable.equals(ean)) {
                    result = cursor.getString(1);
                    break;
                }
            } while (cursor.moveToNext());
        }
        else {
            Log.println(Log.DEBUG,"DEBUG", "No data in db");
        }

        cursor.close();
        return result;
    }

    public void createdatabase() {
        boolean dbexist = checkdatabase();
        if(dbexist) {
            System.out.println(" Database exists.");
        } else {
            this.getReadableDatabase();
            try {
                copydatabase();
            } catch(IOException e) {
                throw new Error("Error copying database");
            }
        }
    }

    private boolean checkdatabase() {

        boolean checkdb = false;
        try {
            String myPath = DB_PATH + DB_NAME;
            File dbfile = new File(myPath);
            checkdb = dbfile.exists();
        } catch(SQLiteException e) {
            System.out.println("Database doesn't exist");
        }
        return checkdb;
    }

    private void copydatabase() throws IOException {
        //Open your local db as the input stream
        InputStream myinput = mycontext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outfilename = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myoutput = new FileOutputStream(outfilename);

        // transfer byte to inputfile to outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myinput.read(buffer))>0) {
            myoutput.write(buffer,0,length);
        }

        //Close the streams
        myoutput.flush();
        myoutput.close();
        myinput.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
