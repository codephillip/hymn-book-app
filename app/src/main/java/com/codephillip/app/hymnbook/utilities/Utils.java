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
    public static final String HAS_LOCKED = "lock";
    public static boolean isSongActivityActive;
    public static final String CHANGE_VIEW = "change_view";
    public static final String IS_FAVORITE = "is_favorite";
    public static final String SONG_TYPE = "song_type";
    public static final String CATEGORY = "category";
    public static final String[] screenNames = {"Original Songs", "Home Songs", "Category", "Favorite"};
    public static String category = "worship";
    public static String ORIGINAL_SONGS = "Original songs";
    public static String HOME_SONGS = "Home songs";
    public static boolean clickedFavorite;
    //used to position the ViewPager in SongActivity
    public static int position = 0;
    public static HymntableCursor cursor;
    public static boolean showFavoriteScreen = false;
    public static String songType = "";
    public static boolean isFromCategoryFragment;
    public static Typeface typeface;
    //local environment
    //public static final String BASE_URL = "http://10.0.3.2:8000";
    //production environment
    public static final String BASE_URL = "https://hymnbook.herokuapp.com";
    public static String HAS_SYNCHRONIZED = "has_synch";


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
