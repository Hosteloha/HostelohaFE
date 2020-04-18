package com.hosteloha.app.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.IBinder;

import com.hosteloha.R;
import com.hosteloha.app.MainActivity;
import com.hosteloha.app.beans.AuthenticationTokenJWT;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.beans.UserAuthentication;
import com.hosteloha.app.data.AllProductsSubject;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.retroapi.ApiUtil;
import com.hosteloha.app.retroapi.CallbackWithRetry;
import com.hosteloha.app.utils.HostelohaUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import retrofit2.Call;
import retrofit2.Response;

public class HostelohaService extends Service {

    private static HostelohaService mHostelohaService = null;
    private String mDefaultUserJWT = null;

    public static HostelohaService getService() {
        return mHostelohaService;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mHostelohaService = this;
        HostelohaLog.debugOut("  service onCreate");
        req_authenticateDefaultUser();
        getSplashData();
    }

    public void testNotify() {
        HostelohaLog.debugOut(" test Notify called :: ");
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // The id of the channel.
        String id = "my_channel_01";
        // The user-visible name of the channel.
        CharSequence name = getString(R.string.channel_name);
        // The user-visible description of the channel.
        String description = getString(R.string.channel_description);
        int importance = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_LOW;
        }
        NotificationChannel mChannel = null;

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(id, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);


        Notification notification = null;
        notification = new NotificationCompat.Builder(HostelohaService.this, id)
                .setContentTitle("New Message 102")
                .setContentText("You've received new messages.")
                .setSmallIcon(R.drawable.ic_menu_monetization_on)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .build();
        // Issue the notification.
        mNotificationManager.notify(0, notification);

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        HostelohaLog.debugOut("  service onDestroy");
    }

    private void req_authenticateDefaultUser() {
        UserAuthentication defaultUser = new UserAuthentication("testuser@gmail.com", "password");
        ApiUtil.getServiceClass().getAuthenticationToken(defaultUser).enqueue(new CallbackWithRetry<AuthenticationTokenJWT>() {
            @Override
            public void onResponse(Call<AuthenticationTokenJWT> call, Response<AuthenticationTokenJWT> response) {
                HostelohaLog.debugOut("[REQ] getAuthenticationToken  =====> isSuccessful  : " + response.isSuccessful());
                if (response.isSuccessful()) {
                    AuthenticationTokenJWT authenticationTokenJWT = response.body();
                    mDefaultUserJWT = "Bearer " + authenticationTokenJWT.getJwt();
                    getSplashData();
                    req_getAllProducts();
                }
            }

            @Override
            public void onFailure(Call<AuthenticationTokenJWT> call, Throwable throwable) {
                super.onFailure(call, throwable);
                HostelohaLog.debugOut("[REQ] getAuthenticationToken ===>  onFailure");
            }
        });
    }

    public void getSplashData() {
        req_getCategoryMapList();
    }

    public void req_getCategoryMapList() {
        ApiUtil.getServiceClass().getCategoryMapList(mDefaultUserJWT).enqueue(new CallbackWithRetry<Map<String, Set<String>>>() {
            @Override
            public void onResponse(Call<Map<String, Set<String>>> call, Response<Map<String, Set<String>>> response) {
                HostelohaLog.debugOut("[REQ] getCategoryMapList  =====> isSuccessful  : " + response.isSuccessful());
                if (response.isSuccessful()) {
                    Map<String, Set<String>> categoriesMap = response.body();
                    if (categoriesMap != null)
                        HostelohaUtils.setAllCategoriesMap(categoriesMap);
                }
            }

            @Override
            public void onFailure(Call<Map<String, Set<String>>> call, Throwable throwable) {
                super.onFailure(call, throwable);
                HostelohaLog.debugOut("[REQ] getCategoryMapList ===>  onFailure");
            }
        });
    }

    public void req_getAllProducts() {
        ApiUtil.getServiceClass().getAllProducts(mDefaultUserJWT).enqueue(new CallbackWithRetry<List<ProductObject>>() {
            @Override
            public void onResponse(Call<List<ProductObject>> call, Response<List<ProductObject>> response) {
                HostelohaLog.debugOut("[REQ] getAllProducts  =====> isSuccessful  : " + response.isSuccessful());
                if (response.isSuccessful()) {
                    List<ProductObject> mArrayList = response.body();
                    HostelohaLog.debugOut("[REQ] products_list size ::  " + mArrayList.size());
                    AllProductsSubject.getAllProductsSubject().setProductsList(mArrayList);
                }
            }

            @Override
            public void onFailure(Call<List<ProductObject>> call, Throwable throwable) {
                HostelohaLog.debugOut("[REQ] getAllProducts ===>  onFailure");
                super.onFailure(call, throwable);
            }
        });
    }
}