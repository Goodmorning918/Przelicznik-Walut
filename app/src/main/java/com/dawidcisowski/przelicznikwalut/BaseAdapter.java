package com.dawidcisowski.przelicznikwalut;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class BaseAdapter  {/*


    private static final String DEBUG_TAG ="BaMenager";

    private static final int DB_VERSION =1;
    private static final String DB_NAME ="database.db";
    public static final String DB_CURRENCY_TABLE ="CURRENCY";

    public static final String KEY_ID ="id";
    public static final String ID_OPTIONS ="INTEGER PRIMARY KEY AUTOINCREMENT";
    public static final int ID_COLUMN= 0;

    public static final String KEY_NAME ="name";
    public static final String NAME_OPTIONS ="TEXT NOT NULL";
    public static final int NAME_COLUMN =1;

    public static final String KEY_CONVERSION ="conversion";
    public static final String CONVERSION_OPTIONS ="INTEGER DEFAULT 0";
    public static final int CONVERSION_COLUMN=2;

    public static final String KEY_CODE ="code";
    public static final String CODE_OPTIONS ="TEXT NOT NULL";
    public static final int CODE_COLUMN =3;

    public static final String 	KEY_AVERAGE_PRICE="rate";
    public static final String AVERAGE_PRICE_OPTIONS ="REAL DEFAULT 0";
    public static final int AVERAGE_PRICE__COLUMN =4;

    private static final String DB_CREATE_CURRENCY_TABLE=
            "CREATE TABLE "+DB_CURRENCY_TABLE +" ( "+
                    KEY_ID +" "+ ID_OPTIONS +", " +
                    KEY_NAME +" " +NAME_OPTIONS +", " +
                    KEY_CONVERSION +" " +CONVERSION_OPTIONS +", "+
                    KEY_CODE +" "+ CODE_OPTIONS +", "+
                    KEY_AVERAGE_PRICE +" " +AVERAGE_PRICE_OPTIONS+");";

    private static final String DROP_CURRENCY_TABLE =
            "DROP TABLE IF EXISTS " +DB_CURRENCY_TABLE;

    private SQLiteDatabase db;
    private Context context;
    private DatabaseHelper  dbHelper;

    private static class DatabaseHelper  extends SQLiteOpenHelper{

        public DatabaseHelper(Context context, String name,
                              CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            Log.d(DEBUG_TAG, "Before Table Creating...");
            db.execSQL(DB_CREATE_CURRENCY_TABLE);
            Log.d(DEBUG_TAG, "Table Creating...");
            Log.d(DEBUG_TAG, "Table"+ DB_CURRENCY_TABLE +"ver" +DB_VERSION +"Created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(DROP_CURRENCY_TABLE);
            Log.d(DEBUG_TAG, "Database updating...");
            Log.d(DEBUG_TAG, "Table" +DB_CREATE_CURRENCY_TABLE +"updated from ver" + oldVersion +"to" +newVersion);
            Log.d(DEBUG_TAG, "All data is lost...");

            onCreate(db);

        }

    }
    public BaseAdapter (Context context){
        this.context =context;
    }

    public BaseAdapter open(){
        dbHelper =new DatabaseHelper(context, DB_NAME, null, DB_VERSION);
        try {
            db =dbHelper.getWritableDatabase();
        } catch (SQLException e){
            db =dbHelper.getReadableDatabase();
        }
        return this;
    }
    public void close(){
        dbHelper.close();
    }

    /*public long insertCurrency(String name, int conversion, String code, double avaragePrice ){
        ContentValues newCurrencyValues = new ContentValues();
        newCurrencyValues.put(KEY_NAME, name);
        newCurrencyValues.put(KEY_CONVERSION, conversion);
        newCurrencyValues.put(KEY_CODE, code);
        newCurrencyValues.put(KEY_AVERAGE_PRICE, avaragePrice);
        return db.insert(DB_CURRENCY_TABLE, null, newCurrencyValues);
    }
    public long insertCurrency(JsonParser.Position position){
        ContentValues newCurrencyValues = new ContentValues();
        newCurrencyValues.put(KEY_NAME, position.name);
        newCurrencyValues.put(KEY_CONVERSION, Integer.parseInt(position.conversion));
        newCurrencyValues.put(KEY_CODE, position.code);
        newCurrencyValues.put(KEY_AVERAGE_PRICE,  Double.parseDouble(position.rate.replace(',', '.')));
        return db.insert(DB_CURRENCY_TABLE, null, newCurrencyValues);
    }*/

  /*  public boolean updateCurrency(CurrencyDescription cuarrencyDesctiption){
        long id=cuarrencyDesctiption.getId();
        String name=cuarrencyDesctiption.getName();
        int conversion=cuarrencyDesctiption.getConversion();
        String code=cuarrencyDesctiption.getCode();
        double AveragePrice=cuarrencyDesctiption.getRate();

        return updateCurrency(id,name,conversion,code,AveragePrice);
    }

    public boolean updateCurrency(long id, String name, int conversion,
                                   String code, double averagePrice) {
        String where =KEY_ID +"=" +id;
        ContentValues currencyValues =new ContentValues();

        currencyValues.put(KEY_NAME, name);
        currencyValues.put(KEY_CONVERSION,conversion);
        currencyValues.put(KEY_CODE, code);
        currencyValues.put(KEY_AVERAGE_PRICE, averagePrice);
        return db.update(DB_CURRENCY_TABLE, currencyValues, where, null)>0;
    }

    public boolean deleteCurrency(long id){
        String where= KEY_ID+"="+id;
        return db.delete(DB_CURRENCY_TABLE, where, null)>0;
    }

    public Cursor getAllCurrency(){
        String[] columns={KEY_ID,
                KEY_NAME,
                KEY_CONVERSION,
                KEY_CODE,
                KEY_AVERAGE_PRICE};
        return db.query(DB_CURRENCY_TABLE, columns, null, null, null, null, null);
    }

    public CurrencyDescription getCurrency(long id){
        String[] columns={KEY_ID,
                KEY_NAME,
                KEY_CONVERSION,
                KEY_CODE,
                KEY_AVERAGE_PRICE};
        String where=KEY_ID+"="+ id;
        Cursor cursor=db.query(DB_CURRENCY_TABLE, columns, where, null, null, null, null);
        CurrencyDescription currency= null;
        if(cursor !=null &&  cursor.moveToFirst()){
            String name=cursor.getString(NAME_COLUMN);
            int conversion=cursor.getInt(CONVERSION_COLUMN);
            String code=cursor.getString(CODE_COLUMN);
            double averagePrice=cursor.getFloat(AVERAGE_PRICE__COLUMN);
            currency=new CurrencyDescription(id, name, conversion, code, averagePrice);
            cursor.close();
        }
        return currency;
    }
    public boolean isTableEmpty(String table){
        Cursor cursor=db.rawQuery("SELECT count(*) FROM "+table,null);
        cursor.moveToFirst();
        int count=cursor.getInt(0);
        cursor.close();
        return count <= 0;
    }
    public long versionBase(){
        return db.getVersion();
    }

*/

}



