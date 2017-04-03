package com.codephillip.app.hymnbook.provider.hymntable;

import com.codephillip.app.hymnbook.provider.base.BaseModel;

import java.util.Date;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Data model for the {@code hymntable} table.
 */
public interface HymntableModel extends BaseModel {

    /**
     * Get the {@code title} value.
     * Can be {@code null}.
     */
    @Nullable
    String getTitle();

    /**
     * Get the {@code content} value.
     * Can be {@code null}.
     */
    @Nullable
    String getContent();

    /**
     * Get the {@code number} value.
     * Can be {@code null}.
     */
    @Nullable
    Integer getNumber();

    /**
     * Get the {@code category} value.
     * Can be {@code null}.
     */
    @Nullable
    String getCategory();

    /**
     * Get the {@code like} value.
     * Can be {@code null}.
     */
    @Nullable
    Boolean getLike();
}
