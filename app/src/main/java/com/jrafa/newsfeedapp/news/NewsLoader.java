package com.jrafa.newsfeedapp.news;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.jrafa.newsfeedapp.utils.QueryUtils;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private final String mUrl;

    public NewsLoader(Context context, String mUrl) {
        super(context);
        this.mUrl = mUrl;
    }

    @Override
    public List<News> loadInBackground() {
        if (mUrl == null) return null;

        return QueryUtils.fetchData(mUrl);
    }

    @Override
    protected void onStartLoading() {
        forceLoad();

    }
}
