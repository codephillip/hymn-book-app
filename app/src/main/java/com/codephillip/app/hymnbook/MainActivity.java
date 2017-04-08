package com.codephillip.app.hymnbook;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.codephillip.app.hymnbook.provider.categorytable.CategorytableColumns;
import com.codephillip.app.hymnbook.provider.categorytable.CategorytableContentValues;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableColumns;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableContentValues;
import com.codephillip.app.hymnbook.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;

import static com.codephillip.app.hymnbook.utilities.Utils.screenNames;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        activateTheme();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Utils.getInstance();
        Log.d(TAG, "onCreate: " + hasChangedView());

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        if (isFirstLaunch())
            connectToStorage();

        activateFont();

        //populate the first default fragment
        Fragment fragment = AllSongsFragment.newInstance(false);
        getSupportActionBar().setTitle(screenNames[0]);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }

    private void activateTheme() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean useDarkTheme = prefs.getBoolean("app_theme", false);
        Log.d(TAG, "activateTheme: " + useDarkTheme);
        if(useDarkTheme) {
            setTheme(R.style.AppTheme_Dark_NoActionBar);
        }
    }

    private void activateFont() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String fontName = null;
        try {
            fontName = prefs.getString("font_list", "Default");
            Log.d(TAG, "onCreate: PREF " + fontName);
            if (fontName.equals("Default"))
                Utils.typeface = Typeface.DEFAULT;
            else
                Utils.typeface = Typeface.createFromAsset(getAssets(), "fonts/" + fontName);
        } catch (Exception e) {
            e.printStackTrace();
            Utils.typeface = Typeface.DEFAULT;
        }
    }

    private void connectToStorage() {
        Log.d(TAG, "onCreate: connectToStorage started");
        deleteHymnTable();
        saveFirstLaunch(false);
        getHymnsFromJson();
        getCategorysFromJson();
    }

    private void deleteHymnTable() {
        long deleted;
        deleted = getContentResolver().delete(CategorytableColumns.CONTENT_URI, null, null);
        Log.d(TAG, "deleteCategoryTable: " + deleted);
        deleted = getContentResolver().delete(HymntableColumns.CONTENT_URI, null, null);
        Log.d(TAG, "deleteHymnTable: " + deleted);
    }

    private void getHymnsFromJson() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.hymns);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            JSONObject jsonObject = new JSONObject(new String(b));
            JSONArray jsonArray = jsonObject.getJSONArray("hymns");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject innerObject = jsonArray.getJSONObject(i);
                storeInHymnTable(Integer.parseInt(innerObject.getString("number")), innerObject.getString("title"), innerObject.getString("content"), innerObject.getJSONObject("category").getString("name"), false);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e){
            Log.e(TAG, e.toString());
        }
    }

    private void storeInHymnTable(int number, String title, String content, String category, boolean like) {
        HymntableContentValues values = new HymntableContentValues();
        values.putNumber(number);
        values.putTitle(title);
        values.putContent(content);
        values.putCategory(category);
        values.putLike(like);
        values.insert(getContentResolver());
    }

    private void getCategorysFromJson() {
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.categorys);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            JSONObject jsonObject = new JSONObject(new String(b));
            JSONArray jsonArray = jsonObject.getJSONArray("categorys");

            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject innerObject = jsonArray.getJSONObject(i);
                storeInCategoryTable(Long.parseLong(innerObject.getString("id")), innerObject.getString("name"));
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e){
            Log.e(TAG, e.toString());
        }
    }

    private void storeInCategoryTable(long id, String name) {
        CategorytableContentValues values = new CategorytableContentValues();
        values.putKey(id);
        values.putName(name);
        values.insert(getContentResolver());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.change_view) {
            Log.d(TAG, "onOptionsItemSelected: changing view#");
            switchView();
            Fragment fragment = AllSongsFragment.newInstance(false);
            getSupportActionBar().setTitle(screenNames[0]);
            android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, fragment);
            fragmentTransaction.commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
        Log.d("Navigation bar", "onNavigationItemSelected: " + id);
        Fragment fragment = null;

        if (id == R.id.all_songs) {
            Utils.clickedFavorite = false;
            fragment = AllSongsFragment.newInstance(false);
            getSupportActionBar().setTitle(screenNames[0]);
        } else if (id == R.id.category) {
            Utils.clickedFavorite = false;
            fragment = new CategoryFragment();
            getSupportActionBar().setTitle(screenNames[1]);
        } else if (id == R.id.favorite) {
            Utils.clickedFavorite = true;
            fragment = AllSongsFragment.newInstance(true);
            getSupportActionBar().setTitle(screenNames[2]);
        } else if (id == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
            return true;
        } else if (id == R.id.about) {

        } else {
            return true;
        }

        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
}
