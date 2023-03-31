package com.example.simplefitnessrecords01.UI_activities;

import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.view.MenuItem;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.simplefitnessrecords01.databinding.SettingsActivityBinding;

public class SettingsActivity extends AppCompatActivity {

    SettingsActivityBinding binding;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = SettingsActivityBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }



    private void init() {

        //initial my Toolbar
        Toolbar myToolbar = (Toolbar) binding.toolbarSettings;
        setSupportActionBar(myToolbar);


        getSupportFragmentManager().
                beginTransaction().
                replace(binding.toolbarSettings.getId(),new SettingsFragment()).
                commit();

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null ) {

        }

    }











    public static class SettingsFragment extends Fragment {

    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        return super.onOptionsItemSelected(item);
    }
}
