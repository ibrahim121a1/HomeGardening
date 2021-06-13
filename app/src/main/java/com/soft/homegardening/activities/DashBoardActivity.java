
package com.soft.homegardening.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.os.Parcelable;
import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.soft.homegardening.AlarmsFragment;
import com.soft.homegardening.FavoriteFragment;
import com.soft.homegardening.HomeFragment;
import com.soft.homegardening.IdentifyPlantFragment;
import com.soft.homegardening.MyGardenFragment;
import com.soft.homegardening.ProfileFragment;
import com.soft.homegardening.R;
import com.soft.homegardening.SavedFragment;
import com.soft.homegardening.fragments.BaseFragment;

public class DashBoardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    //VARIABLE DECLARED
    private DrawerLayout drawerLayout;
    private BaseFragment currentFragment;
    private BaseFragment defaultFrag;
    FragmentManager fm;
    FragmentTransaction ft;
    NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        //VARIABLE INITIALIZED
        navigationView = findViewById(R.id.nav_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        Toolbar toolbar = findViewById(R.id.toolbar);
        navigationView.setItemIconTintList(null);
        navigationView.setNavigationItemSelectedListener(this);
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment());
            navigationView.setCheckedItem(R.id.nav_home);
        }

    }

    //CLICK MENU OPTION TO OPEN THAT FRAGMENT
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                loadFragment(new HomeFragment());
                break;
            case R.id.nav_my_plants:
                loadFragment(new MyGardenFragment());
                break;
            case R.id.nav_saved:
                loadFragment(new SavedFragment());
                break;
            case R.id.nav_settings:
                loadFragment(new ProfileFragment());
                break;
            case R.id.nav_favirote:
                loadFragment(new FavoriteFragment());
                break;
            case R.id.nav_alarm:
                loadFragment(new AlarmsFragment());
                break;
            case R.id.nav_identify:
                loadFragment(new IdentifyPlantFragment());
                break;

        }


        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    public void loadFragment(BaseFragment loadFragment) {
        if (currentFragment == null) {
            defaultFrag = loadFragment;
        }

        if (currentFragment != null && currentFragment.getFragmentName().equals(loadFragment.getFragmentName())) {
            return;
        }

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }

        this.currentFragment = loadFragment;
        String backStateName = loadFragment.getClass().getName();

        fm = getSupportFragmentManager();

        ft = fm.beginTransaction().addToBackStack(backStateName);
        ft.setCustomAnimations(R.anim.fragment_slide_left_enter,
                R.anim.fragment_slide_left_exit,
                R.anim.fragment_slide_right_enter,
                R.anim.fragment_slide_right_exit);
        ft.replace(R.id.fl_container, loadFragment, backStateName);
        ft.commit();
    }

    public BaseFragment getLatestFragmentFromBackStack() {
        int index = getSupportFragmentManager().getBackStackEntryCount() - 1;
        if (index < 0 || getSupportFragmentManager().getBackStackEntryCount() == 0)
            return null;
        else {
            FragmentManager.BackStackEntry backEntry = getSupportFragmentManager().getBackStackEntryAt(index);
            String tag = backEntry.getName();
            BaseFragment baseFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag(tag);
            return baseFragment;
        }
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        if (currentFragment != defaultFrag) {
            getFragmentManager().popBackStack();
            navigationView.setCheckedItem(R.id.nav_my_plants);
            loadFragment(defaultFrag);
        } else {
            super.onBackPressed();
            int fragments = getSupportFragmentManager().getBackStackEntryCount();
            if (fragments == 0) {
                finish();
            }
        }
    }
}