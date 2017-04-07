package com.codephillip.app.hymnbook.provider.categorytable;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.codephillip.app.hymnbook.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code categorytable} table.
 */
public class CategorytableContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return CategorytableColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable CategorytableSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable CategorytableSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public CategorytableContentValues putKey(@Nullable Long value) {
        mContentValues.put(CategorytableColumns.KEY, value);
        return this;
    }

    public CategorytableContentValues putKeyNull() {
        mContentValues.putNull(CategorytableColumns.KEY);
        return this;
    }

    public CategorytableContentValues putName(@Nullable String value) {
        mContentValues.put(CategorytableColumns.NAME, value);
        return this;
    }

    public CategorytableContentValues putNameNull() {
        mContentValues.putNull(CategorytableColumns.NAME);
        return this;
    }
}
