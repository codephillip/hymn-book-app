package com.codephillip.app.hymnbook.provider.favoritetable;

import java.util.Date;

import android.content.Context;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

import com.codephillip.app.hymnbook.provider.base.AbstractSelection;

/**
 * Selection for the {@code favoritetable} table.
 */
public class FavoritetableSelection extends AbstractSelection<FavoritetableSelection> {
    @Override
    protected Uri baseUri() {
        return FavoritetableColumns.CONTENT_URI;
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param contentResolver The content resolver to query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code FavoritetableCursor} object, which is positioned before the first entry, or null.
     */
    public FavoritetableCursor query(ContentResolver contentResolver, String[] projection) {
        Cursor cursor = contentResolver.query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new FavoritetableCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(contentResolver, null)}.
     */
    public FavoritetableCursor query(ContentResolver contentResolver) {
        return query(contentResolver, null);
    }

    /**
     * Query the given content resolver using this selection.
     *
     * @param context The context to use for the query.
     * @param projection A list of which columns to return. Passing null will return all columns, which is inefficient.
     * @return A {@code FavoritetableCursor} object, which is positioned before the first entry, or null.
     */
    public FavoritetableCursor query(Context context, String[] projection) {
        Cursor cursor = context.getContentResolver().query(uri(), projection, sel(), args(), order());
        if (cursor == null) return null;
        return new FavoritetableCursor(cursor);
    }

    /**
     * Equivalent of calling {@code query(context, null)}.
     */
    public FavoritetableCursor query(Context context) {
        return query(context, null);
    }


    public FavoritetableSelection id(long... value) {
        addEquals("favoritetable." + FavoritetableColumns._ID, toObjectArray(value));
        return this;
    }

    public FavoritetableSelection idNot(long... value) {
        addNotEquals("favoritetable." + FavoritetableColumns._ID, toObjectArray(value));
        return this;
    }

    public FavoritetableSelection orderById(boolean desc) {
        orderBy("favoritetable." + FavoritetableColumns._ID, desc);
        return this;
    }

    public FavoritetableSelection orderById() {
        return orderById(false);
    }

    public FavoritetableSelection title(String... value) {
        addEquals(FavoritetableColumns.TITLE, value);
        return this;
    }

    public FavoritetableSelection titleNot(String... value) {
        addNotEquals(FavoritetableColumns.TITLE, value);
        return this;
    }

    public FavoritetableSelection titleLike(String... value) {
        addLike(FavoritetableColumns.TITLE, value);
        return this;
    }

    public FavoritetableSelection titleContains(String... value) {
        addContains(FavoritetableColumns.TITLE, value);
        return this;
    }

    public FavoritetableSelection titleStartsWith(String... value) {
        addStartsWith(FavoritetableColumns.TITLE, value);
        return this;
    }

    public FavoritetableSelection titleEndsWith(String... value) {
        addEndsWith(FavoritetableColumns.TITLE, value);
        return this;
    }

    public FavoritetableSelection orderByTitle(boolean desc) {
        orderBy(FavoritetableColumns.TITLE, desc);
        return this;
    }

    public FavoritetableSelection orderByTitle() {
        orderBy(FavoritetableColumns.TITLE, false);
        return this;
    }

    public FavoritetableSelection content(String... value) {
        addEquals(FavoritetableColumns.CONTENT, value);
        return this;
    }

    public FavoritetableSelection contentNot(String... value) {
        addNotEquals(FavoritetableColumns.CONTENT, value);
        return this;
    }

    public FavoritetableSelection contentLike(String... value) {
        addLike(FavoritetableColumns.CONTENT, value);
        return this;
    }

    public FavoritetableSelection contentContains(String... value) {
        addContains(FavoritetableColumns.CONTENT, value);
        return this;
    }

    public FavoritetableSelection contentStartsWith(String... value) {
        addStartsWith(FavoritetableColumns.CONTENT, value);
        return this;
    }

    public FavoritetableSelection contentEndsWith(String... value) {
        addEndsWith(FavoritetableColumns.CONTENT, value);
        return this;
    }

    public FavoritetableSelection orderByContent(boolean desc) {
        orderBy(FavoritetableColumns.CONTENT, desc);
        return this;
    }

    public FavoritetableSelection orderByContent() {
        orderBy(FavoritetableColumns.CONTENT, false);
        return this;
    }

    public FavoritetableSelection number(Integer... value) {
        addEquals(FavoritetableColumns.NUMBER, value);
        return this;
    }

    public FavoritetableSelection numberNot(Integer... value) {
        addNotEquals(FavoritetableColumns.NUMBER, value);
        return this;
    }

    public FavoritetableSelection numberGt(int value) {
        addGreaterThan(FavoritetableColumns.NUMBER, value);
        return this;
    }

    public FavoritetableSelection numberGtEq(int value) {
        addGreaterThanOrEquals(FavoritetableColumns.NUMBER, value);
        return this;
    }

    public FavoritetableSelection numberLt(int value) {
        addLessThan(FavoritetableColumns.NUMBER, value);
        return this;
    }

    public FavoritetableSelection numberLtEq(int value) {
        addLessThanOrEquals(FavoritetableColumns.NUMBER, value);
        return this;
    }

    public FavoritetableSelection orderByNumber(boolean desc) {
        orderBy(FavoritetableColumns.NUMBER, desc);
        return this;
    }

    public FavoritetableSelection orderByNumber() {
        orderBy(FavoritetableColumns.NUMBER, false);
        return this;
    }

    public FavoritetableSelection category(String... value) {
        addEquals(FavoritetableColumns.CATEGORY, value);
        return this;
    }

    public FavoritetableSelection categoryNot(String... value) {
        addNotEquals(FavoritetableColumns.CATEGORY, value);
        return this;
    }

    public FavoritetableSelection categoryLike(String... value) {
        addLike(FavoritetableColumns.CATEGORY, value);
        return this;
    }

    public FavoritetableSelection categoryContains(String... value) {
        addContains(FavoritetableColumns.CATEGORY, value);
        return this;
    }

    public FavoritetableSelection categoryStartsWith(String... value) {
        addStartsWith(FavoritetableColumns.CATEGORY, value);
        return this;
    }

    public FavoritetableSelection categoryEndsWith(String... value) {
        addEndsWith(FavoritetableColumns.CATEGORY, value);
        return this;
    }

    public FavoritetableSelection orderByCategory(boolean desc) {
        orderBy(FavoritetableColumns.CATEGORY, desc);
        return this;
    }

    public FavoritetableSelection orderByCategory() {
        orderBy(FavoritetableColumns.CATEGORY, false);
        return this;
    }

    public FavoritetableSelection like(Boolean value) {
        addEquals(FavoritetableColumns.LIKE, toObjectArray(value));
        return this;
    }

    public FavoritetableSelection orderByLike(boolean desc) {
        orderBy(FavoritetableColumns.LIKE, desc);
        return this;
    }

    public FavoritetableSelection orderByLike() {
        orderBy(FavoritetableColumns.LIKE, false);
        return this;
    }
}
