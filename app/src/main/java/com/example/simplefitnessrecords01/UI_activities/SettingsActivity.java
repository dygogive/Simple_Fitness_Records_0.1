package com.example.simplefitnessrecords01.UI_activities;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceManager;

import com.example.simplefitnessrecords01.R;
import com.example.simplefitnessrecords01.databinding.SettingsActivityBinding;

public class SettingsActivity extends AppCompatActivity {


    SettingsActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //initial
        init();
    }






    /************************ INITIAL METHOD ********************************/
    private void init() {




        //set size of text
//        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
//        String selectedTextSize = preferences.getString("text_size_preference", getString(R.string.default_text_size));



        //initial my Toolbar
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(getString(R.string.settings));



        //initial Fragment in this activity for Settings
        getSupportFragmentManager().
                beginTransaction().
                replace(binding.frameSettings.getId(), new SettingsFragment()).
                commit();



    }











    /************ SETTINGS MENU *************************/

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {


        if  (item.getItemId() == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }








    //Fragment in this activity for Settings
    public static class SettingsFragment extends PreferenceFragmentCompat {

        @Override
        public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey);


        }
    }

}
