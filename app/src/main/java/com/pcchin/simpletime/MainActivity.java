package com.pcchin.simpletime;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.widget.Toast;

import com.pcchin.simpletime.fragment.ClockFragment;
import com.pcchin.simpletime.fragment.StopwatchFragment;
import com.pcchin.simpletime.fragment.TimerFragment;

public class MainActivity extends AppCompatActivity {
    private boolean doubleBackToExitPressedOnce;
    private static final int OVERLAY_REQUEST = 32331;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Check if alert is needed to be displayed
        if (getIntent().getBooleanExtra("showAlert", false)) {
            // Start alarm
            final MediaPlayer mp = MediaPlayer.create(this,
                    RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
            mp.setLooping(true);
            mp.start();

            new AlertDialog.Builder(this)
                    .setTitle(R.string.timer)
                    .setMessage(R.string.times_up)
                    .setPositiveButton(android.R.string.ok, null)
                    .setOnDismissListener(new DialogInterface.OnDismissListener() {
                        @Override
                        public void onDismiss(DialogInterface dialog) {
                            mp.stop();
                            mp.release();
                        }
                    })
                    .create().show();
        }

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        SectionsPagerAdapter pagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        ViewPager viewPager = findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Asks for permission
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && ! Settings.canDrawOverlays(this)) {
            requestPermissions(new String[]{Settings.ACTION_MANAGE_OVERLAY_PERMISSION}, OVERLAY_REQUEST);
        }
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (grantResults.length <= 0 || grantResults[0] != RESULT_OK) {
            Toast.makeText(this, "This permission is needed for the timer to work. " +
                    "Please try again later", Toast.LENGTH_SHORT).show();
        }
    }
}
