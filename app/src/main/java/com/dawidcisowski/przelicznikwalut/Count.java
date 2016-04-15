package com.dawidcisowski.przelicznikwalut;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public  class Count {

    public static String convert(double rate_in,double rate_out,double value)
    {
        double x= 0;
        try {
            x = rate_in*value/rate_out;
        } catch (Exception e) {

        }
        NumberFormat format=new DecimalFormat("#.##");
        String text=format.format(x);

        Log.d("dad",text);
        return text;
    }


}

