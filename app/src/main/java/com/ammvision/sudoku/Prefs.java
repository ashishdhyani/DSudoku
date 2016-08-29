package com.ammvision.sudoku;

import android.os.Bundle;
import android.preference.PreferenceActivity;

import com.ammvision.sudoku.R;

public class Prefs extends PreferenceActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.layout.pref);
    }
}
