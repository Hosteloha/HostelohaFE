package com.hosteloha.app;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.hosteloha.BuildConfig;
import com.hosteloha.R;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.service.HostelohaService;
import com.hosteloha.app.utils.HostelohaUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private AppBarConfiguration mAppBarConfiguration;
    private HostelohaService mHostelohaService = null;

    private boolean doubleBackToExitPressedOnce = false;
    private DrawerLayout drawer = null;
    private Toolbar toolbar = null;
    private NavigationView navigationView = null;
    private NavController navController = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Init resources
        drawer = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);
        navigationView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        mHostelohaService = HostelohaUtils.getHostelohaService(getApplicationContext());
        //To adjust the layout, when keyboard changes
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        //To disable the side bar default for login screen
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        setSupportActionBar(toolbar);
        //To disable the above toolbar.
        toolbar.setVisibility(View.GONE);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(R.id.nav_home)
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        navigationView.setNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        // Set App version
        setAppVersion();
        navController.addOnDestinationChangedListener(mOnDestinationChangedListener);
    }

    private NavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new NavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            int itemID = item.getItemId();
            if (itemID == R.id.nav_logout) {
                HostelohaUtils.logOutUser(MainActivity.this);
            } else if (itemID == R.id.nav_rate) {
                openPlayStoreApplication();
            }
            // To remove the checked as everything return to fragment Home.
            item.setChecked(false);
            item.setCheckable(false);
            //This is for maintaining the behavior of the Navigation view
            NavigationUI.onNavDestinationSelected(item, navController);
            //This is for maintaining the behavior of the Navigation view
//            NavigationUI.setupWithNavController(navigationView, navController);
            //To navigate to Account Fragment OnClick of Header.
            navigateHeaderOnClickToAccountFragment();
            //This is for closing the drawer after acting on it
            closeNavigationDrawer();
            return true;
        }
    };

    private NavController.OnDestinationChangedListener mOnDestinationChangedListener = new NavController.OnDestinationChangedListener() {
        @Override
        public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
            if (destination.getId() == R.id.nav_home) {
                navigationView.getMenu().findItem(R.id.nav_home).setVisible(false);
                //Hiding the action when reaching the home screen since it cannot go back.
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                toolbar.setVisibility(View.VISIBLE);
//                navigationView.setCheckedItem(R.id.nav_home);
//                    getSupportActionBar().hide();
            } else {
                // In other fragments, we close drawer so as to avoid unnecessary fragment back stack
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
        }
    };

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                HostelohaUtils.showSnackBarNotification(this, "Light Theme enabled");
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                HostelohaUtils.showSnackBarNotification(this, "Dark Theme enabled");
                break;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        closeNavigationDrawer();
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }
        this.doubleBackToExitPressedOnce = true;
        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        //Added Snack Bar only for the home screen and the application is going to exit
        if (navController.getCurrentDestination().getId() == R.id.nav_home) {
            View parentLayout = findViewById(android.R.id.content);
            String snackBarExitButtonText = getResources().getString(R.string.snackbar_exit_button);
            String snackBarExitText = getResources().getString(R.string.snackbar_exit_message);
            Snackbar.make(parentLayout, snackBarExitText, Snackbar.LENGTH_LONG)
                    .setAction(snackBarExitButtonText, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            finish();
                            System.exit(0);
                        }
                    })
                    .setActionTextColor(Color.YELLOW)
                    .show();

        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce = false;
            }
        }, 2000);

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_logout:
                HostelohaUtils.logOutUser(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    /**
     * To navigate to the Account Fragment on Click of the header.
     * Close the existing drawer if it os open
     */
    private void navigateHeaderOnClickToAccountFragment() {
        View header = navigationView.getHeaderView(0);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                closeNavigationDrawer();
                if (navController != null) {
                    navController.navigate(R.id.nav_account);
                }
            }
        });
    }

    /**
     * To set the app version at the bottom of the Navigation drawer.
     * So that, we can get trace bugs in that specific version.
     */
    private void setAppVersion() {
        //TODO check if latest version available, then show update option
        String versionName = BuildConfig.VERSION_NAME;
        TextView hostelohaAppVersionText = findViewById(R.id.hosteloha_app_version);
        String formattedSP = getResources().getString(R.string.app_version, versionName);
        hostelohaAppVersionText.setText(formattedSP);
    }

    /**
     * To close the navigation drawer if it open, when back pressed and when navigation fragment.
     */
    private void closeNavigationDrawer() {
        if (drawer != null && drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
    }

    /**
     * To open the playstore application for the user to rate.
     */
    private void openPlayStoreApplication() {
        final String appPackageName = getPackageName(); // getPackageName() from Context or Activity object
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
        } catch (android.content.ActivityNotFoundException anfe) {
            HostelohaUtils.showSnackBarNotification(MainActivity.this,
                    "PlayStore is not installed");
            HostelohaLog.debugOut("Playstore not installed");
        }
    }
}
