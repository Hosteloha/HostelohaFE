package com.hosteloha.app;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;

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

import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import com.hosteloha.R;
import com.hosteloha.app.service.HostelohaService;
import com.hosteloha.app.utils.HostelohaUtils;

public class MainActivity extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private AppBarConfiguration mAppBarConfiguration;
    private HostelohaService mHostelohaService = null;

    private boolean doubleBackToExitPressedOnce = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mHostelohaService = HostelohaUtils.getHostelohaService(getApplicationContext());

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        final DrawerLayout drawer = findViewById(R.id.drawer_layout);
        //To disable the side bar.
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //To disable the above toolbar.
        toolbar.setVisibility(View.GONE);

        final NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_seller, R.id.nav_buyer, R.id.nav_settings,
                R.id.nav_payments, R.id.nav_account, R.id.nav_about)
                .setDrawerLayout(drawer)
                .build();

        final NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);

        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
            @Override
            public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                if (destination.getId() == R.id.nav_seller) {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    toolbar.setVisibility(View.VISIBLE);
                    // To remove the items from the side bar navigation view
                    navigationView.getMenu().findItem(R.id.nav_home).setVisible(false);
//                    navigationView.getMenu().findItem(R.id.nav_buyer).setVisible(false);
                    mAppBarConfiguration.getDrawerLayout().removeView(findViewById(R.id.nav_seller));
                } else if (destination.getId() == R.id.nav_buyer) {
                    drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
                    toolbar.setVisibility(View.VISIBLE);
                    // To remove the items from the side bar navigation view
                    navigationView.getMenu().findItem(R.id.nav_home).setVisible(false);
//                    navigationView.getMenu().findItem(R.id.nav_seller).setVisible(false);
                } else if (destination.getId() == R.id.nav_home) {
                    //Hiding the action when reaching the home screen since it cannot go back.
                    getSupportActionBar().hide();
                }
            }
        });
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration configuration) {
        super.onConfigurationChanged(configuration);
        int currentNightMode = configuration.uiMode & Configuration.UI_MODE_NIGHT_MASK;
        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                HostelohaUtils.showSnackBarNotification(this,"Light Theme enabled");
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                HostelohaUtils.showSnackBarNotification(this,"Dark Theme enabled");
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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
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
}
