package com.codephillip.app.hymnbook.utilities;

import android.content.Context;
import android.graphics.Typeface;

import com.codephillip.app.hymnbook.provider.hymntable.HymntableCursor;
import com.codephillip.app.hymnbook.provider.hymntable.HymntableSelection;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Created by codephillip on 31/03/17.
 */

public class Utils {
    private static final Utils ourInstance = new Utils();
    public static final String FIRST_LAUNCH = "first_launch";
    private static final String TAG = Utils.class.getSimpleName();
    public static final String FONT_SIZE = "font_size";
    public static final String THEME_KEY = "theme_key";
    public static final String THEME_WHITE = "white";
    public static final String THEME_SEPIA = "sepia";
    public static final String THEME_DARK = "dark";
    public static final String HAS_LOCKED = "lock";
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

    public static int findLargestNumber(String text) {
        int max = Integer.MIN_VALUE;
        String[] words = text.split("\\D+");

        for (String word : words) {
            if (!word.isEmpty()) {
                int num = Integer.parseInt(word);
                if (num > max) {
                    max = num;
                }
            }
        }
        return max;
    }

    public enum Season {
        ADVENT, CHRISTMAS, LENT, EASTER, PENTECOST, ORDINARY_TIME
    }

    public static Season getCurrentSeason() {
        Calendar cal = Calendar.getInstance();
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        if (month == Calendar.DECEMBER) {
            return day <= 24 ? Season.ADVENT : Season.CHRISTMAS;
        } else if (month == Calendar.JANUARY && day <= 6) {
            return Season.CHRISTMAS;
        } else if (month == Calendar.MARCH || month == Calendar.APRIL) {
            // Simulating Lent/Easter approximation
            return (month == Calendar.MARCH && day < 20) ? Season.LENT : Season.EASTER;
        } else if (month == Calendar.MAY || month == Calendar.JUNE) {
            return Season.PENTECOST;
        } else {
            return Season.ORDINARY_TIME;
        }
    }

    public static List<String> getCategoriesForSeason(Season season, Map<Season, List<String>> customMapping) {
        if (customMapping != null && customMapping.containsKey(season)) {
            return customMapping.get(season);
        }

        List<String> categories = new ArrayList<>();
        switch (season) {
            case ADVENT:
                categories.add("MAYINGIRA");
                break;
            case CHRISTMAS:
                categories.add("MAZAALIBWA");
                break;
            case LENT:
                categories.add("KUBONABONA");
                break;
            case EASTER:
                categories.add("MAZUUKIRA");
                categories.add("MAZUKIRA");
                break;
            case PENTECOST:
                categories.add("MWOYO MUTUUKIRIVU");
                categories.add("MWOYO MUTUKIRIVU");
                break;
            case ORDINARY_TIME:
            default:
                categories.add("KUSINZA");
                categories.add("MUGAATI OGW");
                categories.add("KWAGALA");
                break;
        }
        return categories;
    }

    public static Map<Season, List<String>> getDefaultSeasonMapping() {
        Map<Season, List<String>> mapping = new HashMap<>();
        for (Season season : Season.values()) {
            mapping.put(season, getCategoriesForSeason(season, null));
        }
        return mapping;
    }

    public static HymntableCursor getSeasonalHymnCursor(Context context, Map<Season, List<String>> customMapping, String categorySuffix) {
        Season season = getCurrentSeason();
        List<String> categories = getCategoriesForSeason(season, customMapping);
        HymntableSelection selection = new HymntableSelection();
        String[] cats = categories.toArray(new String[0]);
        if (categorySuffix != null && !categorySuffix.isEmpty()) {
            return selection.categoryContains(cats).and().categoryEndsWith(categorySuffix).query(context.getContentResolver());
        }
        return selection.categoryContains(cats).query(context.getContentResolver());
    }
}
