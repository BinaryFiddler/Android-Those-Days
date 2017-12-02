package io.github.huang_chenyu.thosedays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dukel on 11/18/2017.
 */

public class Database extends SQLiteOpenHelper {

    public static final String DB_NAME = "Activity.db";
    public static final String TABLE_NAME = "activity_log";
    public static final String COL1_NAME = "time";
    public static final String COL2_NAME = "activity";
    public static final String COL3_NAME = "location";

    public Database(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TABLE_NAME
                + " ("
                + COL1_NAME + " TEXT,"
                + COL2_NAME + " TEXT,"
                + COL3_NAME + " TEXT)");
        System.out.println("Database created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("drop table if exists " + TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public boolean insert(String time, String activity, String location) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contents = new ContentValues();
        contents.put(COL1_NAME, time);
        contents.put(COL2_NAME, activity);
        contents.put(COL3_NAME, location);
        if (db.insert(TABLE_NAME, null, contents) == -1) {
            return false;
        }
        else {
            return true;
        }
    }

    /*
    Usage:
        instantiate Cursor object
        res.moveToNext() to go to next entry
        res.getString(0 to 2) to get entry
     */
    public Cursor retrieveDate(String time) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + TABLE_NAME + " where time = '" + time + "'", null);
        return res;
    }


}