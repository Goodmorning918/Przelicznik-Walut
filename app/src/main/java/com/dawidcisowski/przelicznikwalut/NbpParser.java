package com.dawidcisowski.przelicznikwalut;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class NbpParser {
    private static final String ns = null;

    public List<Position> parse(InputStream in) throws XmlPullParserException, IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private List readFeed(XmlPullParser parser) throws XmlPullParserException, IOException {
        List Position = new ArrayList<Position>();

        parser.require(XmlPullParser.START_TAG, ns, "tabela_kursow");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            if (name.equals("pozycja")) {
                Position.add(readPozycja(parser));
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
        public final String AveragePrice;

        private Position(String name,String conversion, String code, String AveragePrice) {
            this.name = name;
            this.conversion=conversion;
            this.code = code;
            this.AveragePrice = AveragePrice;
        }
    }

    private Position readPozycja(XmlPullParser parser) throws XmlPullParserException, IOException{
        parser.require(XmlPullParser.START_TAG,ns,"pozycja");
        String name=null;
        String conversion= null;
        String code=null;
        String AveragePrice=null;
        while(parser.next()!=XmlPullParser.END_TAG){
            if(parser.getEventType()!=XmlPullParser.START_TAG){
                continue;
            }
            String checkName = parser.getName();
            if (checkName.equals("nazwa_waluty")) {
                name = readName(parser);
            } else if (checkName.equals("przelicznik")){
                conversion=readConversion(parser);
            } else if (checkName.equals("kod_waluty")) {
                code = readCode(parser);
            } else if (checkName.equals("kurs_sredni")) {
                AveragePrice = readAveragePrice(parser);
            } else {
                skip(parser);
            }
        }
        return new Position(name,conversion, code, AveragePrice);
    }

    private String readName(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "nazwa_waluty");
        String name = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "nazwa_waluty");
        return name;
    }

    private String readConversion(XmlPullParser parser) throws IOException,XmlPullParserException{
        parser.require(XmlPullParser.START_TAG, ns, "przelicznik");
        String conversion =readText(parser);
        parser.require(XmlPullParser.END_TAG,ns, "przelicznik");
        return conversion;
    }

    private String readCode(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "kod_waluty");
        String code = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "kod_waluty");
        return code;
    }

    private String readAveragePrice(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "kurs_sredni");
        String AveragePrice = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "kurs_sredni");
        return AveragePrice;
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