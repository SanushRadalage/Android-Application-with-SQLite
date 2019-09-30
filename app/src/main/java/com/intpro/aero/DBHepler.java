package com.intpro.aero;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.FileInputStream;


/**
 * DBHelper is the class that enable all the data manipulations in the app
 *
 * Author : Sarani_Hansamali
 */

class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "Alpha";
    public static final String TABLE_NAME = "User";
    public static final String INDEX = "IndexNo";
    public static final String NAME = "Name";
    public static final String EMAIL = "Email";
    public static final String PHONE = "Phone";
    public static final String GPA = "Gpa";
    public static final String PASSWORD = "Password";
    public static final String PROFILE_IMAGE = "ProfilePicture";
    public static final String COVER_IMAGE = "coverPicture";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE User(IndexNo INTEGER PRIMARY KEY, Name TEXT, Email TEXT, Phone TEXT,Gpa TEXT, Password TEXT, ProfilePicture blob, coverPicture blob)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL(" DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public long addUser(String index, String name, String email, String phone, String Gpa, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(INDEX, index);
        contentValues.put(NAME, name);
        contentValues.put(EMAIL, email);
        contentValues.put(PHONE, phone);
        contentValues.put(GPA, Gpa);
        contentValues.put(PASSWORD, password);

        long res = db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return res;
    }

    public boolean checkUser(String index, String password) {
        String[] colmns = {INDEX};
        SQLiteDatabase db = getReadableDatabase();
        String select = INDEX + "=?" + " and " + PASSWORD + "=?";
        String[] selectArgs = {index, password};
        Cursor cursor = db.query(TABLE_NAME, colmns, select, selectArgs, null, null, null);
        int count = cursor.getCount();
        cursor.close();

        if (count > 0)
            return true;
        else
            return false;
    }

    public String[] readUser(Context c, String index) {
        String[] colmns = {INDEX, NAME, EMAIL, PHONE, GPA};
        SQLiteDatabase db = getReadableDatabase();
        String select = INDEX + "=?";
        String[] selectArgs = {index};
        Cursor cursor = db.query(TABLE_NAME, colmns
                , select
                , selectArgs, null, null, null);

        String[] s = new String[6];
        int count = cursor.getCount();
        while (cursor.moveToNext()) {
            int i;

            i = cursor.getColumnIndexOrThrow(INDEX);
            s[i] = cursor.getString(i);

            i = cursor.getColumnIndexOrThrow(NAME);
            s[i] = cursor.getString(i);

            i = cursor.getColumnIndexOrThrow(EMAIL);
            s[i] = cursor.getString(i);

            i = cursor.getColumnIndexOrThrow(PHONE);
            s[i] = cursor.getString(i);

            i = cursor.getColumnIndexOrThrow(GPA);
            s[i] = cursor.getString(i);

        }

        cursor.close();

        if (count > 0)
            return s;
        else
            return null;
    }

    public Boolean addProfileImages(Context context, String x, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            FileInputStream fs = new FileInputStream(x);
            byte[] imgByte = new byte[fs.available()];
            fs.read(imgByte);

            ContentValues args = new ContentValues();
            args.put(PROFILE_IMAGE, imgByte);
            String[] selectArgs = {id};
            db.update(TABLE_NAME, args, INDEX + "=?", selectArgs);
            fs.close();
            return true;
        } catch (Exception e) {
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public Bitmap getProfileImage(Context context, String indNomber) {

        String[] colmns = {INDEX, PROFILE_IMAGE};
        SQLiteDatabase db = getReadableDatabase();
        String select = INDEX + "=?";
        String[] selectArgs = {indNomber};
        Cursor cursor = db.query(TABLE_NAME, colmns
                , select
                , selectArgs, null, null, null);

        int count = cursor.getCount();
        while (cursor.moveToNext()) {
            try {
                if (cursor.moveToFirst()) {
                    int k = cursor.getColumnIndexOrThrow(PROFILE_IMAGE);
                    byte[] imgByte = cursor.getBlob(1);
                    cursor.close();
                    return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            } catch (Exception i) {
                //Toast.makeText(context, i.getMessage(), Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        return null;
    }

    public Boolean addCoverImages(Context context, String x, String id) {
        SQLiteDatabase db = this.getWritableDatabase();

        try {
            FileInputStream fs = new FileInputStream(x);
            byte[] imgByte = new byte[fs.available()];
            fs.read(imgByte);

            ContentValues args = new ContentValues();
            args.put(COVER_IMAGE, imgByte);
            String[] selectArgs = {id};
            db.update(TABLE_NAME, args, INDEX + "=?", selectArgs);
            fs.close();
            return true;
        } catch (Exception e) {
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_LONG).show();
            return false;
        }
    }

    public Bitmap getCoverImage(Context context, String indNmber) {

        String[] colmns = {INDEX, COVER_IMAGE};
        SQLiteDatabase db = getReadableDatabase();
        String select = INDEX + "=?";
        String[] selectArgs = {indNmber};
        Cursor cursor = db.query(TABLE_NAME, colmns
                , select
                , selectArgs, null, null, null);

        int count = cursor.getCount();
        while (cursor.moveToNext()) {
            try {
                if (cursor.moveToFirst()) {
                    int k = cursor.getColumnIndexOrThrow(COVER_IMAGE);
                    byte[] imgByte = cursor.getBlob(1);
                    cursor.close();
                    return BitmapFactory.decodeByteArray(imgByte, 0, imgByte.length);
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            } catch (Exception i) {
                //Toast.makeText(context, i.getMessage(), Toast.LENGTH_SHORT).show();
                return null;
            }
        }

        return null;
    }

    public void updateUser(Context context, String index, String name, String email, String phone, String Gpa) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        String select = INDEX + "=?";
        String[] selectArgs = {index};
        contentValues.put(INDEX, index);
        contentValues.put(NAME, name);
        contentValues.put(EMAIL, email);
        contentValues.put(PHONE, phone);
        contentValues.put(GPA, Gpa);

        db.update(TABLE_NAME, contentValues, INDEX + "=?", selectArgs);
        Toast.makeText(context, "Profile updated!", Toast.LENGTH_SHORT).show();

    }
}
