package com.codephillip.app.hymnbook.services;

/**
 * Created by codephillip on 10/04/17.
 */

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;


public class MyJson {

    private static final String TAG = MyJson.class.getSimpleName();

    public static void saveData(Context context, String mJsonResponse, String fileName) {
        Log.d(TAG, "saveData: writing to storage");
        try {
            FileWriter file = new FileWriter(context.getFilesDir().getPath() + "/" + fileName + ".json");
            file.write(mJsonResponse);
            file.flush();
            file.close();
        } catch (IOException e) {
            Log.e("TAG", "Error in Writing: " + e.getLocalizedMessage());
        }
    }

    public static String getData(Context context, String fileName) {
        Log.d(TAG, "getData: fetching from storage");
        try {
            File f = new File(context.getFilesDir().getPath() + "/" + fileName + ".json");
            //check whether file exists
            FileInputStream is = new FileInputStream(f);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            return new String(buffer);
        } catch (IOException e) {
            Log.e("TAG", "Error in Reading: " + e.getLocalizedMessage());
            return null;
        }
    }
}