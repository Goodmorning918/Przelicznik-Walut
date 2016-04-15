package com.dawidcisowski.przelicznikwalut;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;



import java.util.ArrayList;
import java.util.List;

public class ListViewAllCurrency extends AppCompatActivity {

    private ListView listView;
    private ListViewAdapter listViewAdapter;
    private Cursor currencyCursor;
    private List<CurrencyDescription> currencyDescriptions;
    private BaseAdapter baseAdapter;
    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        listView = (ListView) findViewById(R.id.listViewCurrency);

        fillListViewData();
        // initListViewOnItemClick();

    }

    private void fillListViewData() {
        baseAdapter = new BaseAdapter(getApplicationContext());
        baseAdapter.open();
        getAllTasks();
        listViewAdapter = new ListViewAdapter(this, currencyDescriptions);
        listView.setAdapter(listViewAdapter);
    }

    private void getAllTasks() {
        currencyDescriptions = new ArrayList<CurrencyDescription>();
        currencyCursor = getAllEntriesFromDb();
        updateCurrencyList();
    }

    private Cursor getAllEntriesFromDb() {
        currencyCursor = baseAdapter.getAllCurrency();
        if (currencyCursor != null) {
            // this.startManagingCursor();
            currencyCursor.moveToFirst();
        }
        return currencyCursor;
    }

    private void updateCurrencyList() {
        if (currencyCursor != null && currencyCursor.moveToFirst()) {
            do {
                long id = currencyCursor.getLong(baseAdapter.ID_COLUMN);
                String name = currencyCursor.getString(baseAdapter.NAME_COLUMN);
                int conversion = currencyCursor.getInt(baseAdapter.CONVERSION_COLUMN);
                String code = currencyCursor.getString(baseAdapter.CODE_COLUMN);
                double averagePrice = currencyCursor.getDouble(baseAdapter.AVERAGE_PRICE__COLUMN);
                currencyDescriptions.add(new CurrencyDescription(id, name, conversion, code, averagePrice));
            } while ((currencyCursor.moveToNext()));
        }
    }

    @Override
    protected void onDestroy() {
        if (baseAdapter != null) {
            baseAdapter.close();
        }
        super.onDestroy();
    }
}

