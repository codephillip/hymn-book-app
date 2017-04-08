package com.codephillip.app.hymnbook.utilities;

import android.graphics.Typeface;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;

/**
 * Created by codephillip on 31/03/17.
 */

public class Utils {
    private static final Utils ourInstance = new Utils();
    public static final String FIRST_LAUNCH = "first_launch";
    private static final String TAG = Utils.class.getSimpleName();
    public static final String FONT_SIZE = "font_size";
    public static boolean isSongActivityActive;
    public static final String CHANGE_VIEW = "change_view";
    public static final String IS_FAVORITE = "is_favorite";
    public static final String CATEGORY = "category";
    public static final String[] screenNames = {"All Songs", "Category", "Favorite", "About"};
    public static String category = "worship";
    public static boolean clickedFavorite;
    //used to position the ViewPager in SongActivity
    public static int position = 0;
    public static HymntableCursor cursor;
    public static boolean showFavoriteScreen;
    public static boolean isFromCategoryFragment;
    public static Typeface typeface;

    public static Utils getInstance() {
        return ourInstance;
    }

    private Utils() {
    }

    public static TextDrawable generateTextDrawable(int position, ColourQueue colourQueue) {
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getColor(colourQueue.getCount());
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(140)  // width in px
                .height(140) // height in px
                .endConfig()
                .buildRound(String.valueOf(position), color1);
        return drawable;
    }

    public static TextDrawable generateTextDrawable(String text, ColourQueue colourQueue) {
        ColorGenerator generator = ColorGenerator.MATERIAL; // or use DEFAULT
        int color1 = generator.getColor(colourQueue.getCount());
        TextDrawable drawable = TextDrawable.builder()
                .beginConfig()
                .width(140)  // width in px
                .height(140) // height in px
                .endConfig()
                .buildRound(text, color1);
        return drawable;
    }
}
