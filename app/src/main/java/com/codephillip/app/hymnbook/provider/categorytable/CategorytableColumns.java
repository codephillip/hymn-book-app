package com.codephillip.app.hymnbook.provider.categorytable;

import android.net.Uri;
import android.provider.BaseColumns;

import com.codephillip.app.hymnbook.provider.HymnbookProvider;
import com.codephillip.app.hymnbook.provider.categorytable.CategorytableColumns;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableColumns;

/**
 * Columns for the {@code categorytable} table.
 */
public class CategorytableColumns implements BaseColumns {
    public static final String TABLE_NAME = "categorytable";
    public static final Uri CONTENT_URI = Uri.parse(HymnbookProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String KEY = "key";

    public static final String NAME = "name";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            KEY,
            NAME
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(KEY) || c.contains("." + KEY)) return true;
            if (c.equals(NAME) || c.contains("." + NAME)) return true;
        }
        return false;
    }

}
