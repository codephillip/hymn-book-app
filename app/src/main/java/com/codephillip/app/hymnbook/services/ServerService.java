package com.codephillip.app.hymnbook.services;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.codephillip.app.hymnbook.utilities.Utils;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;


public class ServerService extends IntentService {
    private static final String TAG = ServerService.class.getSimpleName();

    public ServerService() {
        super("ServerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "onHandleIntent: started");
        try {
            MyJson.saveData(this, connectToServer(Utils.BASE_URL + "/api/v1/hymns"));
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            Log.d(TAG, "onHandleIntent: getData#");
            String data = MyJson.getData(this);
            Log.d(TAG, "onHandleIntent: DATA# " + data);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private String connectToServer(String urlConnection) throws Exception{
        Request request = new Request.Builder().url(urlConnection).build();
        OkHttpClient client = new OkHttpClient();
        Response response = client.newCall(request).execute();

        String jsonData = response.body().string();
        Log.d("JSON STRING_DATA", jsonData);
        return jsonData;
    }
}
