package it.jaschke.alexandria.utils;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Amrendra Kumar on 14/01/16.
 */
public class NetworkUtils {
    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context
                .CONNECTIVITY_SERVICE);

        return cm.getActiveNetworkInfo() != null;
    }
}
