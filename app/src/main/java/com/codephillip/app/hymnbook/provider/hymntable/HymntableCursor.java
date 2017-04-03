package com.codephillip.app.hymnbook.provider.hymntable;

import java.util.Date;

import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.codephillip.app.hymnbook.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code hymntable} table.
 */
public class HymntableCursor extends AbstractCursor implements HymntableModel {
    public HymntableCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(HymntableColumns._ID);
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
        String res = getStringOrNull(HymntableColumns.TITLE);
        return res;
    }

    /**
     * Get the {@code content} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getContent() {
        String res = getStringOrNull(HymntableColumns.CONTENT);
        return res;
    }

    /**
     * Get the {@code number} value.
     * Can be {@code null}.
     */
    @Nullable
    public Integer getNumber() {
        Integer res = getIntegerOrNull(HymntableColumns.NUMBER);
        return res;
    }

    /**
     * Get the {@code category} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getCategory() {
        String res = getStringOrNull(HymntableColumns.CATEGORY);
        return res;
    }

    /**
     * Get the {@code like} value.
     * Can be {@code null}.
     */
    @Nullable
    public Boolean getLike() {
        Boolean res = getBooleanOrNull(HymntableColumns.LIKE);
        return res;
    }
}
