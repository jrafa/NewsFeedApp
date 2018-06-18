package com.jrafa.newsfeedapp;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.jrafa.newsfeedapp.news.News;
import com.jrafa.newsfeedapp.news.NewsAdapter;
import com.jrafa.newsfeedapp.news.NewsLoader;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {

    private final static String GUARDIAN_URL = "http://content.guardianapis.com/search?";

    private final static int LOADER_ID = 1;

    private NewsAdapter adapter;

    private TextView emptyStateTextView;
    private ProgressBar newsProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView newsListView = findViewById(R.id.news_list_view);

        emptyStateTextView = findViewById(R.id.empty_view);
        newsListView.setEmptyView(emptyStateTextView);

        adapter = new NewsAdapter(this, new ArrayList<News>());
        newsListView.setAdapter(adapter);

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News news = adapter.getItem(i);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrl()));
                startActivity(intent);
            }
        });

        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();

        if (isConnected) {
            getLoaderManager().initLoader(LOADER_ID, null, this);
        } else {
            newsProgressBar = findViewById(R.id.loading_spinner);
            newsProgressBar.setVisibility(View.GONE);
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {

        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

        String orderByNews = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        String searchNews = sharedPrefs.getString(
                getString(R.string.settings_search_by_key),
                getString(R.string.settings_search_by_default)
        );

        String counterNews = sharedPrefs.getString(
                getString(R.string.settings_counter_news_by_key),
                getString(R.string.settings_counter_news_by_default)
        );

        String fromDateNews = sharedPrefs.getString(
                getString(R.string.settings_from_date_by_key),
                getString(R.string.settings_from_date_by_default)
        );

        Uri baseUri = Uri.parse(GUARDIAN_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("q", searchNews);
        uriBuilder.appendQueryParameter("order-by", orderByNews);
        uriBuilder.appendQueryParameter("from-date", fromDateNews);
        uriBuilder.appendQueryParameter("api-key", "97f5678b-e544-491f-b7de-4505b9c8f3c5");
        uriBuilder.appendQueryParameter("page-size", counterNews);
        uriBuilder.appendQueryParameter("show-tags", "contributor");

        return new NewsLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        emptyStateTextView.setText(R.string.no_news);

        newsProgressBar = findViewById(R.id.loading_spinner);
        newsProgressBar.setVisibility(View.GONE);

        adapter.clear();

        if (news != null && !news.isEmpty()) {
            adapter.addAll(news);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        adapter.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
