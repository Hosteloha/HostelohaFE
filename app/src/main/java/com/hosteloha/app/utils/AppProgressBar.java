package com.hosteloha.app.utils;

import android.app.Activity;
import android.app.ProgressDialog;

public class AppProgressBar {
    private static ProgressDialog mProgressDialog = null;

    /**
     * To create the progress bar
     */
    public static void createProgressBar(Activity mActivity) {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(mActivity);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
    }

    public static void showDefaultProgress(Activity mActivity) {
        AppProgressBar.showCustomProgress(mActivity, null, null);
    }

    public static void showCustomProgress(Activity mActivity, String title, String message) {
        createProgressBar(mActivity);
        if (mProgressDialog != null) {
            if (title != null) {
                mProgressDialog.setTitle(title);
            } else {
                mProgressDialog.setTitle("Processing...");
            }
            if (message != null) {
                mProgressDialog.setMessage(message);
            } else {
                mProgressDialog.setMessage("Please wait...");
            }
            mProgressDialog.show();
        }
    }

    public static void hide() {
        dismissProgressBar();
    }

    /**
     * @param mActivity
     * @param dismissMessage
     */
    public static void hideWithSnackBarMessage(Activity mActivity, String dismissMessage) {
        HostelohaUtils.showSnackBarNotification(mActivity, dismissMessage);
        dismissProgressBar();
    }

    /**
     * //TODO :: Yet to develope
     *
     * @param mActivity
     * @param isMessageType  :: Success or Error message, so that we can add colors
     * @param dismissMessage
     */
    public static void hideWithPopUpMessage(Activity mActivity, boolean isMessageType, String dismissMessage) {

    }

    public static void dismissProgressBar() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

}
