package it.mmzitarosa.beerbox.util;

import android.util.Log;

import androidx.annotation.NonNull;

public class Logger {

    private final static String TAG = "BeerBox"; // can't retrieve from resources without context

    private final static int DEFAULT = 2;

    public static void i(@NonNull String log) {
        if (Util.isLoggable())
            Log.i(getTag(DEFAULT), log);
    }

    public static void e(@NonNull String log) {
        if (Util.isLoggable())
            Log.e(getTag(DEFAULT), log);
    }

    public static void e(@NonNull String log, @NonNull Exception e) {
        if (Util.isLoggable()) {
            Log.e(getTag(DEFAULT), log + " : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void e(@NonNull Exception e) {
        if (Util.isLoggable()) {
            Log.i(getTag(DEFAULT), e.getMessage());
            e.printStackTrace();
        }
    }

    private static String getTag(int i) {
        String className = new Exception().getStackTrace()[i].getClassName();
        return TAG + "::" + className.substring(1 + className.lastIndexOf('.'));
    }

}
