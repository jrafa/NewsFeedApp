package com.jrafa.newsfeedapp.news;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.jrafa.newsfeedapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NewsAdapter extends ArrayAdapter<News> {

    private static final String LOG_TAG = NewsLoader.class.getName();

    public NewsAdapter(@NonNull Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        News news = getItem(position);

        TextView titleNewsTextView = convertView.findViewById(R.id.title_text_view);
        titleNewsTextView.setText(news.getTitle());

        TextView authorNewsTextView = convertView.findViewById(R.id.author_text_view);
        authorNewsTextView.setText(news.getAuthor());

        TextView sectionNewsTextView = convertView.findViewById(R.id.section_text_view);
        sectionNewsTextView.setText(news.getSection());

        sectionNewsTextView.setTextColor(getSectionColor(news.getSection()));

        TextView datePublishNewsTextView = convertView.findViewById(R.id.date_publish_text_view);
        datePublishNewsTextView.setText(formatDateTimeInString(news.getDatePublish(), "MMM d, yyyy"));

        TextView timePublishNewsTextView = convertView.findViewById(R.id.time_publish_text_view);
        timePublishNewsTextView.setText(formatDateTimeInString(news.getDatePublish(), "h:mm a"));

        return convertView;
    }

    private String formatDateTimeInString(String datePublish, String pattern) {
        Date date = new Date();

        try {
            date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").parse(datePublish);
        } catch (ParseException e) {
            Log.v(LOG_TAG, "Parse exception!", e);
        }

        SimpleDateFormat formatter = new SimpleDateFormat(pattern);
        return formatter.format(date);
    }

    private int getSectionColor(String section) {
        int sectionColor;

        switch (section.toLowerCase()) {
            case "technology":
                sectionColor = R.color.colorSectionTechnology;
                break;
            case "games":
                sectionColor = R.color.colorSectionGames;
                break;
            case "culture":
                sectionColor = R.color.colorSectionCulture;
                break;
            default:
                sectionColor = R.color.colorSectionDefault;
        }

        return ContextCompat.getColor(getContext(), sectionColor);
    }

}
