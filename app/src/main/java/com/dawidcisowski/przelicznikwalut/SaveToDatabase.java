package com.dawidcisowski.przelicznikwalut;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SaveToDatabase {
    List<NbpParser.Position> positions;
    List<CurrencyDescription> currencyDescriptions = new ArrayList<CurrencyDescription>();
    NbpParser parserNbpParser = new NbpParser();
    BaseAdapter baseAdapter;
    InputStream stream = null;
    Context context;

    public SaveToDatabase(Context context){
        this.context=context;
    }
    public void initial(){
        baseAdapter=new BaseAdapter(context);
        baseAdapter.open();
    }

    public void update() {
        int i=1;
        if (baseAdapter.isTableEmpty(baseAdapter.DB_CURRENCY_TABLE)) {
            for (NbpParser.Position p : positions) {
                baseAdapter.insertCurrency(p);
            }
            baseAdapter.insertCurrency("Złoty",1,"PLN",1.000);
            Log.d("Po parsowaniu ","Utworzona baza");

        }else {
            //baseAdapter.updateCurrency(10,"da",1,"aa",2.8);
            for (NbpParser.Position p : positions) {
                baseAdapter.updateCurrency(new CurrencyDescription(i, p.name, Integer.parseInt(p.conversion), p.code, Double.parseDouble(p.AveragePrice.replace(',', '.'))));
                i++;
            }
            Log.d("Po parsowaniu ","Zaktualizowana baza");
        }
    }
    //currencyDescriptions.add(   new CurrencyDescription(i, p.name, Integer.parseInt(p.conversion), p.code, Double.parseDouble(p.AveragePrice.replace(',', '.'))));

    public void close()
    {
        baseAdapter.close();
    }

    public void showCurrency(){
        CurrencyDescription c;
        c=baseAdapter.getCurrency(9);
      //  Log.d("Wiersz",c.getName());
        Log.d("Po parsowaniu ", Long.toString(c.getId()));
        Log.d("Po parsowaniu ", c.getName());
        Log.d("Po parsowaniu ", Integer.toString(c.getConversion()));
        Log.d("Po parsowaniu ", c.getCode());
        Log.d("Po parsowaniu ", Double.toString(c.getAveragePrice()));
        Log.d("Po parsowaniu", "*******************");
        Log.d("Wersja", Long.toString(baseAdapter.versionBase()));
       // baseAdapter.
    }

    public void check() throws IOException, XmlPullParserException {
        try {
            stream = DownloadCode.downloadUrl("http://www.nbp.pl/kursy/xml/LastA.xml");
            positions = parserNbpParser.parse(stream);
        } finally {
        }
    }

    public void parseToCurrencyDescriptions() {
        int i = 1;
        for (NbpParser.Position p : positions) {
           // currencyDescriptions.add(new CurrencyDescription(i, p.name, Integer.parseInt(p.conversion), p.code, Double.parseDouble(p.AveragePrice.replace(',', '.'))));

            baseAdapter.insertCurrency(p);
            Log.d("SQL", "wiersz:" + i);

            i++;
        }
    }


        /*for (CurrencyDescription c : currencyDescriptions) {
            Log.d("Po parsowaniu ", Long.toString(c.getId()));
            Log.d("Po parsowaniu ", c.getName());
            Log.d("Po parsowaniu ", Integer.toString(c.getConversion()));
            Log.d("Po parsowaniu ", c.getCode());
            Log.d("Po parsowaniu ", Double.toString(c.getAveragePrice()));
            Log.d("Po parsowaniu", "*******************");
        }

    }*/
}

