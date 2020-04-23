package com.hosteloha.app.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.webkit.MimeTypeMap;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.hosteloha.app.log.HostelohaLog;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

/**
 * {@link AppFireStorage} is helpful manage the activities related to the Files Management.
 */
public class AppFireStorage {

    /**
     * To get the file extension while storing data to the FireBase.
     *
     * @param uri
     * @param context
     * @return
     */
    public static String getFileExtension(Uri uri, Context context) {
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    public static long getFileSizeInKB(long fileBytes) {
        return fileBytes / 1024;
    }

    public static long getFileSizeInMB(long fileBytes) {
        return (fileBytes / 1024) / 1024;
    }

    public static double getFileProgress(UploadTask.TaskSnapshot taskSnapshot) {
        long transferredBytes = taskSnapshot.getBytesTransferred();
        long totalBytes = taskSnapshot.getTotalByteCount();
        double progress = (100.0 * transferredBytes) / totalBytes;
        return Math.floor(progress * 100) / 100;
    }

    public enum fileUploadStatus {
        SUCCESS,
        PROGRESS,
        FAILURE
    }

    private static ArrayList<String> generatedURlList = new ArrayList<>();

    /**
     * To upload the product gallery to the server.
     *
     * @param filesURIList list of file URI to upload.
     * @param productID    product ID, to link the file URI
     * @param context
     * @return
     */
    public static ArrayList<String> uploadFileToFirebase(List<Uri> filesURIList, int productID, final Context context) {
        int totalFilesToUpload = filesURIList.size();
        generatedURlList.clear();
        final List<UploadTask> tasks = new ArrayList<>();
        Task<Void> allTask = null;
        for (int i = 0; i < totalFilesToUpload; i++) {
            final String filesStatus = " [" + (i + 1) + "/" + totalFilesToUpload + "] ";
            Uri fileURI = filesURIList.get(i);
            if (fileURI != null) {
                Uri file = fileURI;
                String fileExtension = getFileExtension(fileURI, context);
                final String fileName = System.currentTimeMillis() + "." + fileExtension;
                HostelohaLog.debugOut("[FILES] START UPLOAD " + filesStatus + " :: " + fileName);
                // Create if notification manager is null
                StorageReference storageReference = HostelohaUtils.getFirebaseStorage().child(Define.STORAGE_PATH_UPLOADS + fileName);
                tasks.add(storageReference.putFile(file));

//                storageReference.putFile(file)
//                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                                if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
//                                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
//                                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
//                                        @Override
//                                        public void onSuccess(Uri uri) {
//                                            String imageUrl = uri.toString();
//                                            HostelohaLog.debugOut("uploadFileToFireBaseStorage onSuccess :: fileName : " + fileName + " :: " + imageUrl);
//                                            AppNotificationManager.updateFileUploadNotification(context, fileUploadStatus.SUCCESS, 0, filesStatus, null);
//                                            generatedURlList.add(imageUrl);
//                                        }
//                                    });
//                                }
//
//                            }
//                        })
//                        .addOnFailureListener(new OnFailureListener() {
//                            @Override
//                            public void onFailure(@NonNull Exception exception) {
//                                HostelohaLog.debugOut("uploadFileToFireBaseStorage onFailure :: " + exception.getLocalizedMessage());
//                                AppNotificationManager.updateFileUploadNotification(context, fileUploadStatus.FAILURE, 0, filesStatus, null);
//                            }
//                        })
//                        .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                            @Override
//                            public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                                double progress = getFileProgress(taskSnapshot);
//                                long transferredBytesInKB = getFileSizeInKB(taskSnapshot.getBytesTransferred());
//                                long totalBytesInKB = getFileSizeInKB(taskSnapshot.getTotalByteCount());
//                                String completion = transferredBytesInKB + "/" + totalBytesInKB + "KB";
//                                HostelohaLog.debugOut("uploadFileToFireBaseStorage onProgress :: " + progress + "-" + completion);
//                                AppNotificationManager.updateFileUploadNotification(context, fileUploadStatus.PROGRESS, progress, filesStatus, completion);
//                            }
//                        });
            } else {
                HostelohaLog.debugOut("uploadFileToFireBaseStorage URI NULL");
            }
        }
        HostelohaLog.debugOut("Total tasks added :: " + tasks.size());
        allTask = Tasks.whenAll(tasks);
        // End of for loop
        allTask.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                HostelohaLog.debugOut("onSuccess ALL TASKS FINISHED");
                for (int i = 0; i < tasks.size(); i++) {
                    Task individualTask = tasks.get(i);
                    UploadTask.TaskSnapshot taskSnapshot = (UploadTask.TaskSnapshot) individualTask.getResult();
                    if (taskSnapshot.getMetadata() != null && taskSnapshot.getMetadata().getReference() != null) {
                        Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                        final int finalI = i;
                        result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String imageUrl = uri.toString();
                                HostelohaLog.debugOut("uploadFileToFireBaseStorage onSuccess :: fileName : " + finalI + " :: " + imageUrl);
                            }
                        });
                    }

                }
            }
        });
        allTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                HostelohaLog.debugOut("onSuccess ALL TASKS FAILURE" + e.getLocalizedMessage());
            }
        });
        return generatedURlList;
    }
}
