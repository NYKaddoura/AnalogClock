package com.example.nkaddouralab7;

import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.PreferenceFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class SettingsFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener {

    private SharedPreferences preferences;

    public SettingsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle onSavedInstanceState) {
        super.onCreate(onSavedInstanceState);

        addPreferencesFromResource(R.xml.preferences);
        preferences = getPreferenceScreen().getSharedPreferences();

        onSharedPreferenceChanged(preferences, "TIMER_INTERVAL");

        preferences.registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        Preference preference = findPreference(key);

        switch (key){
            case "TIMER_INTERVAL":
                int interval = Integer.parseInt(sharedPreferences.getString(key, "100"));

                if (interval < 100){
                    interval = 100;
                }
                if (interval > 5000){
                    interval = 5000;
                }

                sharedPreferences.edit().putString(key, Integer.toString(interval)).commit();
                preference.setSummary("Time Interval = " + interval);
                ((EditTextPreference)preference).setText(Integer.toString(interval));
                break;
        }
    }
}
