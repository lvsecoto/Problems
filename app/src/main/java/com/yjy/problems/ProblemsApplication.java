package com.yjy.problems;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import com.yjy.problems.data.source.ProblemDataSource;
import com.yjy.problems.utils.DemoProblems;

public class ProblemsApplication extends Application {

    public static final String KEY_IS_FIRST_USE = "is_first_use";

    private static Context mAppContext = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mAppContext = getApplicationContext();

        checkFirstTimeUse();
    }

    private void checkFirstTimeUse() {
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(getAppContext());
        boolean isFirstTimeUse = sharedPreferences.getBoolean(KEY_IS_FIRST_USE, true);

        if (isFirstTimeUse) {
            DemoProblems.getInstance().loadFirstUseProblems(ProblemDataSource.getInstance());

            sharedPreferences.edit()
                    .putBoolean(KEY_IS_FIRST_USE, false)
                    .apply();
        }
    }

    public static Context getAppContext() {
        return mAppContext;
    }
}
