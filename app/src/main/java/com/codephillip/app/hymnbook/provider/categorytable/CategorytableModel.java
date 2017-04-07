package com.codephillip.app.hymnbook.provider.categorytable;

import com.codephillip.app.hymnbook.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Data model for the {@code categorytable} table.
 */
public interface CategorytableModel extends BaseModel {

    /**
     * Get the {@code key} value.
     * Can be {@code null}.
     */
    @Nullable
    Long getKey();

    /**
     * Get the {@code name} value.
     * Can be {@code null}.
     */
    @Nullable
    String getName();
}
