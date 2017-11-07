package com.example.emmalady.note.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.support.v4.view.GravityCompat;

import java.text.SimpleDateFormat;

/**
 * Created by Liz Nguyen on 02/11/2017.
 */

public class DateFormatUtils {
    @SuppressLint({"SimpleDateFormat"})
    //SimpleDateFormat
    public static final SimpleDateFormat DAY_OF_WEEK = new SimpleDateFormat("EEEE");
    public static final SimpleDateFormat DATE = new SimpleDateFormat("dd/MM/yyyy");
    public static final SimpleDateFormat DATE_LONG = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static final SimpleDateFormat DATE_HOUR = new SimpleDateFormat("dd/MM/yyyy HH:mm");
    public static final SimpleDateFormat DATE_SHORT = new SimpleDateFormat("dd/MM HH:mm");
    public static final SimpleDateFormat DATE_PICTURE = new SimpleDateFormat("yyyyMMdd_HHmmss");
    public static final SimpleDateFormat HOUR = new SimpleDateFormat("HH:mm");
}
