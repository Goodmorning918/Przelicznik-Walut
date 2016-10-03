package com.dawidcisowski.przelicznikwalut;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;



import java.util.ArrayList;
import java.util.List;

public class ListViewAllCurrency extends AppCompatActivity {

    private ListView listView;
    private Cursor currencyCursor;
    private List<CurrencyDescription> currencyDescriptions;
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        listView = (ListView) findViewById(R.id.listViewCurrency);

        fillListViewData();

    }

    private void fillListViewData() {
        dbHelper = new DbHelper(getApplicationContext());
        dbHelper.openDataBase();
        getAllCurrency();
        ListViewAdapter listViewAdapter = new ListViewAdapter(this, currencyDescriptions);
        listView.setAdapter(listViewAdapter);
    }
    private void getAllCurrency() {
        currencyDescriptions = new ArrayList<CurrencyDescription>();
        currencyCursor = getAllEntriesFromDb();
        updateCurrencyList();
    }

    private Cursor getAllEntriesFromDb() {
        currencyCursor = dbHelper.getAllCurrency();
        if (currencyCursor != null) {
            currencyCursor.moveToFirst();
        }
        return currencyCursor;
    }

    private void updateCurrencyList() {
        if (currencyCursor != null && currencyCursor.moveToFirst()) {
            do {
                String name = currencyCursor.getString(0);
                String code = currencyCursor.getString(1);
                String country = currencyCursor.getString(2);
                Double rate = 1/currencyCursor.getDouble(3);
                rate=Double.parseDouble(Count.round(rate,".000"));
                currencyDescriptions.add(new CurrencyDescription(name,code,country,rate));
            } while ((currencyCursor.moveToNext()));
        }
    }

    @Override
    protected void onDestroy() {
        if (dbHelper != null) {
            dbHelper.close();
        }
        super.onDestroy();
    }
}

