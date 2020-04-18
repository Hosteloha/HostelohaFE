package com.hosteloha.app.service;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.IBinder;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hosteloha.R;
import com.hosteloha.app.MainActivity;
import com.hosteloha.app.beans.AuthenticationTokenJWT;
import com.hosteloha.app.beans.ProductImageUpload;
import com.hosteloha.app.beans.ProductObject;
import com.hosteloha.app.beans.UserAuthentication;
import com.hosteloha.app.data.AllProductsSubject;
import com.hosteloha.app.log.HostelohaLog;
import com.hosteloha.app.retroapi.ApiUtil;
import com.hosteloha.app.retroapi.CallbackWithRetry;
import com.hosteloha.app.utils.Define;
import com.hosteloha.app.utils.HostelohaUtils;

import java.util.List;
import java.util.Map;
import java.util.Set;

import androidx.annotation.NonNull;
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

    private NotificationManager mNotificationManager = null;
    private NotificationCompat.Builder mNotificationBuilder = null;

    public void createFileProgressNotification() {
        HostelohaLog.debugOut(" notifyFileUploadProgress :: ");
        mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        // The id of the channel.
        String channelID = "my_channel_01";
        // The user-visible name of the channel.
        CharSequence name = getString(R.string.channel_name);
        // The user-visible description of the channel.
        String description = getString(R.string.channel_description);

        NotificationChannel mChannel = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(channelID, name, NotificationManager.IMPORTANCE_DEFAULT);
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

        mNotificationBuilder = new NotificationCompat.Builder(HostelohaService.this, channelID);
        mNotificationBuilder.setContentTitle("Product images uploading")
                .setContentText("in progress")
                .setSmallIcon(R.drawable.ic_cloud_upload)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setProgress(100, 0, true)
                .setOngoing(true)
                .build();
        // Issue the notification.
        mNotificationManager.notify(Define.NOTIFICATION_PRODUCT_IMAGE, mNotificationBuilder.build());
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

    public void uploadFileToFireBaseStorage(List<Uri> filesURIList, int productID) {
        int totalFilesToUpload = filesURIList.size();
        for (int i = 0; i < totalFilesToUpload; i++) {
            final String filesStatus = " [" + (i + 1) + "/" + totalFilesToUpload + "] ";
            Uri fileURI = filesURIList.get(i);
            if (fileURI != null) {
                Uri file = fileURI;
                String fileExtension = HostelohaUtils.getFileExtension(fileURI, getBaseContext());
                final String fileName = System.currentTimeMillis() + "." + fileExtension;
                StorageReference storageReference = HostelohaUtils.getFirebaseStorage().child(Define.STORAGE_PATH_UPLOADS + fileName);
                HostelohaLog.debugOut("[FILES] START UPLOAD " + filesStatus + " :: " + fileName);
                // Create if notification manager is null
                if (mNotificationManager == null) {
                    createFileProgressNotification();
                }

                storageReference.putFile(file)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                // Get a URL to the uploaded content
                                if (taskSnapshot.getMetadata() != null) {
                                    if (taskSnapshot.getMetadata().getReference() != null) {
                                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                            @Override
                                            public void onSuccess(Uri uri) {
                                                String imageUrl = uri.toString();
                                                HostelohaLog.debugOut("uploadFileToFireBaseStorage onSuccess :: fileName : " + fileName + " :: " + imageUrl);
                                                // Notify in the Status Bar.
                                                //createNewPost(imageUrl);
                                                //creating the upload object to store uploaded image details
                                                ProductImageUpload upload = new ProductImageUpload(fileName, imageUrl);
                                                //adding an upload to firebase database
                                                DatabaseReference mDataBaseRef = HostelohaUtils.getFirebaseDatabase();
                                                String uploadId = mDataBaseRef.push().getKey();
                                                mDataBaseRef.child(uploadId).setValue(upload);
                                                // Update notification
                                                mNotificationBuilder.setContentTitle("Product images " + filesStatus + " uploaded")
                                                        .setProgress(100, 100, false)
                                                        .setContentText("success");
                                                mNotificationManager.notify(Define.NOTIFICATION_PRODUCT_IMAGE, mNotificationBuilder.build());
                                            }
                                        });
                                    }
                                }
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception exception) {
                                HostelohaLog.debugOut("uploadFileToFireBaseStorage onFailure :: " + exception.getLocalizedMessage());
                                mNotificationBuilder.setContentTitle("Product images upload")
                                        .setProgress(100, 100, false)
                                        .setContentText("failed");
                                mNotificationManager.notify(Define.NOTIFICATION_PRODUCT_IMAGE, mNotificationBuilder.build());
                            }
                        })
                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                                long transferredBytes = taskSnapshot.getBytesTransferred();
                                long totalBytes = taskSnapshot.getTotalByteCount();
                                double progress = (100.0 * transferredBytes) / totalBytes;
                                long transferredBytesInKB = ((transferredBytes / 1024));
                                long totalBytesInKB = ((totalBytes / 1024));
                                String completion = transferredBytesInKB + "/" + totalBytesInKB + "KB";
                                double formattedProgress = Math.floor(progress * 100) / 100;
                                HostelohaLog.debugOut("uploadFileToFireBaseStorage onProgress :: " + formattedProgress + "-" + completion);
                                mNotificationBuilder.setContentText("Files:" + filesStatus + formattedProgress + "%" + "-" + completion);
                                mNotificationBuilder.setProgress(100, (int) progress, false);
                                mNotificationManager.notify(Define.NOTIFICATION_PRODUCT_IMAGE, mNotificationBuilder.build());
                            }
                        });
            } else {
                HostelohaLog.debugOut("uploadFileToFireBaseStorage URI NULL");
            }
        }
    }
}