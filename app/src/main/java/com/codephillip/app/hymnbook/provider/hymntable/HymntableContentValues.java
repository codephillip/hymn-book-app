package com.codephillip.app.hymnbook.provider.hymntable;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.codephillip.app.hymnbook.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code hymntable} table.
 */
public class HymntableContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return HymntableColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable HymntableSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable HymntableSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public HymntableContentValues putTitle(@Nullable String value) {
        mContentValues.put(HymntableColumns.TITLE, value);
        return this;
    }

    public HymntableContentValues putTitleNull() {
        mContentValues.putNull(HymntableColumns.TITLE);
        return this;
    }

    public HymntableContentValues putContent(@Nullable String value) {
        mContentValues.put(HymntableColumns.CONTENT, value);
        return this;
    }

    public HymntableContentValues putContentNull() {
        mContentValues.putNull(HymntableColumns.CONTENT);
        return this;
    }

    public HymntableContentValues putNumber(@Nullable Integer value) {
        mContentValues.put(HymntableColumns.NUMBER, value);
        return this;
    }

    public HymntableContentValues putNumberNull() {
        mContentValues.putNull(HymntableColumns.NUMBER);
        return this;
    }

    public HymntableContentValues putCategory(@Nullable String value) {
        mContentValues.put(HymntableColumns.CATEGORY, value);
        return this;
    }

    public HymntableContentValues putCategoryNull() {
        mContentValues.putNull(HymntableColumns.CATEGORY);
        return this;
    }

    public HymntableContentValues putLike(@Nullable Boolean value) {
        mContentValues.put(HymntableColumns.LIKE, value);
        return this;
    }

    public HymntableContentValues putLikeNull() {
        mContentValues.putNull(HymntableColumns.LIKE);
        return this;
    }
}
