package com.dawidcisowski.przelicznikwalut;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

//klasa aktywności z walutami do wyboru
public class ListViewActivity extends AppCompatActivity {

    private ListView listView;
    private Cursor currencyCursor;
    private List<CurrencyDescription> currencyDescriptions;
    private DbHelper dbHelper;
    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        listView = (ListView) findViewById(R.id.listViewCurrency);

        fillListViewData();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // position++;
                TextView txtCode=(TextView) view.findViewById(R.id.code);
                String code=txtCode.getText().toString();
                intent.putExtra("code",code);
                setResult(2, intent);
                finish();//finishing activity

            }
        });
    }

    private void fillListViewData() {
        dbHelper = new DbHelper(getApplicationContext());
        dbHelper.openDataBase();
        getAllTasks();
        ListViewAdapter listViewAdapter = new ListViewAdapter(this, currencyDescriptions);
        listView.setAdapter(listViewAdapter);
    }

    private void getAllTasks() {
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
                Double rate = currencyCursor.getDouble(3);
                rate=Double.parseDouble(Count.round(rate,"##.###"));
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

