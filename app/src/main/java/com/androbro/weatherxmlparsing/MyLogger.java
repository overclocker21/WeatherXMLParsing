package com.androbro.weatherxmlparsing;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by user on 2/3/2016.
 */
public class MyLogger {
    public static void m(String message){
        Log.d("SET", message);
    }

    public static void s(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
