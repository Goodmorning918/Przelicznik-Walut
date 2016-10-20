package com.dawidcisowski.Przelicznik_Walut;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NbpParser {

    public List<Position> parse(InputStream inputStream) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(inputStream, null);
            parser.nextTag();
            return readFeed(parser);
        }
        finally {
            inputStream.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List Position = new ArrayList<Position>();

        parser.require(XmlPullParser.START_TAG, null, "tabela_kursow");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("pozycja")) {
                Position.add(readPosition(parser));
            } else {
                skip(parser);
            }
        }
        return Position;
    }

    public static class Position {
        public final String name;
        public final String conversion;
        public final String code;
        public final String averagePrice;

        private Position(String name,String conversion, String code, String averagePrice) {
            this.name = name;
            this.conversion=conversion;
            this.code = code;
            this.averagePrice = averagePrice;
        }
    }

    private Position readPosition(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG,null,"pozycja");
        String name=null;
        String conversion= null;
        String code=null;
        String averagePrice=null;
        while(parser.next()!=XmlPullParser.END_TAG){
            if(parser.getEventType()!=XmlPullParser.START_TAG){
                continue;
            }
            String checkName = parser.getName();
            if (checkName.equals("nazwa_waluty")) {
                name = readData(parser,"nazwa_waluty");
            } else if (checkName.equals("przelicznik")){
                conversion=readData(parser,"przelicznik");
            } else if (checkName.equals("kod_waluty")) {
                code = readData(parser,"kod_waluty");
            } else if (checkName.equals("kurs_sredni")) {
                averagePrice = readData(parser,"kurs_sredni");
            } else {
                skip(parser);
            }
        }
        return new Position(name,conversion, code, averagePrice);
    }


    private String readData(XmlPullParser parser,String tag) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, null,tag);
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, null,tag);
        return name;
    }


    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }




}