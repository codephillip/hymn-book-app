package com.codephillip.app.hymnbook.provider.categorytable;

import android.database.Cursor;
import android.support.annotation.Nullable;

import com.codephillip.app.hymnbook.provider.base.AbstractCursor;

/**
 * Cursor wrapper for the {@code categorytable} table.
 */
public class CategorytableCursor extends AbstractCursor implements CategorytableModel {
    public CategorytableCursor(Cursor cursor) {
        super(cursor);
    }

    /**
     * Primary key.
     */
    public long getId() {
        Long res = getLongOrNull(CategorytableColumns._ID);
        if (res == null)
            throw new NullPointerException("The value of '_id' in the database was null, which is not allowed according to the model definition");
        return res;
    }

    /**
     * Get the {@code key} value.
     * Can be {@code null}.
     */
    @Nullable
    public Long getKey() {
        Long res = getLongOrNull(CategorytableColumns.KEY);
        return res;
    }

    /**
     * Get the {@code name} value.
     * Can be {@code null}.
     */
    @Nullable
    public String getName() {
        String res = getStringOrNull(CategorytableColumns.NAME);
        return res;
    }
}
