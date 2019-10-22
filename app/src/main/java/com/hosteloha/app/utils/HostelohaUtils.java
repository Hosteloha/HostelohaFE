package com.hosteloha.app.utils;

import java.util.Calendar;
import java.util.Date;

public class HostelohaUtils {

    public static String getCurrentDateTime(){
        Date currentTime = Calendar.getInstance().getTime();
        return String.valueOf(currentTime);
    }
}
