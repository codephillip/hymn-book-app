package com.codephillip.app.hymnbook.provider.favoritetable;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.codephillip.app.hymnbook.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code favoritetable} table.
 */
public class FavoritetableCursor extends AbstractCursor implements FavoritetableModel {
    public FavoritetableCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(FavoritetableColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code title} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getTitle() {
        String res = getStringOrNull(FavoritetableColumns.TITLE);
        return res;
    }

    /**
     * Get the {@code content} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getContent() {
        String res = getStringOrNull(FavoritetableColumns.CONTENT);
        return res;
    }

    /**
     * Get the {@code number} value.
     * Can be {@code null}.
     */
    @Nullable
    public Integer getNumber() {
        Integer res = getIntegerOrNull(FavoritetableColumns.NUMBER);
        return res;
    }

    /**
     * Get the {@code category} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getCategory() {
        String res = getStringOrNull(FavoritetableColumns.CATEGORY);
        return res;
    }

    /**
     * Get the {@code like} value.
     * Can be {@code null}.
     */
    @Nullable
    public Boolean getLike() {
        Boolean res = getBooleanOrNull(FavoritetableColumns.LIKE);
        return res;
    }
}
