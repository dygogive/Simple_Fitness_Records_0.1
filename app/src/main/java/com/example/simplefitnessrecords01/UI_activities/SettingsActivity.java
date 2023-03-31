package com.example.simplefitnessrecords01.UI_activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {



    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);

        init();
    }



    private void init() {

    }











    public static class SettingsFragment extends PreferenceFragment {

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
