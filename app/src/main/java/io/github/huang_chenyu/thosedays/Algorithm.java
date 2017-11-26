package io.github.huang_chenyu.thosedays;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import io.github.huang_chenyu.thosedays.MainActivity;

/**
 * Created by Chiao on 2017/11/25.
 */

public class Algorithm {
    private static final String LOG_TAG = "[Algorithm]";

    private static final String SERVER_PREDICTIONS_FILE_SUFFIX = ".server_predictions.json";
    private static final String USER_REPORTED_LABELS_FILE_SUFFIX = ".user_reported_labels.json";
    private static final String UUID_DIR_PREFIX = "extrasensory.labels.";
    private static final String EXTRASENSORY_PKG_NAME = "edu.ucsd.calab.extrasensory";

    public static void process(Context context) {
        try {
            File esaFilesDir = getUsersFilesDirectory(context);
            List<String> files = getLabelFiles(esaFilesDir);
        }
        catch (Exception e) {
            e.printStackTrace();
            return;
        }
    }

    private static List<String> getLabelFiles(File filesDir) throws IOException, JSONException{
        String[] filenames = filesDir.list();
        for (String filename : filenames) {
            File file = new File(filesDir, filename);

            StringBuilder text = new StringBuilder();
            BufferedReader br = new BufferedReader(new FileReader(file));
            text.append(br.readLine());
            JSONObject jsonObject = new JSONObject(text.toString());
            Log.d(LOG_TAG, jsonObject.get("location_lat_long").toString());
        }

        return Arrays.asList(filenames);
    }

    private static File getUsersFilesDirectory(Context context) throws PackageManager.NameNotFoundException {
        // Locate the ESA saved files directory, and the specific minute-example's file:
        Context extraSensoryAppContext = context.createPackageContext(EXTRASENSORY_PKG_NAME, 0);
        File esaFilesDir = extraSensoryAppContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (esaFilesDir == null) {
            Log.e(LOG_TAG, "Cannot find ExtraSensory directory.");
            return null;
        }

        // String[] filenames = esaFilesDir.list();
        String[] filenames = esaFilesDir.list(new FilenameFilter() {
            @Override
            public boolean accept(File file, String s) {
                return s.startsWith(UUID_DIR_PREFIX);
            }
        });

        // Assume there's only one user using this phone, and take first user's data
        File userEsaFilesDir = new File(extraSensoryAppContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), filenames[0]);
        if (!userEsaFilesDir.exists()) {
            Log.e(LOG_TAG, "Cannot find ExtraSensory user data.");
            return null;
        }
        else {
            Log.d(LOG_TAG, "ExtraSensory directory exists! " + esaFilesDir.getPath());
            return userEsaFilesDir;
        }
    }

//      There should be a attribute for this app recording the timestamp of the file it processed last time

    //      Maybe need to sort or not -> check which files are not processed yet -> Dump the unprocessed ones into "dataProcess".
//      -> Dumping the processed data into database may happen within the function '"ataProcess".
//        if (blah blah) {
//      }

//        Read data from database and render the screen of the app.


    private void readESALabelsFileForMinute(String uuidPrefix, String fileName, boolean serverOfUser) {

    }


    private void dataProcess(List<String> users) {
        Log.d("MKTEST", "Data Process");
        Log.d("MKTEST", String.valueOf(users.size()));
        for ( int i = 0; i < users.size(); i++ ) {

            Log.d("MKTEST", users.get(i).toString());

//            retrieve the activity, location with the highest probability and the longitude and latitude ( should be a function).

//            TO-DO: Implement the que
//            Push into the que
//            Decide the most possible activity and location at this moment
        }
    }

}
