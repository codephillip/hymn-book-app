package com.codephillip.app.hymnbook.provider.categorytable;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.codephillip.app.hymnbook.provider.base.AbstractSelection;

/**
 * Selection for the {@code categorytable} table.
 */
public class CategorytableSelection extends AbstractSelection<CategorytableSelection> {
    @Override
    protected Uri baseUri() {
        return CategorytableColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code CategorytableCursor} object, which is positioned before the first entry, or null.
     */
    public CategorytableCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new CategorytableCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public CategorytableCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code CategorytableCursor} object, which is positioned before the first entry, or null.
     */
    public CategorytableCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new CategorytableCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public CategorytableCursor query(Context context) {
        return query(context, null);
    }


    public CategorytableSelection id(long... value) {
        addEquals("categorytable." + CategorytableColumns._ID, toObjectArray(value));
        return this;
    }

    public CategorytableSelection idNot(long... value) {
        addNotEquals("categorytable." + CategorytableColumns._ID, toObjectArray(value));
        return this;
    }

    public CategorytableSelection orderById(boolean desc) {
        orderBy("categorytable." + CategorytableColumns._ID, desc);
        return this;
    }

    public CategorytableSelection orderById() {
        return orderById(false);
    }

    public CategorytableSelection key(Long... value) {
        addEquals(CategorytableColumns.KEY, value);
        return this;
    }

    public CategorytableSelection keyNot(Long... value) {
        addNotEquals(CategorytableColumns.KEY, value);
        return this;
    }

    public CategorytableSelection keyGt(long value) {
        addGreaterThan(CategorytableColumns.KEY, value);
        return this;
    }

    public CategorytableSelection keyGtEq(long value) {
        addGreaterThanOrEquals(CategorytableColumns.KEY, value);
        return this;
    }

    public CategorytableSelection keyLt(long value) {
        addLessThan(CategorytableColumns.KEY, value);
        return this;
    }

    public CategorytableSelection keyLtEq(long value) {
        addLessThanOrEquals(CategorytableColumns.KEY, value);
        return this;
    }

    public CategorytableSelection orderByKey(boolean desc) {
        orderBy(CategorytableColumns.KEY, desc);
        return this;
    }

    public CategorytableSelection orderByKey() {
        orderBy(CategorytableColumns.KEY, false);
        return this;
    }

    public CategorytableSelection name(String... value) {
        addEquals(CategorytableColumns.NAME, value);
        return this;
    }

    public CategorytableSelection nameNot(String... value) {
        addNotEquals(CategorytableColumns.NAME, value);
        return this;
    }

    public CategorytableSelection nameLike(String... value) {
        addLike(CategorytableColumns.NAME, value);
        return this;
    }

    public CategorytableSelection nameContains(String... value) {
        addContains(CategorytableColumns.NAME, value);
        return this;
    }

    public CategorytableSelection nameStartsWith(String... value) {
        addStartsWith(CategorytableColumns.NAME, value);
        return this;
    }

    public CategorytableSelection nameEndsWith(String... value) {
        addEndsWith(CategorytableColumns.NAME, value);
        return this;
    }

    public CategorytableSelection orderByName(boolean desc) {
        orderBy(CategorytableColumns.NAME, desc);
        return this;
    }

    public CategorytableSelection orderByName() {
        orderBy(CategorytableColumns.NAME, false);
        return this;
    }
}
