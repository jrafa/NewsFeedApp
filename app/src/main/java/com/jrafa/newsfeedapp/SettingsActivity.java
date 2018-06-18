package com.jrafa.newsfeedapp;

import android.app.DatePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.DatePicker;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }

    public static class NewsPreferenceFragment extends PreferenceFragment implements Preference.OnPreferenceChangeListener, DatePickerDialog.OnDateSetListener {

        @Override
        public void onCreate(@Nullable Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            addPreferencesFromResource(R.xml.settings_main);

            Preference orderByNews = findPreference(getString(R.string.settings_order_by_key));
            bindPreferenceSummaryToValue(orderByNews);

            Preference searchNews = findPreference(getString(R.string.settings_search_by_key));
            bindPreferenceSummaryToValue(searchNews);

            Preference counterNews = findPreference(getString(R.string.settings_counter_news_by_key));
            bindPreferenceSummaryToValue(counterNews);

            Preference fromDateNews = findPreference(getString(R.string.settings_from_date_by_key));
            bindPreferenceSummaryToValue(fromDateNews);

            fromDateNews.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    showDateDialog();
                    return false;
                }
            });

        }

        @Override
        public boolean onPreferenceChange(Preference preference, Object o) {
            String stringValue = o.toString();
            preference.setSummary(stringValue);

            if (preference instanceof ListPreference) {
                ListPreference listPreference = (ListPreference) preference;
                int prefIndex = listPreference.findIndexOfValue(stringValue);
                if (prefIndex >= 0) {
                    CharSequence[] labels = listPreference.getEntries();
                    preference.setSummary(labels[prefIndex]);
                }
            } else {
                preference.setSummary(stringValue);
            }

            return true;
        }

        @Override
        public void onDateSet(DatePicker datePicker, int year, int month, int day) {
            Preference preference = findPreference(getString(R.string.settings_from_date_by_key));
            preference.setOnPreferenceChangeListener(this);

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String fromDateString = formatDateToString(year, month, day);

            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(preference.getKey(), fromDateString);
            editor.apply();
            editor.commit();

            onPreferenceChange(preference, fromDateString);
        }

        private void bindPreferenceSummaryToValue(Preference preference) {
            preference.setOnPreferenceChangeListener(this);
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(preference.getContext());
            String preferenceString = preferences.getString(preference.getKey(), "");
            onPreferenceChange(preference, preferenceString);
        }

        private void showDateDialog() {
            final Calendar currentDate = Calendar.getInstance();
            int year = currentDate.get(Calendar.YEAR);
            int month = currentDate.get(Calendar.MONTH);
            int day = currentDate.get(Calendar.DAY_OF_MONTH);
            new DatePickerDialog(getActivity(), this, year, month, day).show();
        }

        private String formatDateToString(int year, int month, int day) {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            return formatter.format(new Date(year - 1900, month, day));
        }
    }

}
