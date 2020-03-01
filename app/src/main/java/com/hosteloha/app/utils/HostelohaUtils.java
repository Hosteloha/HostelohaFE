package com.hosteloha.app.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class HostelohaUtils {

    public static String AUTHENTICATION_TOKEN = "";

    public static String getCurrentDateTime() {
        Date currentTime = Calendar.getInstance().getTime();
        return String.valueOf(currentTime);
    }

    public static String getCurrentTimePin() {
        SimpleDateFormat sdf = new SimpleDateFormat("HHmm", Locale.getDefault());
        String currentDateandTime = sdf.format(new Date());
        return currentDateandTime;
    }

    public static void setAuthenticationToken(String token) {
        AUTHENTICATION_TOKEN = "Bearer " + token;
    }
}
