package com.example.roman.imagegrid;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, TabLayout.OnTabSelectedListener {

    private static final String PACKAGE = "com.example.android.imagegrid";
    static float sAnimatorScale = 1;

    private NavigationView navigationView;

    private boolean isLight;
    private int currentTheme;
    private int oldTheme;

    private int col_num;
    private int old_col_num;


    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Following options to change the Theme must precede setContentView().

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        String lister = sharedPref.getString("theme_preference", "1");
        oldTheme = Integer.parseInt(lister);

        String gridColumns = sharedPref.getString("pref_col_num", "4");
        old_col_num = Integer.parseInt(gridColumns);

        toggleSettings();

        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);

        mViewPager.setAdapter(mSectionsPagerAdapter);


        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);
        tabLayout.setOnTabSelectedListener(this);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        View hView =  navigationView.getHeaderView(0);
        ImageView profileView = (ImageView)hView.findViewById(R.id.profile);

        BitmapUtils profBmp = new BitmapUtils();
        Bitmap circlProfile = profBmp.getRoundedShape(BitmapFactory
                .decodeResource(getResources(), R.drawable.ic_myphoto));

        profileView.setImageBitmap(circlProfile);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    private void toggleSettings() {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);

        String gridColumns = sharedPref.getString("pref_col_num", "4");
        col_num = Integer.parseInt(gridColumns);

        String lister = sharedPref.getString("theme_preference", "1");
        currentTheme = Integer.parseInt(lister);

        if(currentTheme == 2){
            isLight = false;
        } else {
            isLight = true;
        }

        if(isLight) {
            setTheme(R.style.LightTheme);
        } else {
            setTheme(R.style.DarkTheme);
        }

        if(oldTheme != currentTheme || old_col_num != col_num){

            oldTheme = currentTheme;
            old_col_num = col_num;

            Intent curIntent = new Intent(this, MainActivity.class);
            curIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(curIntent);

        }
    }


    /** onPause is called when the activity is going to background. */

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();

        toggleSettings();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        MenuItem item;

        final int position = tab.getPosition();
        if (tab.getPosition() == 0) {
            item = navigationView.getMenu().findItem(R.id.gallery);
            item.setCheckable(true);
            item.setChecked(true);
            mViewPager.setCurrentItem(0);
        } else if (tab.getPosition() == 1) {
            item = navigationView.getMenu().findItem(R.id.photo);
            item.setCheckable(true);
            item.setChecked(true);
            mViewPager.setCurrentItem(1);
        }
        else if (tab.getPosition() == 2) {
            item = navigationView.getMenu().findItem(R.id.cache);
            item.setCheckable(true);
            item.setChecked(true);
            mViewPager.setCurrentItem(2);
        }
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
   }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, PrefsActivity.class);
            startActivityForResult(intent, 0);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        //TabHost tabHost = getTabHost();


        if (id == R.id.gallery) {
            mViewPager.setCurrentItem(0);

        } else if (id == R.id.photo) {
            mViewPager.setCurrentItem(1);

        } else if (id == R.id.cache) {
            mViewPager.setCurrentItem(2);

        } else if (id == R.id.settings) {
            Intent intent = new Intent();
            intent.setClass(MainActivity.this, PrefsActivity.class);
            startActivityForResult(intent, 0);

        } else if (id == R.id.feedback) {
            /* Create the Intent */
            final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

            /* Fill it with Data */
            emailIntent.setType("plain/text");
            emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"roma.sergeev@gmail.com"});
            emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Feedback");
            emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "");

            /* Send it off to the Activity-Chooser */
            this.startActivity(Intent.createChooser(emailIntent, "Send mail..."));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * A placeholder fragment containing a simple view.
     */

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();


        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            return PlaceholderFragment.newInstance(position + 1);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Галерея";
                case 1:
                    return "Яфотки";
                case 2:
                    return "Кэш";
            }
            return null;
        }
    }
}
