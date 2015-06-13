package com.mobilplug.spotifystreamer;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;

/**
 * Created by setico on 04/06/15.
 */
public class Utils {
    public static String LOG_TAG = Utils.class.getSimpleName();

    public static String getCountryCode(Context context) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        return prefs.getString(context.getString(R.string.pref_country_key),
                context.getString(R.string.pref_country_default));
    }


    public static boolean checkNetworkState(Context context) {
        boolean isOnWifi=false;
        boolean is3G=false;
        ConnectivityManager connManager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo m3G = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

        if (mWifi!=null)
            isOnWifi = mWifi.isConnected();
        if (m3G!=null)
            is3G = m3G.isConnected();

        if(isOnWifi == true || is3G == true) {
            return true;
        }

        return false;
    }



}
