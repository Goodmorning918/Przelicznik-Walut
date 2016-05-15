package com.dawidcisowski.przelicznikwalut;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;

//klasa do przelicznia kursu

public class Count {

    public static String convert(double rate_in,double rate_out,double value)
    {
        double x= 0;
        try {
            x = rate_in*value/rate_out;
        } catch (Exception e) {
            Log.d("Convert","fault in convert");
        }

        //przybliżanie wyniku do 2 miejsc po przecinku
        NumberFormat format=new DecimalFormat("#.##");

        return format.format(x);
    }


}

