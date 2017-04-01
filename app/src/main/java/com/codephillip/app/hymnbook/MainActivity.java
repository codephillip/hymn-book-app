package com.codephillip.app.hymnbook;

import android.content.Context;
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
    String[] screenNames = {"title1", "title2", "title3"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        HymnDatabase.getInstance();
        Log.d(TAG, "onCreate: " + hasChangedView(this));

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Log.d(TAG, "onCreate: connectToStorage");
        connectToStorage();

        //populate the first default fragment
        Fragment fragment = hasChangedView(this) ? new AllSongsFragment() : new AllSongsGridFragment();
        getSupportActionBar().setTitle(screenNames[0]);
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame,fragment);
        fragmentTransaction.commit();
    }

    private void connectToStorage() {
        Log.d(TAG, "onCreate: connectToStorage started");
        try {
            InputStream inputStream = getResources().openRawResource(R.raw.data);
            byte[] b = new byte[inputStream.available()];
            inputStream.read(b);
            JSONObject jsonObject = new JSONObject(new String(b));
            JSONArray jsonArray = jsonObject.getJSONArray("data");

            ArrayList<Hymn> hymnList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject innerObject = jsonArray.getJSONObject(i);
                hymnList.add(new Hymn(Integer.parseInt(innerObject.getString("number")), innerObject.getString("title"), innerObject.getString("content"), innerObject.getJSONObject("category").getString("name")));
            }
            HymnDatabase.hymns.setHymnArrayList(hymnList);
        } catch (JSONException e) {
            Log.e(TAG, e.toString());
        } catch (IOException e){
            Log.e(TAG, e.toString());
        }
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
            Fragment fragment = hasChangedView(this) ? new AllSongsFragment() : new AllSongsGridFragment();
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

        if (id == R.id.nav_camera) {
            fragment = hasChangedView(this) ? new AllSongsFragment() : new AllSongsGridFragment();
            getSupportActionBar().setTitle(screenNames[0]);
        } else if (id == R.id.nav_gallery) {
            fragment = new SongFragment();
            getSupportActionBar().setTitle(screenNames[0]);
        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

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
        Log.d(TAG, "switchView: " + hasChangedView(this));
        saveChangedView(this, !hasChangedView(this));
    }

    private void saveChangedView(Context context, boolean hasChangedView) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(Utils.CHANGE_VIEW, hasChangedView);
        editor.apply();
    }

    private boolean hasChangedView(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getBoolean(Utils.CHANGE_VIEW, false);
    }
}
