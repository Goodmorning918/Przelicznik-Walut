package com.dawidcisowski.przelicznikwalut;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.List;

//klasa aktywności z walutami do wyboru
public class ListViewActivity extends AppCompatActivity  {

    private ListView listView;
    private Cursor currencyCursor;
    private List<CurrencyDescription> currencyDescriptions;
    private DbHelper dbHelper;
    private Intent intent = new Intent();

    private Button codeSort;
    private Button nameSort;
    private Button countrySort;

    private EditText searchEditText;

    private String searchText="";

    private String sortText="code";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);

        codeSort=(Button) findViewById(R.id.codeSortButton);
        nameSort=(Button) findViewById(R.id.nameSortButton);
        countrySort=(Button) findViewById(R.id.countrySortButton);

        searchEditText=(EditText) findViewById(R.id.searchEditText);

        listView = (ListView) findViewById(R.id.listViewCurrency);

        currencyDescriptions = new ArrayList<CurrencyDescription>();


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

     /*listenery do sortowania*/
        codeSort.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sortText="code";
                fillListViewData();
            }
        });

        nameSort.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sortText="name";
                fillListViewData();            }
        });

        countrySort.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                sortText="country";
                fillListViewData();            }
        });



        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                searchText=searchEditText.getText().toString();
                fillListViewData();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private void fillListViewData() {
        currencyDescriptions = new ArrayList<CurrencyDescription>();
        dbHelper = new DbHelper(getApplicationContext());
        dbHelper.openDataBase();
        if(searchText.equals("")) {
            currencyCursor = dbHelper.getAllCurrency(sortText);
        }else{
            currencyCursor = dbHelper.getAllCurrency(sortText,searchText);
        }
        updateCurrencyList();
        ListViewAdapter listViewAdapter = new ListViewAdapter(this, currencyDescriptions);
        listView.setAdapter(listViewAdapter);
    }




    private Cursor getAllEntriesFromDb(String sort,String search) {
        currencyCursor = dbHelper.getAllCurrency(sort);
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

