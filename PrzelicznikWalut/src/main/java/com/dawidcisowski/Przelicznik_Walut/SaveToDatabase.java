package com.dawidcisowski.Przelicznik_Walut;

import android.content.Context;
import android.util.Log;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.List;

public class SaveToDatabase {
    private List<JsonParser.Position> positions;
    private JsonParser jsonParser;
    private DbHelper dbHelper;
    private String jsonString;
    private Context context;

    public SaveToDatabase(Context context){
        this.context=context;
    }
    public void initial(){
        jsonParser = new JsonParser();
        jsonString = null;
        dbHelper =new DbHelper(context);
        dbHelper.createDataBase();
        dbHelper.openDataBase();
    }

    public void update() {
            for (JsonParser.Position p : positions) {
                dbHelper.updateCurrency(p);
            }
    }


    public void close()
    {
        dbHelper.close();
    }


    public void check() throws IOException, XmlPullParserException {
        try {
            jsonString = DownloadCode.downloadUrl("http://www.mycurrency.net/service/rates");
       //     Log.d("code",stream);
        }catch (Exception e){
            Log.d("SaveToDatabase","fault download data");
        }
        try {
          positions= jsonParser.parse(jsonString);
        }catch (Exception e){
            Log.d("SaveToDatabase","fault parse data");
        }

    }



}

