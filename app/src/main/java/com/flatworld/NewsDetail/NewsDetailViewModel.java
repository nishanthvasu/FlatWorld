package com.flatworld.NewsDetail;

import android.content.Context;

import java.util.Observable;

public class NewsDetailViewModel extends Observable {

    NewsDetailNavigator NewsDetailNavigator;
    private Context mContext;

    public void setNavigator(NewsDetailNavigator taskDetailNavigator) {
        NewsDetailNavigator = taskDetailNavigator;
    }

    public NewsDetailViewModel(Context context) {
        mContext = context;
    }

    public void onBack() {
        NewsDetailNavigator.onBackPressed();
    }
}
