package com.codephillip.app.hymnbook.utilities;

/**
 * Created by codephillip on 01/04/17.
 */

public class ColourQueue {

    int count = 0;

    public ColourQueue(int count) {
        this.count = count;
    }

    public ColourQueue() {
    }

    public int getCount() {
        count++;
        if (count == 17) {
            resetCount();
        }
        return count;
    }

    private void resetCount() {
        setCount(0);
    }

    public void setCount(int count) {
        this.count = count;
    }
}
