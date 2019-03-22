package com.pcchin.simpletime;

import android.content.Intent;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.pcchin.simpletime.fragment.ClockFragment;
import com.pcchin.simpletime.fragment.StopwatchFragment;
import com.pcchin.simpletime.fragment.TimerFragment;
import com.pcchin.simpletime.thread.StopwatchThread;
import com.pcchin.simpletime.thread.TimerThread;

public class MainActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce;
    public TimerThread timerThread;
    private SectionsPagerAdapter pagerAdapter;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPager = findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Press back to exit
        if (doubleBackToExitPressedOnce) {
            moveTaskToBack(true);
            android.os.Process.killProcess(android.os.Process.myPid());
            System.exit(0);
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 1500);

        }
    }

    // Tab between fragments
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            switch (position) {
                case 0:
                    return new ClockFragment();
                case 1:
                    return new StopwatchFragment();
                case 2:
                    return new TimerFragment();
            }
            return null;
        }

        // overriding getPageTitle()
        @Override
        public CharSequence getPageTitle(int position) {
            String[] tabTitles = {getString(R.string.clock),
            getString(R.string.stopwatch), getString(R.string.timer)};
            return tabTitles[position];
        }

        @Override
        public int getCount() {
            return 3;
        }
    }
}
