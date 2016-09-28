package com.jksam.jaikishan96.bill;

import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceActivity;
import android.text.method.DigitsKeyListener;

public class TPref extends PreferenceActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.task_preferences);

        // Set the time default to a numeric number only
        EditTextPreference timeDefault = (EditTextPreference) findPreference(getString(R.string.pref_default_time_from_now_key));
        timeDefault.getEditText().setKeyListener(DigitsKeyListener.getInstance());
    }
}

