package com.dawidcisowski.przelicznikwalut;


import android.util.Log;

import com.dawidcisowski.przelicznikwalut.NbpParser.Position;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class Test {
    List<Position> positions;
    List<CurrencyDescription> currencyDescriptions=new ArrayList<CurrencyDescription>();
    NbpParser parserNbpParser= new NbpParser();

    InputStream stream = null;

    public void check() throws IOException, XmlPullParserException{
        try{
            //stream= downloadUrl("http://www.nbp.pl/kursy/xml/LastA.xml");
            positions=parserNbpParser.parse(stream);
        } finally{
        }
    }

    public void print() {
        //CurrencyDescription cur=  new CurrencyDescription(1,"da",1,"aa",2);
        // currencyDescriptions.add(cur);
        Double f = Double.parseDouble("12.0");
        int i = 1;
        for (Position p : positions) {
            //  String averagePrice=p.AveragePrice.
            currencyDescriptions.add(new CurrencyDescription(i, p.name, Integer.parseInt(p.conversion), p.code, Double.parseDouble(p.AveragePrice.replace(',', '.'))));
            i++;
        }

        for (CurrencyDescription c : currencyDescriptions) {
            Log.d("Po parsowaniu ", Long.toString(c.getId()));
            Log.d("Po parsowaniu ", c.getName());
            Log.d("Po parsowaniu ", Integer.toString(c.getConversion()));
            Log.d("Po parsowaniu ", c.getCode());
            Log.d("Po parsowaniu ", Double.toString(c.getAveragePrice()));
            Log.d("Po parsowaniu", "*******************");
        }
    }
}
