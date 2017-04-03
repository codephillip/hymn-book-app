package com.codephillip.app.hymnbook;

import android.content.SharedPreferences;
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

import com.codephillip.app.hymnbook.models.Hymn;
import com.codephillip.app.hymnbook.models.HymnDatabase;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableColumns;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableContentValues;
import com.codephillip.app.hymnbook.utilities.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    //    String[] screenNames = {"Nyinba Zona", "Category", "Ezisinga"};
    String[] screenNames = {"All Songs", "Category", "Favorite", "About"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        HymnDatabase.getInstance();
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

        //populate the first default fragment
        Fragment fragment = hasChangedView() ? AllSongsFragment.newInstance(false) : new AllSongsGridFragment();
        getSupportActionBar().setTitle(screenNames[0]);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }

    private void connectToStorage() {
        Log.d(TAG, "onCreate: connectToStorage started");
        deleteHymnTable();
        saveFirstLaunch(false);
        //todo remove on release
//        getContentResolver().delete(FavoritetableColumns.CONTENT_URI, null, null);

        try {
            InputStream inputStream = getResources().openRawResource(R.raw.data);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            JSONObject jsonObject = new JSONObject(new String(b));
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            ArrayList<Hymn> hymnList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject innerObject = jsonArray.getJSONObject(i);
//                hymnList.add(new Hymn(Integer.parseInt(innerObject.getString("number")), innerObject.getString("title"), innerObject.getString("content"), innerObject.getJSONObject("category").getString("name")));
                storeInHymnTable(Integer.parseInt(innerObject.getString("number")), innerObject.getString("title"), innerObject.getString("content"), innerObject.getJSONObject("category").getString("name"), false);
            }
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e){
            Log.e(TAG, e.toString());
        }
    }

    private void deleteHymnTable() {
        long deleted = getContentResolver().delete(HymntableColumns.CONTENT_URI, null, null);
        Log.d(TAG, "deleteHymnTable: " + deleted);
    }

    private void storeInHymnTable(int number, String title, String content, String category, boolean like) {
        HymntableContentValues hymn = new HymntableContentValues();
        hymn.putNumber(number);
        hymn.putTitle(title);
        hymn.putContent(content);
        hymn.putCategory(category);
        hymn.putLike(like);
        hymn.insert(getContentResolver());
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
        Log.d(TAG, "onCreateOptionsMenu: ");
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
            Fragment fragment = hasChangedView() ? AllSongsFragment.newInstance(false) : new AllSongsGridFragment();
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
            fragment = hasChangedView() ?  AllSongsFragment.newInstance(false) : new AllSongsGridFragment();
            getSupportActionBar().setTitle(screenNames[0]);
        } else if (id == R.id.category) {
            fragment = new SongFragment();
            getSupportActionBar().setTitle(screenNames[1]);
        } else if (id == R.id.favorite) {
            fragment = hasChangedView() ?  AllSongsFragment.newInstance(true) : new AllSongsGridFragment();
            getSupportActionBar().setTitle(screenNames[2]);
        }
        else if (id == R.id.about) {

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
        return prefs.getBoolean(Utils.CHANGE_VIEW, false);
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
