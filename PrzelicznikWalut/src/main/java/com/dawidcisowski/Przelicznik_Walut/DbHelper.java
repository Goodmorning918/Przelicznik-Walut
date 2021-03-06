package com.dawidcisowski.Przelicznik_Walut;



import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DbHelper extends SQLiteOpenHelper {

    private static String DB_PATCH ;
    private static String DB_NAME="CurrencyDescribe";
    private final String DATABASE_FOLDER = "/databases/";
    private final String DB_APP_PATCH = "/data/data/";
    private final String TABLE_NAME="Currency";

    public final String KEY_CODE ="code";
    public final String KEY_NAME ="name";
    public final String KEY_RATE ="rate";
    public final String KEY_COUNTRY ="country";

    private final String[] columns ={KEY_NAME, KEY_CODE, KEY_COUNTRY, KEY_RATE};


    private SQLiteDatabase db;
    private final Context myContext;


    public DbHelper (Context context){
        super(context,DB_NAME,null,1);
        DB_PATCH =  DB_APP_PATCH + context.getPackageName() + DATABASE_FOLDER;
        this.myContext=context;
    }

    public void createDataBase(){
        boolean dbExist=checkDataBase();

        if(!dbExist){
            this.getReadableDatabase();
            //this.close();
            try {
                copyDataBase();
            }catch (IOException e){
                Log.d("Copy Database","fail with copy");
            }
        }
    }


    //Sprawdzenie czy istnieje  baza
    private boolean checkDataBase() {
        SQLiteDatabase checkDB =null;
        try{
            String myPatch=DB_PATCH+DB_NAME;
            checkDB=SQLiteDatabase.openDatabase(myPatch,null,SQLiteDatabase.OPEN_READONLY);
        }catch (SQLiteException e){
            Log.d("Check Exist Database","db not exist");
        }
        return checkDB != null;

    }



    private void copyDataBase() throws IOException{
        InputStream inputStream=myContext.getAssets().open(DB_NAME);

        String outFileName=DB_PATCH+DB_NAME;


        OutputStream outputStream =new FileOutputStream(outFileName);

        byte []buffer =new byte[1024];
        int length;


        while ((length=inputStream.read(buffer))>0){
            outputStream.write(buffer,0,length);
        }

        outputStream.flush();
        outputStream.close();
        inputStream.close();
    }

    public boolean update(Float code,String averagePrice){

        ContentValues contentValues=new ContentValues();

        contentValues.put("rate",averagePrice);
        return db.update(TABLE_NAME,contentValues,"code="+code,null)>0;

    }


    public  void openDataBase() throws SQLiteException{
        String myPath =DB_PATCH+DB_NAME;
        db =SQLiteDatabase.openDatabase(myPath,null,SQLiteDatabase.OPEN_READWRITE);
    }

   /* public long versionBase(){
        return db.getVersion();
    }*/

    @Override
    public synchronized  void close (){
        if(db !=null){
            db.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean updateCurrency(JsonParser.Position position){
        String code=position.code;

        //zaokrąglenie
        String rate =(Count.round(Double.parseDouble(position.rate),"00.0000")).replace(',','.');

        ContentValues contentValues=new ContentValues();
        contentValues.put(KEY_RATE,rate);

        return db.update(TABLE_NAME,contentValues, KEY_CODE +"='"+code+"'",null)>0;

    }

    public Cursor getAllCurrency(){
               return  db.query(TABLE_NAME, columns,null,null,null,null,null);
        }

    public Cursor getAllCurrency(String sort){
        return  db.query(TABLE_NAME, columns,null,null,null,null,sort);
    }
    public Cursor getAllCurrency(String sort,String search){
        return  db.query(TABLE_NAME, columns,KEY_CODE+" LIKE '%"+search+"%' OR "+KEY_NAME+" LIKE '%"+search+"%' OR "+KEY_COUNTRY+" LIKE '%"+search+"%' ",null,null,null,sort);
    }

    public CurrencyDescription getCurrency(String currencyCode){
        Cursor cursor=db.query(TABLE_NAME, columns,KEY_CODE+"='"+currencyCode+"'",null,null,null,null,null);

        cursor.moveToFirst();
        String name=cursor.getString(0);
        String code=cursor.getString(1);
        String country=cursor.getString(2);
        Double rate=cursor.getDouble(3);

        return (new CurrencyDescription(name,code,country,rate));


    }



}
