package io.github.huang_chenyu.thosedays;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.SortedSet;
import java.util.TreeSet;

public class MainActivity extends AppCompatActivity {
    private static final String LOG_TAG = "[Using ESA]";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        List<String> users = getLabelFiles();
        System.out.printf("=== %d\n", users.size());
        for (int i = 0; i < users.size(); i++) {
           System.out.println(users.get(i) + " !!");
        }

        getSupportFragmentManager().beginTransaction().add(R.id.menu, new MenuFragment()).add(R.id.content, new ActivityFragment()).commit();

    }

    private static final String SERVER_PREDICTIONS_FILE_SUFFIX = ".server_predictions.json";
    private static final String USER_REPORTED_LABELS_FILE_SUFFIX = ".user_reported_labels.json";
    private static final String UUID_DIR_PREFIX = "extrasensory.labels.";

    private File getUsersFilesDirectory() throws PackageManager.NameNotFoundException {
        // Locate the ESA saved files directory, and the specific minute-example's file:
        Context extraSensoryAppContext = getApplicationContext().createPackageContext("edu.ucsd.calab.extrasensory", 0);
        File esaFilesDir = extraSensoryAppContext.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS);
        if (esaFilesDir == null)
            return null;

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
            return null;
        }
        else {
            return userEsaFilesDir;
        }
    }

    private List<String> getLabelFiles() {
        try {
            File esaFilesDir = getUsersFilesDirectory();
            if (esaFilesDir == null) {
                return null;
            }
            System.out.println("file directory not NULL! " + esaFilesDir.getPath());

            String[] filenames = esaFilesDir.list();
            // List<String> data = Arrays.asList(filenames);
            // return data;
            return Arrays.asList(filenames);
        }
        catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


}
