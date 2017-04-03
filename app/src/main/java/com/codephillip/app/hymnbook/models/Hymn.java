package com.codephillip.app.hymnbook.models;

/**
 * Created by codephillip on 31/03/17.
 */

public class Hymn {

    private int number;
    private String title;
    private String content;
    private String category;
    private long id;
    private boolean liked = false;

    public Hymn(int number, String title, String content, String category) {
        this.number = number;
        this.title = title;
        this.content = content;
        this.category = category;
    }

    public Hymn(int number, String title, String content, String category, boolean liked) {
        this.number = number;
        this.title = title;
        this.content = content;
        this.category = category;
        this.liked = liked;
    }

    public Hymn() {
    }

    public Hymn(int number, String title, String content, String category, long id, boolean liked) {
        this.number = number;
        this.title = title;
        this.content = content;
        this.category = category;
        this.id = id;
        this.liked = liked;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
