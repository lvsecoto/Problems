package com.yjy.problems.problem.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import com.yjy.problems.R;

public class ProblemDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_fragment);

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);

        if (fragment == null) {
            fragment = new ProblemDetailFragment();
            fragment.setArguments(getIntent().getExtras());
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
    }

}
