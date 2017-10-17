package com.yjy.problems.problem.list;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import butterknife.BindView;
import butterknife.ButterKnife;

import com.yjy.problems.R;

public class ProblemListActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.drawerLayout)
    DrawerLayout mDrawerLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_layout);
        ButterKnife.bind(this);

        initToolBar();
        initDrawerLayout();

        setTitle("");
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = fragmentManager.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
            fragment = new ProblemListFragment();
            fragmentManager.beginTransaction()
                    .add(R.id.fragmentContainer, fragment)
                    .commit();
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
