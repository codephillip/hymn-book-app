package com.codephillip.app.hymnbook.utilities;

/**
 * Created by codephillip on 31/03/17.
 */

public class Utils {
    private static final Utils ourInstance = new Utils();
    public static final String FIRST_LAUNCH = "first_launch";
    public static boolean isSongActivityActive;
    public static final String CHANGE_VIEW = "change_view";
    public static final String IS_FAVORITE = "is_favorite";


    public static Utils getInstance() {
        return ourInstance;
    }

    public static int position = 0;

    private Utils() {
    }
}