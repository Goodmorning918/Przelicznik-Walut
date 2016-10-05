package com.dawidcisowski.przelicznikwalut;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public  class DownloadCode {
    public static String downloadUrl(String urlString) throws IOException {
        URL url = new URL(urlString);
        InputStream stream;
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setConnectTimeout(15000);
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.connect();

        //Parsowanie InputStream na String
        stream= conn.getInputStream();
        BufferedReader reader=new BufferedReader((new InputStreamReader(stream,"UTF-8")),8);
        StringBuilder sb=new StringBuilder();
        String line;

        while((line=reader.readLine())!=null){
            sb.append(line+"\n");
        }

        return sb.toString();
    }
}
