package com.hosteloha.app.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Looper;
import android.widget.Toast;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.hosteloha.app.log.HostelohaLog;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.MutableLiveData;

public class AppLocation {

    private static Context mContext = null;
    private static Activity mActivity = null;
    public static final int REQUEST_CHECK_LOCATION = 44;
    FusedLocationProviderClient mFusedLocationClient = null;

    public AppLocation(Context context, Activity activity) {
        mContext = context;
        mActivity = activity;
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        HostelohaLog.debugOut(" Constructor - context : "+(mContext!=null) +", activity : "+(activity!=null));
    }

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(
                mActivity,
                new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CHECK_LOCATION
        );
    }

    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
                LocationManager.NETWORK_PROVIDER
        );
    }

    public void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                        == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient.getLastLocation().addOnCompleteListener(
                            new OnCompleteListener<Location>() {
                                @Override
                                public void onComplete(@NonNull Task<Location> task) {
                                    Location location = task.getResult();
                                    if (location == null) {
                                        HostelohaLog.debugOut("Location is null");
                                        requestNewLocationData();
                                    } else {
                                        double loc_lat = location.getLatitude();
                                        double loc_long = location.getLongitude();
                                        getLocationAddress(loc_lat, loc_long);
                                        String lat_long = "location :  [Lat,Long] : " + loc_lat + "," + loc_long;
                                        mAddressText.setValue(lat_long);
                                        HostelohaLog.debugOut(lat_long);
                                    }
                                }
                            }
                    );
                }
            } else {
                Toast.makeText(mContext, "Turn on location", Toast.LENGTH_LONG).show();
                showLocationSettingPermissionPopUp();
//                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    private void requestNewLocationData() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(mContext);
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mFusedLocationClient.requestLocationUpdates(
                    createLocationRequest(), mLocationCallback, Looper.myLooper()
            );
        }
    }

    protected LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(0);
        locationRequest.setFastestInterval(0);
        locationRequest.setNumUpdates(1);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
            double loc_lat = mLastLocation.getLatitude();
            double loc_long = mLastLocation.getLongitude();
            getLocationAddress(loc_lat, loc_long);
            String lat_long = "location :  [Lat,Long] : " + loc_lat + "," + loc_long;
            mAddressText.setValue(lat_long);
            HostelohaLog.debugOut(lat_long);
        }
    };

    private void showLocationSettingPermissionPopUp() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(createLocationRequest());
        SettingsClient client = LocationServices.getSettingsClient(mContext);
        builder.setAlwaysShow(true);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(mActivity, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
                HostelohaLog.debugOut(" LocationRequest :: onSuccess");
                getLastLocation();
            }
        });

        task.addOnFailureListener(mActivity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                HostelohaLog.debugOut(" LocationRequest :: onFailure : " + e.getLocalizedMessage());

                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(mActivity,
                                REQUEST_CHECK_LOCATION);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });
    }

    public void getLastKnownLocation(MutableLiveData<String> mText, MutableLiveData<List<Address>> addressList) {
        mAddressText = mText;
        mAddressList = addressList;
        getLastLocation();
    }

    public MutableLiveData<String> mAddressText = null;
    MutableLiveData<List<Address>> mAddressList = null;

    public List<Address> getLocationAddress(double latitude, double longitude) {
        Geocoder geocoder;
        List<Address> addresses = null;
        geocoder = new Geocoder(mContext, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
        } catch (IOException e) {
            e.printStackTrace();
        }
        mAddressList.setValue(addresses);
        return addresses;

    }
}
