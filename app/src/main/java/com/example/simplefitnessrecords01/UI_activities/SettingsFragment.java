package com.example.simplefitnessrecords01.UI_activities;

import android.os.Bundle;

import androidx.preference.PreferenceFragmentCompat;

import com.example.simplefitnessrecords01.R;

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey);


    }
}