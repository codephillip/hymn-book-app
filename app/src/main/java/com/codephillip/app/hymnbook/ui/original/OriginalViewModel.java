package com.codephillip.app.hymnbook.ui.original;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class OriginalViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public OriginalViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is home fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}