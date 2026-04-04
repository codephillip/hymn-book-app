package com.codephillip.app.hymnbook;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.codephillip.app.hymnbook.databinding.ActivityMainBottomBinding;
import com.codephillip.app.hymnbook.provider.categorytable.CategorytableColumns;
import com.codephillip.app.hymnbook.provider.categorytable.CategorytableContentValues;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableColumns;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableContentValues;
import com.codephillip.app.hymnbook.services.MyJson;
import com.codephillip.app.hymnbook.services.ReminderReceiver;
import com.codephillip.app.hymnbook.utilities.Utils;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

public class MainActivityBottom extends AppCompatActivity {

    private ActivityMainBottomBinding binding;
    private static final String TAG = MainActivityBottom.class.getSimpleName();

    private final ActivityResultLauncher<String> requestPermissionLauncher =
            registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted -> {
                if (isGranted) {
                    ReminderReceiver.scheduleDailyReminder(this);
                } else {
                    Toast.makeText(this, "Notification permission denied. Reminders will not be shown.", Toast.LENGTH_SHORT).show();
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBottomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        if (isFirstLaunch() || isSynchronized())
            connectToStorage();

        activateFont();

        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_original, R.id.favourites_dashboard, R.id.navigation_home)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main_bottom);
        NavigationUI.setupWithNavController(binding.navView, navController);

        askNotificationPermission();

        getOnBackPressedDispatcher().addCallback(this,
                new OnBackPressedCallback(true) {
                    @Override
                    public void handleOnBackPressed() {
                        finishAffinity(); // exits the app
                    }
                });
    }

    private void askNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) ==
                    PackageManager.PERMISSION_GRANTED) {
                ReminderReceiver.scheduleDailyReminder(this);
            } else {
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS);
            }
        } else {
            ReminderReceiver.scheduleDailyReminder(this);
        }
    }

    private void activateFont() {
        try {
            Utils.typeface = Typeface.createFromAsset(getAssets(), "fonts/" + "Raleway-Bold.ttf");
        } catch (Exception e) {
            e.printStackTrace();
            Utils.typeface = Typeface.DEFAULT;
        }
    }

    private void connectToStorage() {
        Log.d(TAG, "onCreate: connectToStorage started");
        deleteTables();

        boolean hasSynchronized = isSynchronized();
        if (hasSynchronized) {
            try {
                getSynchronizedData();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to update hymns, Check Internet connection", Toast.LENGTH_LONG).show();
                fetchLocalData();
            }
        } else {
            fetchLocalData();
        }

        saveFirstLaunch(false);
        saveHasSynchronized(false);
    }

    private void fetchLocalData() {
        getCategorysFromJson();
        getHymnsFromJson();
    }

    private void getSynchronizedData() {
        Log.d(TAG, "onHandleIntent: getData#");
        try {
            extractHymnJsonData(MyJson.getData(this, HymntableColumns.TABLE_NAME));
            extractCategoryJsonData(MyJson.getData(this, CategorytableColumns.TABLE_NAME));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void deleteTables() {
        long deleted;
        deleted = getContentResolver().delete(CategorytableColumns.CONTENT_URI, null, null);
        Log.d(TAG, "deleteCategoryTable: " + deleted);
        deleted = getContentResolver().delete(HymntableColumns.CONTENT_URI, null, null);
        Log.d(TAG, "deleteTables: " + deleted);
    }

    private void getHymnsFromJson() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.hymns);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            extractHymnJsonData(new String(b));
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void extractHymnJsonData(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("hymns");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject innerObject = jsonArray.getJSONObject(i);
            storeInHymnTable(Integer.parseInt(innerObject.getString("number")), innerObject.getString("title"), innerObject.getString("content"), innerObject.getJSONObject("category").getString("name"));
        }
    }

    private void storeInHymnTable(int number, String title, String content, String category) {
        HymntableContentValues values = new HymntableContentValues();
        values.putNumber(number);
        values.putTitle(title);
        values.putContent(content);
        values.putCategory(category);
        values.putLike(false);
        values.insert(getContentResolver());
    }

    private void getCategorysFromJson() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.categorys);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            extractCategoryJsonData(new String(b));
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e) {
            Log.e(TAG, e.toString());
        }
    }

    private void extractCategoryJsonData(String jsonString) throws JSONException {
        JSONObject jsonObject = new JSONObject(jsonString);
        JSONArray jsonArray = jsonObject.getJSONArray("categorys");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject innerObject = jsonArray.getJSONObject(i);
            storeInCategoryTable(Long.parseLong(innerObject.getString("id")), innerObject.getString("name"));
        }
    }

    private void storeInCategoryTable(long id, String name) {
        CategorytableContentValues values = new CategorytableContentValues();
        values.putKey(id);
        values.putName(name);
        values.insert(getContentResolver());
    }

    private void switchView() {
        Log.d(TAG, "switchView: " + hasChangedView());
        saveChangedView(!hasChangedView());
    }

    private void saveChangedView(boolean hasChangedView) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Utils.CHANGE_VIEW, hasChangedView);
        editor.apply();
    }

    private boolean hasChangedView() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean(Utils.CHANGE_VIEW, true);
    }

    private void saveFirstLaunch(boolean hasChangedView) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Utils.FIRST_LAUNCH, hasChangedView);
        editor.apply();
    }

    private boolean isFirstLaunch() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean(Utils.FIRST_LAUNCH, true);
    }

    private void saveHasSynchronized(boolean hasSynchronized) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Utils.HAS_SYNCHRONIZED, hasSynchronized);
        editor.apply();
    }

    private boolean isSynchronized() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        return prefs.getBoolean(Utils.HAS_SYNCHRONIZED, false);
    }
}
