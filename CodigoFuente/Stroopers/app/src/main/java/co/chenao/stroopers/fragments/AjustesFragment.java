package co.chenao.stroopers.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import co.chenao.stroopers.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class AjustesFragment extends PreferenceFragmentCompat {


    public AjustesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String key) {
        setPreferencesFromResource(R.xml.preferencias,key);
    }


}
