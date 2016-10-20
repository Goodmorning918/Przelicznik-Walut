package com.dawidcisowski.Przelicznik_Walut;

import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;

//klasa do przeliczeń

public class Count {

    public static String convert(Double rate_in,Double rate_out,Double value)
    {
        Double x= 0.00;
        try {
            x = rate_in*value/rate_out;
        } catch (Exception e) {
            Log.d("Convert","fault in convert");
        }


        return round(x,"##.##");
    }

    //przybliżanie wyniku
    public static String round(Double number, String pattern){
        NumberFormat format=new DecimalFormat(pattern);

        return format.format(number).replace(',','.');
    }


}


