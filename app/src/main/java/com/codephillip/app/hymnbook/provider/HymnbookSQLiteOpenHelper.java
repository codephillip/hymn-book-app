package com.codephillip.app.hymnbook.provider;

import android.annotation.TargetApi;
import android.content.Context;
import android.database.DatabaseErrorHandler;
import android.database.DefaultDatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import com.codephillip.app.hymnbook.BuildConfig;
import com.codephillip.app.hymnbook.provider.categorytable.CategorytableColumns;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableColumns;

public class HymnbookSQLiteOpenHelper extends SQLiteOpenHelper {
    private static final String TAG = HymnbookSQLiteOpenHelper.class.getSimpleName();

    public static final String DATABASE_FILE_NAME = "hymnbook.db";
    private static final int DATABASE_VERSION = 1;
    private static HymnbookSQLiteOpenHelper sInstance;
    private final Context mContext;
    private final HymnbookSQLiteOpenHelperCallbacks mOpenHelperCallbacks;

    // @formatter:off
    public static final String SQL_CREATE_TABLE_CATEGORYTABLE = "CREATE TABLE IF NOT EXISTS "
            + CategorytableColumns.TABLE_NAME + " ( "
            + CategorytableColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + CategorytableColumns.KEY + " INTEGER, "
            + CategorytableColumns.NAME + " TEXT "
            + " );";

    public static final String SQL_CREATE_TABLE_HYMNTABLE = "CREATE TABLE IF NOT EXISTS "
            + HymntableColumns.TABLE_NAME + " ( "
            + HymntableColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + HymntableColumns.TITLE + " TEXT, "
            + HymntableColumns.CONTENT + " TEXT, "
            + HymntableColumns.NUMBER + " INTEGER, "
            + HymntableColumns.CATEGORY + " TEXT, "
            + HymntableColumns.LIKE + " INTEGER "
            + " );";

    // @formatter:on

    public static HymnbookSQLiteOpenHelper getInstance(Context context) {
        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = newInstance(context.getApplicationContext());
        }
        return sInstance;
    }

    private static HymnbookSQLiteOpenHelper newInstance(Context context) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return newInstancePreHoneycomb(context);
        }
        return newInstancePostHoneycomb(context);
    }


    /*
     * Pre Honeycomb.
     */
    private static HymnbookSQLiteOpenHelper newInstancePreHoneycomb(Context context) {
        return new HymnbookSQLiteOpenHelper(context);
    }

    private HymnbookSQLiteOpenHelper(Context context) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION);
        mContext = context;
        mOpenHelperCallbacks = new HymnbookSQLiteOpenHelperCallbacks();
    }


    /*
     * Post Honeycomb.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private static HymnbookSQLiteOpenHelper newInstancePostHoneycomb(Context context) {
        return new HymnbookSQLiteOpenHelper(context, new DefaultDatabaseErrorHandler());
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    private HymnbookSQLiteOpenHelper(Context context, DatabaseErrorHandler errorHandler) {
        super(context, DATABASE_FILE_NAME, null, DATABASE_VERSION, errorHandler);
        mContext = context;
        mOpenHelperCallbacks = new HymnbookSQLiteOpenHelperCallbacks();
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        if (BuildConfig.DEBUG) Log.d(TAG, "onCreate");
        mOpenHelperCallbacks.onPreCreate(mContext, db);
        db.execSQL(SQL_CREATE_TABLE_CATEGORYTABLE);
        db.execSQL(SQL_CREATE_TABLE_HYMNTABLE);
        mOpenHelperCallbacks.onPostCreate(mContext, db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        mOpenHelperCallbacks.onOpen(mContext, db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        mOpenHelperCallbacks.onUpgrade(mContext, db, oldVersion, newVersion);
    }
}
