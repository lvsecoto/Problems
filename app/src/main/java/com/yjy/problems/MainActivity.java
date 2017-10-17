package com.yjy.problems;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.yjy.problems.problem.list.ProblemListFragment;
import com.yjy.problems.settings.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    @BindView(R.id.navigation)
    NavigationView mNavigationView;

    private FragmentManager mFragmentManager;

    private ProblemListFragment mProblemListFragment;

    private SettingsFragment mSettingsFragment;

    private Fragment mCurrentFragment;

    private NavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.home:
                    switchToFragment(mProblemListFragment);
                    break;
                case R.id.settings:
                    switchToFragment(mSettingsFragment);
                    break;
            }

            mDrawerLayout.closeDrawers();

            return true;
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);
        ButterKnife.bind(this);

        mFragmentManager = getSupportFragmentManager();

        initToolBar();
        initDrawerLayout();
        initFragmentContainer();

        mNavigationView.setNavigationItemSelectedListener(navigationItemSelectedListener);
        mNavigationView.setCheckedItem(R.id.home);

        setTitle("");

    }

    private void initToolBar() {
        setSupportActionBar(mToolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_24dp);
    }

    private void initDrawerLayout() {
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, android.R.string.ok, android.R.string.cancel);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void initFragmentContainer() {
        Fragment fragment = mFragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            mProblemListFragment = new ProblemListFragment();
            mSettingsFragment = new SettingsFragment();
            mFragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, mProblemListFragment)
                    .hide(mProblemListFragment)
                    .add(R.id.fragmentContainer, mSettingsFragment)
                    .hide(mSettingsFragment)
                    .commit();

            mCurrentFragment = mProblemListFragment;
            switchToFragment(mProblemListFragment);
        }
    }

    private void switchToFragment(Fragment fragment) {
        clearToolbar();

        getSupportFragmentManager()
                .beginTransaction()
                .hide(mCurrentFragment)
                .show(fragment)
                .commit();

        mCurrentFragment = fragment;
    }

    private void clearToolbar() {
        setTitle("");
    }

}
