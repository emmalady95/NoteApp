package com.example.emmalady.note.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.util.LruCache;
import android.support.v4.view.GravityCompat;

import com.example.emmalady.note.model.Notes;

import java.util.List;

/**
 * Created by Liz Nguyen on 04/11/2017.
 */

public class PhotoFormatUtils {
    //LruCache
    public static LruCache<String, Bitmap> BITMAP_CACHE = new LruCache(4194304);
    public static LruCache<String, Bitmap> BITMAP_LARGE_CACHE = new LruCache(GravityCompat.RELATIVE_LAYOUT_DIRECTION);
    public static List<Notes> tmpNote;

    public static Bitmap getBitmap(String pathPhoto, int width, int height) {
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(pathPhoto, bmOptions);
        int scaleFactor = Math.min(bmOptions.outWidth / width, bmOptions.outHeight / height);

        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;
        bmOptions.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(pathPhoto, bmOptions);
    }
}
