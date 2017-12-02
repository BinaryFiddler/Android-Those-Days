package io.github.huang_chenyu.thosedays;

import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Chenyu on 12/2/17.
 */

public class Database2 {
    SQLiteDatabase db;
    public static final String TABLE_COMMENT = "activityList";
    public static final String COLUMN_ID = "ID";
    public static final String COLUMN_DATE = "Date";
    public static final String COLUMN_START_TIME = "Start Time";
    public static final String COLUMN_END_TIME = "End Time";
    public static final String COLUMN_LAT = "Latitude";
    public static final String COLUMN_LON = "Longtitude";
    public static final String COLUMN_TAG = "Tags";
    public static final String COLUMN_LOC = "Location";
    public static final String COLUMN_COMMENT = "Comment";
    public static final String COLUMN_PHOTO = "Photos";

    private static final String DATABASE_CREATE = "create table IF NOT EXISTS "
            + TABLE_COMMENT + "( "
            + COLUMN_ID + " integer primary key, "
            + COLUMN_DATE + " date, "
            + COLUMN_START_TIME + " varchar, "
            + COLUMN_END_TIME + " varchar, "
            + COLUMN_LAT + " varchar, "
            + COLUMN_LON + " varchar, "
            + COLUMN_TAG + " varchar, "
            + COLUMN_LOC + " varchar, "
            + COLUMN_COMMENT + " varchar, "
            + COLUMN_PHOTO + " varchar);";

//    Map<String, String> pickWords(){
//        Cursor cursor = db.rawQuery("SELECT * FROM wordlist ORDER BY RANDOM() LIMIT 5;", null);
//        cursor.moveToFirst();
//        Map<String, String> map = new HashMap<>();
//        while (cursor.moveToNext()){
//            String word = cursor.getString(cursor.getColumnIndex(COLUMN_WORD));
//            String def = cursor.getString(cursor.getColumnIndex(COLUMN_DEFINITION));
//            map.put(word, def);
//        }
//        return map;
//    }

//    void updateAttempts(String word, Boolean isRightAnswer){
//        Cursor cursor = db.rawQuery("SELECT * FROM wordlist WHERE " + COLUMN_WORD + " = '" + word + "'", null);
//        cursor.moveToPosition(0);
//        int updatedAttempts = cursor.getInt(cursor.getColumnIndex(COLUMN_ATTEMPT)) + 1;
//        int updatedWrongAttempts = isRightAnswer ? cursor.getInt(cursor.getColumnIndex(COLUMN_WRONG_ATTMPT)) : cursor.getInt(cursor.getColumnIndex(COLUMN_WRONG_ATTMPT)) + 1;
//        cursor.close();
//        ContentValues values = new ContentValues();
//        values.put(COLUMN_ATTEMPT, updatedAttempts);
//        values.put(COLUMN_WRONG_ATTMPT, updatedWrongAttempts);
//        db.update(TABLE_COMMENT, values, COLUMN_WORD +"='" + word + "'", null);
//    }


    void openDB(Activity activity){

        db = activity.openOrCreateDatabase("Activities", MODE_PRIVATE, null);
//      check if table exists
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '"+TABLE_COMMENT+"'", null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return;
            }
            cursor.close();
        }
//      if table not exist, read the data from static files
        db.execSQL("DROP TABLE IF EXISTS activityList;");
        db.execSQL(DATABASE_CREATE);
    }

    //todo implement
    void addEntries(HumanActivity humanActivity){

    }

    //todo implement
    List<HumanActivity> queryByDate(String date){
//        List<HumanActivity> activities = new LinkedList<>();

        List<HumanActivity> activities = HumanActivity.createActivitiesList(10);

        return activities;
    }

    //todo implement
    void updateComment(HumanActivity activity){

    }

    void closeDB(){
        db.close();
    }

//    public List<String> searchForPotentialMatch(String searchText) {
//        List<String> res = new ArrayList<>();
//        if (searchText.length() == 0)
//            return res;
//        Cursor cursor = db.rawQuery("SELECT " + COLUMN_WORD + ", " + COLUMN_DEFINITION + " FROM " + TABLE_COMMENT + " WHERE " + COLUMN_WORD + " LIKE '%" + searchText +"%';", null);
//        while(cursor.moveToNext()){
//            String word = cursor.getString(cursor.getColumnIndex(COLUMN_WORD));
//            String def = cursor.getString(cursor.getColumnIndex(COLUMN_DEFINITION));
//            res.add(word + ": " + def);
//        }
//        cursor.close();
//        return res;
//    }
}
