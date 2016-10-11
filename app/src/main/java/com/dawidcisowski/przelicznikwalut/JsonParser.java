package com.dawidcisowski.przelicznikwalut;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class JsonParser {
    public List<Position> parse(String jsonString){
        String plnValue="0.00";
        List<Position> positionList=new ArrayList<Position>();
        try {
             JSONArray jsonArray=new JSONArray(jsonString);



            //szukanie wartości złotówki
            for(int i=0; i<jsonArray.length();i++){
                  String code="";
                  String rate="";

                  JSONObject jsonObject=jsonArray.getJSONObject(i);

                  code=jsonObject.optString("currency_code".toString());
                  rate=jsonObject.optString("rate".toString());

                  if (code.equals("PLN")) {
                      plnValue=rate;
                  }
            }

            for(int i=0; i<jsonArray.length();i++){
                String code;
                String rate;

                JSONObject jsonObject=jsonArray.getJSONObject(i);

                code=jsonObject.optString("currency_code".toString());
                rate=jsonObject.optString("rate".toString());

                //przeliczanie ze wzglądu na 1 PLN
                Double parseRate=1/(Double.parseDouble(rate)/Double.parseDouble(plnValue));

                Position position=new Position(code,Double.toString(parseRate));

                positionList.add(position);
            }
        } catch (JSONException e) {
            e.printStackTrace();

        }
        return positionList;
     }

    public static class Position {
        public final String code;
        public final String rate;

        private Position(String code, String rate) {
            this.code = code;
            this.rate = rate;
        }
    }

}