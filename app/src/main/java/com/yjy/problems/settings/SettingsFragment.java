package com.yjy.problems.settings;

import android.os.Bundle;
import android.preference.Preference;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;
import com.github.machinarius.preferencefragment.PreferenceFragment;
import com.yjy.problems.R;
import com.yjy.problems.data.source.ProblemDataSource;
import com.yjy.problems.utils.DemoProblems;

/**
 * Created by Administrator on 2017/10/16.
 */

public class SettingsFragment extends PreferenceFragment {

    @Override
    public void onCreate(Bundle paramBundle) {
        super.onCreate(paramBundle);
        addPreferencesFromResource(R.xml.preference_main);

        findPreference("key_load_demo").setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                if (ProblemDataSource.getInstance().getCount() != 0) {
                    Toast.makeText(getContext(), "Please delete all your problems", Toast.LENGTH_LONG)
                            .show();
                    return true;
                }
                DemoProblems.getInstance().loadDemoProblems(ProblemDataSource.getInstance());
                return true;
            }
        });
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            return;
        }

        ((AppCompatActivity) getActivity()).setTitle("Settings");
    }


}
