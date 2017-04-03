package com.codephillip.app.hymnbook.provider.favoritetable;

import android.net.Uri;
import android.provider.BaseColumns;

import com.codephillip.app.hymnbook.provider.HymnbookProvider;
import com.codephillip.app.hymnbook.provider.favoritetable.FavoritetableColumns;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableColumns;

/**
 * Columns for the {@code favoritetable} table.
 */
public class FavoritetableColumns implements BaseColumns {
    public static final String TABLE_NAME = "favoritetable";
    public static final Uri CONTENT_URI = Uri.parse(HymnbookProvider.CONTENT_URI_BASE + "/" + TABLE_NAME);

    /**
     * Primary key.
     */
    public static final String _ID = BaseColumns._ID;

    public static final String TITLE = "title";

    public static final String CONTENT = "content";

    public static final String NUMBER = "number";

    public static final String CATEGORY = "category";

    public static final String LIKE = "like";


    public static final String DEFAULT_ORDER = TABLE_NAME + "." +_ID;

    // @formatter:off
    public static final String[] ALL_COLUMNS = new String[] {
            _ID,
            TITLE,
            CONTENT,
            NUMBER,
            CATEGORY,
            LIKE
    };
    // @formatter:on

    public static boolean hasColumns(String[] projection) {
        if (projection == null) return true;
        for (String c : projection) {
            if (c.equals(TITLE) || c.contains("." + TITLE)) return true;
            if (c.equals(CONTENT) || c.contains("." + CONTENT)) return true;
            if (c.equals(NUMBER) || c.contains("." + NUMBER)) return true;
            if (c.equals(CATEGORY) || c.contains("." + CATEGORY)) return true;
            if (c.equals(LIKE) || c.contains("." + LIKE)) return true;
        }
        return false;
    }

}
