package it.jaschke.alexandria.fragments;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import it.jaschke.alexandria.R;

/**
 * Created by Amrendra Kumar on 24/01/16.
 */
public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
    }
}
