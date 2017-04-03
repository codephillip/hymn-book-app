package com.codephillip.app.hymnbook.provider.hymntable;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.codephillip.app.hymnbook.provider.base.AbstractSelection;

/**
 * Selection for the {@code hymntable} table.
 */
public class HymntableSelection extends AbstractSelection<HymntableSelection> {
    @Override
    protected Uri baseUri() {
        return HymntableColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code HymntableCursor} object, which is positioned before the first entry, or null.
     */
    public HymntableCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new HymntableCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public HymntableCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code HymntableCursor} object, which is positioned before the first entry, or null.
     */
    public HymntableCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new HymntableCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public HymntableCursor query(Context context) {
        return query(context, null);
    }


    public HymntableSelection id(long... value) {
        addEquals("hymntable." + HymntableColumns._ID, toObjectArray(value));
        return this;
    }

    public HymntableSelection idNot(long... value) {
        addNotEquals("hymntable." + HymntableColumns._ID, toObjectArray(value));
        return this;
    }

    public HymntableSelection orderById(boolean desc) {
        orderBy("hymntable." + HymntableColumns._ID, desc);
        return this;
    }

    public HymntableSelection orderById() {
        return orderById(false);
    }

    public HymntableSelection title(String... value) {
        addEquals(HymntableColumns.TITLE, value);
        return this;
    }

    public HymntableSelection titleNot(String... value) {
        addNotEquals(HymntableColumns.TITLE, value);
        return this;
    }

    public HymntableSelection titleLike(String... value) {
        addLike(HymntableColumns.TITLE, value);
        return this;
    }

    public HymntableSelection titleContains(String... value) {
        addContains(HymntableColumns.TITLE, value);
        return this;
    }

    public HymntableSelection titleStartsWith(String... value) {
        addStartsWith(HymntableColumns.TITLE, value);
        return this;
    }

    public HymntableSelection titleEndsWith(String... value) {
        addEndsWith(HymntableColumns.TITLE, value);
        return this;
    }

    public HymntableSelection orderByTitle(boolean desc) {
        orderBy(HymntableColumns.TITLE, desc);
        return this;
    }

    public HymntableSelection orderByTitle() {
        orderBy(HymntableColumns.TITLE, false);
        return this;
    }

    public HymntableSelection content(String... value) {
        addEquals(HymntableColumns.CONTENT, value);
        return this;
    }

    public HymntableSelection contentNot(String... value) {
        addNotEquals(HymntableColumns.CONTENT, value);
        return this;
    }

    public HymntableSelection contentLike(String... value) {
        addLike(HymntableColumns.CONTENT, value);
        return this;
    }

    public HymntableSelection contentContains(String... value) {
        addContains(HymntableColumns.CONTENT, value);
        return this;
    }

    public HymntableSelection contentStartsWith(String... value) {
        addStartsWith(HymntableColumns.CONTENT, value);
        return this;
    }

    public HymntableSelection contentEndsWith(String... value) {
        addEndsWith(HymntableColumns.CONTENT, value);
        return this;
    }

    public HymntableSelection orderByContent(boolean desc) {
        orderBy(HymntableColumns.CONTENT, desc);
        return this;
    }

    public HymntableSelection orderByContent() {
        orderBy(HymntableColumns.CONTENT, false);
        return this;
    }

    public HymntableSelection number(Integer... value) {
        addEquals(HymntableColumns.NUMBER, value);
        return this;
    }

    public HymntableSelection numberNot(Integer... value) {
        addNotEquals(HymntableColumns.NUMBER, value);
        return this;
    }

    public HymntableSelection numberGt(int value) {
        addGreaterThan(HymntableColumns.NUMBER, value);
        return this;
    }

    public HymntableSelection numberGtEq(int value) {
        addGreaterThanOrEquals(HymntableColumns.NUMBER, value);
        return this;
    }

    public HymntableSelection numberLt(int value) {
        addLessThan(HymntableColumns.NUMBER, value);
        return this;
    }

    public HymntableSelection numberLtEq(int value) {
        addLessThanOrEquals(HymntableColumns.NUMBER, value);
        return this;
    }

    public HymntableSelection orderByNumber(boolean desc) {
        orderBy(HymntableColumns.NUMBER, desc);
        return this;
    }

    public HymntableSelection orderByNumber() {
        orderBy(HymntableColumns.NUMBER, false);
        return this;
    }

    public HymntableSelection category(String... value) {
        addEquals(HymntableColumns.CATEGORY, value);
        return this;
    }

    public HymntableSelection categoryNot(String... value) {
        addNotEquals(HymntableColumns.CATEGORY, value);
        return this;
    }

    public HymntableSelection categoryLike(String... value) {
        addLike(HymntableColumns.CATEGORY, value);
        return this;
    }

    public HymntableSelection categoryContains(String... value) {
        addContains(HymntableColumns.CATEGORY, value);
        return this;
    }

    public HymntableSelection categoryStartsWith(String... value) {
        addStartsWith(HymntableColumns.CATEGORY, value);
        return this;
    }

    public HymntableSelection categoryEndsWith(String... value) {
        addEndsWith(HymntableColumns.CATEGORY, value);
        return this;
    }

    public HymntableSelection orderByCategory(boolean desc) {
        orderBy(HymntableColumns.CATEGORY, desc);
        return this;
    }

    public HymntableSelection orderByCategory() {
        orderBy(HymntableColumns.CATEGORY, false);
        return this;
    }

    public HymntableSelection like(Boolean value) {
        addEquals(HymntableColumns.LIKE, toObjectArray(value));
        return this;
    }

    public HymntableSelection orderByLike(boolean desc) {
        orderBy(HymntableColumns.LIKE, desc);
        return this;
    }

    public HymntableSelection orderByLike() {
        orderBy(HymntableColumns.LIKE, false);
        return this;
    }
}
