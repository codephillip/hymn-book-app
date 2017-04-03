package com.codephillip.app.hymnbook.models;

import java.util.ArrayList;

/**
 * Created by codephillip on 31/03/17.
 */

public class HymnDatabase {
    private static final HymnDatabase ourInstance = new HymnDatabase();

    public static HymnDatabase getInstance() {
        return ourInstance;
    }

    //don't initialize with null
    public static Hymns hymns = new Hymns();

    public static ArrayList<String> categorys = new ArrayList<>();

    private HymnDatabase() {
    }



}
