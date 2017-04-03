package com.codephillip.app.hymnbook.provider.favoritetable;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.codephillip.app.hymnbook.provider.base.AbstractContentValues;

/**
 * Content values wrapper for the {@code favoritetable} table.
 */
public class FavoritetableContentValues extends AbstractContentValues {
    @Override
    public Uri uri() {
        return FavoritetableColumns.CONTENT_URI;
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(ContentResolver contentResolver, @Nullable FavoritetableSelection where) {
        return contentResolver.update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    /**
     * Update row(s) using the values stored by this object and the given selection.
     *
     * @param contentResolver The content resolver to use.
     * @param where The selection to use (can be {@code null}).
     */
    public int update(Context context, @Nullable FavoritetableSelection where) {
        return context.getContentResolver().update(uri(), values(), where == null ? null : where.sel(), where == null ? null : where.args());
    }

    public FavoritetableContentValues putTitle(@Nullable String value) {
        mContentValues.put(FavoritetableColumns.TITLE, value);
        return this;
    }

    public FavoritetableContentValues putTitleNull() {
        mContentValues.putNull(FavoritetableColumns.TITLE);
        return this;
    }

    public FavoritetableContentValues putContent(@Nullable String value) {
        mContentValues.put(FavoritetableColumns.CONTENT, value);
        return this;
    }

    public FavoritetableContentValues putContentNull() {
        mContentValues.putNull(FavoritetableColumns.CONTENT);
        return this;
    }

    public FavoritetableContentValues putNumber(@Nullable Integer value) {
        mContentValues.put(FavoritetableColumns.NUMBER, value);
        return this;
    }

    public FavoritetableContentValues putNumberNull() {
        mContentValues.putNull(FavoritetableColumns.NUMBER);
        return this;
    }

    public FavoritetableContentValues putCategory(@Nullable String value) {
        mContentValues.put(FavoritetableColumns.CATEGORY, value);
        return this;
    }

    public FavoritetableContentValues putCategoryNull() {
        mContentValues.putNull(FavoritetableColumns.CATEGORY);
        return this;
    }

    public FavoritetableContentValues putLike(@Nullable Boolean value) {
        mContentValues.put(FavoritetableColumns.LIKE, value);
        return this;
    }

    public FavoritetableContentValues putLikeNull() {
        mContentValues.putNull(FavoritetableColumns.LIKE);
        return this;
    }
}
