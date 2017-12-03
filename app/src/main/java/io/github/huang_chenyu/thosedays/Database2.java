package io.github.huang_chenyu.thosedays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by Chenyu on 12/2/17.
 */

public class Database2 {
    SQLiteDatabase db;
    private static final String DATABASE_NAME = "Activities";
    private static final String TABLE_COMMENT = "activityList";
    private static final String COLUMN_ID = "ID";
    private static final String COLUMN_ACT_NAME = "Name";
    private static final String COLUMN_DATE = "Date";
    private static final String COLUMN_START_TIME = "Start_Time";
    private static final String COLUMN_END_TIME = "End_Time";
    private static final String COLUMN_LAT = "Latitude";
    private static final String COLUMN_LON = "Longitude";
    private static final String COLUMN_TAG = "Tags";
    private static final String COLUMN_LOC = "Location";
    private static final String COLUMN_COMMENT = "Comment";
    private static final String COLUMN_PHOTO = "Photos";

    private static final String DATABASE_CREATE = "create table IF NOT EXISTS "
            + TABLE_COMMENT + "( "
            + COLUMN_ID + " integer primary key, "
            + COLUMN_ACT_NAME + " varchar, "
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


    void openDB(Context context){

        db = context.openOrCreateDatabase(DATABASE_NAME, MODE_PRIVATE, null);
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

        ContentValues values = new ContentValues();

        // Now the ID is not necessary
//        values.put(COLUMN_ID, "0");

        values.put(COLUMN_ACT_NAME, humanActivity.getActivityName());
        values.put(COLUMN_DATE, humanActivity.getDate());
        values.put(COLUMN_START_TIME, humanActivity.getStartTime());
        values.put(COLUMN_END_TIME, humanActivity.getEndTime());
        values.put(COLUMN_LAT, humanActivity.getLat());
        values.put(COLUMN_LON, humanActivity.getLon());


        // Tags need to be pre-processed before dumping into db.
        StringBuilder tagsStr = new StringBuilder();

        for(String tag : humanActivity.getTags()) {

            if ( tagsStr.length() != 0 ) {
                tagsStr.append(",");
            }

            tagsStr.append(tag);
        }
        values.put(COLUMN_TAG, tagsStr.toString());


        values.put(COLUMN_LOC, humanActivity.getLocation());
        values.put(COLUMN_COMMENT, humanActivity.getComments());

        // There may be multiple photos, thus, need to pre-processed
        StringBuilder photoPaths = new StringBuilder();
        for(String path : humanActivity.getPhotoPaths()) {

            if ( tagsStr.length() != 0)
                photoPaths.append(",");

            photoPaths.append(path);
        }

        values.put(COLUMN_PHOTO, photoPaths.toString());

        db.insertWithOnConflict(TABLE_COMMENT, null, values, SQLiteDatabase.CONFLICT_REPLACE);
    }

    List<HumanActivity> queryByDate(String date){

        List<HumanActivity> res = new LinkedList<>();

        String query = "SELECT * from " + TABLE_COMMENT+
                        " WHERE " + COLUMN_DATE + "= '" + date + "';";

        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){

            String actName = cursor.getString(cursor.getColumnIndex(COLUMN_ACT_NAME));
            String loc = cursor.getString(cursor.getColumnIndex(COLUMN_LOC));
            String startTime = cursor.getString(cursor.getColumnIndex(COLUMN_START_TIME));
            String endTime = cursor.getString(cursor.getColumnIndex(COLUMN_END_TIME));
            String lat = cursor.getString(cursor.getColumnIndex(COLUMN_LAT));
            String lon = cursor.getString(cursor.getColumnIndex(COLUMN_LON));
            String com = cursor.getString(cursor.getColumnIndex(COLUMN_COMMENT));
            // Retrieve tags
            Set<String> tags = new HashSet<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(COLUMN_TAG)).split("\\s*,\\s*")));

            // Retrieve photo paths
            Set<String> photoPaths = new HashSet<>(Arrays.asList(cursor.getString(cursor.getColumnIndex(COLUMN_PHOTO)).split("\\s*,\\s*")));

            // Create a HumanActivity
            HumanActivity tmp = new HumanActivity(actName, tags, date, endTime, startTime, lat, lon, loc, com, photoPaths);
            // Append to res
            res.add(tmp);

        }
        cursor.close();
        return res;
    }

    void updateComment(HumanActivity activity){
        String date = activity.getDate();
        String startTime = activity.getStartTime();
        String comment = activity.getComments();

        // Handling ' symbol
        comment = comment.replace("'", "''");

        String updateCmd = "UPDATE " + TABLE_COMMENT + " " +
                           "SET " + COLUMN_COMMENT + "='" + comment +"' " +
                            "WHERE " + COLUMN_DATE + "= '" + date + "' and " + COLUMN_START_TIME + "='" + startTime +"';";

        db.execSQL(updateCmd);

        //Codes for debugging and testing.
        String query = "SELECT * from " + TABLE_COMMENT+
                " WHERE " + COLUMN_DATE + "= '" + date + "' and " + COLUMN_START_TIME + "='" + startTime +"';";
        Cursor cursor = db.rawQuery(query, null);
        while(cursor.moveToNext()) {
            String newComment = cursor.getString(cursor.getColumnIndex(COLUMN_COMMENT));
            Log.d("Updated Comment", newComment);
        }
    }

    void closeDB(){
        db.close();
    }

    // Delete the table from database.
    void delete() {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_COMMENT);
    }

//    private List<String> searchForPotentialMatch(String searchText) {
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
