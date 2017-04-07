package com.codephillip.app.hymnbook.provider;

import java.util.Arrays;

import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;

import com.codephillip.app.hymnbook.BuildConfig;
import com.codephillip.app.hymnbook.provider.base.BaseContentProvider;
import com.codephillip.app.hymnbook.provider.categorytable.CategorytableColumns;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableColumns;

public class HymnbookProvider extends BaseContentProvider {
    private static final String TAG = HymnbookProvider.class.getSimpleName();

    private static final boolean DEBUG = BuildConfig.DEBUG;

    private static final String TYPE_CURSOR_ITEM = "vnd.android.cursor.item/";
    private static final String TYPE_CURSOR_DIR = "vnd.android.cursor.dir/";

    public static final String AUTHORITY = "com.codephillip.app.hymnbook.provider";
    public static final String CONTENT_URI_BASE = "content://" + AUTHORITY;

    private static final int URI_TYPE_CATEGORYTABLE = 0;
    private static final int URI_TYPE_CATEGORYTABLE_ID = 1;

    private static final int URI_TYPE_HYMNTABLE = 2;
    private static final int URI_TYPE_HYMNTABLE_ID = 3;



    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, CategorytableColumns.TABLE_NAME, URI_TYPE_CATEGORYTABLE);
        URI_MATCHER.addURI(AUTHORITY, CategorytableColumns.TABLE_NAME + "/#", URI_TYPE_CATEGORYTABLE_ID);
        URI_MATCHER.addURI(AUTHORITY, HymntableColumns.TABLE_NAME, URI_TYPE_HYMNTABLE);
        URI_MATCHER.addURI(AUTHORITY, HymntableColumns.TABLE_NAME + "/#", URI_TYPE_HYMNTABLE_ID);
    }

    @Override
    protected SQLiteOpenHelper createSqLiteOpenHelper() {
        return HymnbookSQLiteOpenHelper.getInstance(getContext());
    }

    @Override
    protected boolean hasDebug() {
        return DEBUG;
    }

    @Override
    public String getType(Uri uri) {
        int match = URI_MATCHER.match(uri);
        switch (match) {
            case URI_TYPE_CATEGORYTABLE:
                return TYPE_CURSOR_DIR + CategorytableColumns.TABLE_NAME;
            case URI_TYPE_CATEGORYTABLE_ID:
                return TYPE_CURSOR_ITEM + CategorytableColumns.TABLE_NAME;

            case URI_TYPE_HYMNTABLE:
                return TYPE_CURSOR_DIR + HymntableColumns.TABLE_NAME;
            case URI_TYPE_HYMNTABLE_ID:
                return TYPE_CURSOR_ITEM + HymntableColumns.TABLE_NAME;

        }
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (DEBUG) Log.d(TAG, "insert uri=" + uri + " values=" + values);
        return super.insert(uri, values);
    }

    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {
        if (DEBUG) Log.d(TAG, "bulkInsert uri=" + uri + " values.length=" + values.length);
        return super.bulkInsert(uri, values);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "update uri=" + uri + " values=" + values + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.update(uri, values, selection, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        if (DEBUG) Log.d(TAG, "delete uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs));
        return super.delete(uri, selection, selectionArgs);
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        if (DEBUG)
            Log.d(TAG, "query uri=" + uri + " selection=" + selection + " selectionArgs=" + Arrays.toString(selectionArgs) + " sortOrder=" + sortOrder
                    + " groupBy=" + uri.getQueryParameter(QUERY_GROUP_BY) + " having=" + uri.getQueryParameter(QUERY_HAVING) + " limit=" + uri.getQueryParameter(QUERY_LIMIT));
        return super.query(uri, projection, selection, selectionArgs, sortOrder);
    }

    @Override
    protected QueryParams getQueryParams(Uri uri, String selection, String[] projection) {
        QueryParams res = new QueryParams();
        String id = null;
        int matchedId = URI_MATCHER.match(uri);
        switch (matchedId) {
            case URI_TYPE_CATEGORYTABLE:
            case URI_TYPE_CATEGORYTABLE_ID:
                res.table = CategorytableColumns.TABLE_NAME;
                res.idColumn = CategorytableColumns._ID;
                res.tablesWithJoins = CategorytableColumns.TABLE_NAME;
                res.orderBy = CategorytableColumns.DEFAULT_ORDER;
                break;

            case URI_TYPE_HYMNTABLE:
            case URI_TYPE_HYMNTABLE_ID:
                res.table = HymntableColumns.TABLE_NAME;
                res.idColumn = HymntableColumns._ID;
                res.tablesWithJoins = HymntableColumns.TABLE_NAME;
                res.orderBy = HymntableColumns.DEFAULT_ORDER;
                break;

            default:
                throw new IllegalArgumentException("The uri '" + uri + "' is not supported by this ContentProvider");
        }

        switch (matchedId) {
            case URI_TYPE_CATEGORYTABLE_ID:
            case URI_TYPE_HYMNTABLE_ID:
                id = uri.getLastPathSegment();
        }
        if (id != null) {
            if (selection != null) {
                res.selection = res.table + "." + res.idColumn + "=" + id + " and (" + selection + ")";
            } else {
                res.selection = res.table + "." + res.idColumn + "=" + id;
            }
        } else {
            res.selection = selection;
        }
        return res;
    }
}
