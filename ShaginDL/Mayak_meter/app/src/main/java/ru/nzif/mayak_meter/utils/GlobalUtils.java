package ru.nzif.mayak_meter.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Alon on 14.03.2015.
 */
public class GlobalUtils {

    // time in miliseconds
    public static final int URL_CONNECTION_TIME_OUT = 15000;
    public static final int INPUT_STREAM_READ_TIME_OUT = 10000;

    private static final String GOOGLE_PLAY_URL = "https://play.google.com/store/apps/details?id=";

    /**
     * Gets json response from remote server by url
     */
    public static JSONObject getJsonWithUrl(String url) throws IOException, JSONException {

        URL link = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) link.openConnection();
        connection.setReadTimeout(INPUT_STREAM_READ_TIME_OUT);
        connection.setConnectTimeout(URL_CONNECTION_TIME_OUT);
        connection.setRequestMethod("POST");
        connection.setRequestProperty("Content-length", "0");
        connection.setUseCaches(false);
        connection.setAllowUserInteraction(false);
        connection.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line + "\n");
        }
        br.close();
        connection.disconnect();
        String result = sb.toString();
        JSONObject jsonObject = new JSONObject(result);
        return jsonObject;
    }

    public static String readJsonStringFromFile(Context mContext, String fileName) {
        InputStream is = null;
        String json = null;
        try {
            is = mContext.getAssets().open(fileName);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return json;
    }

    public static boolean containsIgnoreCase(String src, String what) {
        final int length = what.length();
        if (length == 0)
            return true; // Empty string is contained

        final char firstLo = Character.toLowerCase(what.charAt(0));
        final char firstUp = Character.toUpperCase(what.charAt(0));

        for (int i = src.length() - length; i >= 0; i--) {
            // Quick check before calling the more expensive regionMatches() method:
            final char ch = src.charAt(i);
            if (ch != firstLo && ch != firstUp)
                continue;

            if (src.regionMatches(true, i, what, 0, length))
                return true;
        }

        return false;
    }

    public static String buildGooglePlayLink(Context mContext) {
        return GOOGLE_PLAY_URL + mContext.getPackageName();
    }

    public static boolean isNetworkConnected(Context mContext) {
        ConnectivityManager cm = (ConnectivityManager) mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni == null) {
            // There are no active networks.
            return false;
        } else
            return true;
    }

    public static void safeAnimate(View view, int duration, Techniques type) {
        if (view != null) {
            YoYo.with(type)
                    .duration(duration)
                    .playOn(view);
        }
    }
}
