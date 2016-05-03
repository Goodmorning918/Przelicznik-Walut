package com.dawidcisowski.przelicznikwalut;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class SaveToDatabase {
    private List<NbpParser.Position> positions;
    private NbpParser parserNbpParser;
    private BaseAdapter baseAdapter;
    private InputStream stream;
    private Context context;

    public SaveToDatabase(Context context){
        this.context=context;
    }
    public void initial(){
        parserNbpParser = new NbpParser();
        stream = null;
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
        }else {
            for (NbpParser.Position p : positions) {
                baseAdapter.updateCurrency(new CurrencyDescription(i, p.name, Integer.parseInt(p.conversion), p.code, Double.parseDouble(p.averagePrice.replace(',', '.'))));
                i++;
            }
        }
    }

    public void close()
    {
        baseAdapter.close();
    }


    public void check() throws IOException, XmlPullParserException {
        try {
            stream = DownloadCode.downloadUrl("http://www.nbp.pl/kursy/xml/LastA.xml");
        }catch (Exception e){
            Log.d("SaveToDatabase","fault download data");
        }
        try {
            positions = parserNbpParser.parse(stream);
        }catch (Exception e){
            Log.d("SaveToDatabase","fault parse data");
        }

    }



}

