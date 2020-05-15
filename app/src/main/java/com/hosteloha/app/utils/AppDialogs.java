package com.hosteloha.app.utils;

import android.content.Context;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.hosteloha.R;

import androidx.appcompat.app.AlertDialog;

public class AppDialogs {
    public static void createSimpleAlertDialog(Context context){
        String title = "Title";
        String message = "Message";
        AlertDialog mDialogBuilder = new MaterialAlertDialogBuilder(context)
                .setTitle(title)
                .setMessage(message).show();
    }
}
