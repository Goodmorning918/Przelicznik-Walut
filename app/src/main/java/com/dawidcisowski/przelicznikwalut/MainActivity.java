package com.dawidcisowski.przelicznikwalut;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static com.dawidcisowski.przelicznikwalut.R.string.navigation_drawer_open;


public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener{

    SharedPreferences sharedPreference;

    private Intent intentListView;

    private TextView nameInput;
    private Button codeInputButton;
    private EditText valueInput;



    private TextView nameOutput;
    private Button codeOutputButton;
    private TextView valueOutput;

    private int inputOutput=0;

    private DbHelper dbHelper;

    private String codeInput;
    private String codeOutput;

    private boolean isUpdated=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //sprawdzenie czy aplikacja czy został obrócony ekran
        if(savedInstanceState!=null){
            isUpdated=true;
        }

        Init();

    }

    private void updateTask(){
        new Update(this).execute();

    }
    //metoda do obsługi przeliczania walut
    private void exchange(){
     //   boolean execute=false;
        String value="0";
        Double editTextValue;
        CurrencyDescription currencyDescriptionInput= dbHelper.getCurrency(codeInput);
        CurrencyDescription currencyDescriptionOutput= dbHelper.getCurrency(codeOutput);



        String text=(valueInput.getText().toString());

        try{
            editTextValue=Double.parseDouble(text);
        }catch (Exception e){
            editTextValue=0.00;

        }

        Double x;
        try {
            x = currencyDescriptionInput.getRate();
        } catch (Exception e) {
            x=0.00;
        }
        Double y = 0.00;
        try {
            y = currencyDescriptionOutput.getRate();
        } catch (Exception e) {
            x=0.00;
        }
        if (x!=0&&y!=0&&editTextValue!=0) {
            try {
                value=Count.convert(x, y, editTextValue);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Błąd",Toast.LENGTH_SHORT).show();
            }
        }
        valueOutput.setText(value);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    private void Init(){
        SharedPreferences sharedPreference;

        nameInput=(TextView) findViewById(R.id.nameInput);
        codeInputButton=(Button) findViewById(R.id.codeInput);
        valueInput=(EditText) findViewById(R.id.valueInput);
        nameOutput=(TextView) findViewById(R.id.nameOutput);
        codeOutputButton =(Button) findViewById(R.id.codeOutput);
        valueOutput=(TextView) findViewById(R.id.textViewOutput);
        Button buttonRevert = (Button) findViewById(R.id.buttonRevert);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();


        NavigationView nvDrawer = (NavigationView) findViewById(R.id.nav_view);
        nvDrawer.setNavigationItemSelectedListener(this);

        sharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        boolean update = sharedPreference.getBoolean("sync_switch", true);

        //sprawdzenie czy w ustawieniach jest włączone automatyczne aktualizowanie
        //i czy już nie była aktualizowana baza przy obecynym włączeniu
        if(update &&!isUpdated){
            updateTask();
            isUpdated=true;
        }


        buttonRevert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                change();
            }
        });
        valueInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                exchange();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        dbHelper =new DbHelper(getApplicationContext());
        dbHelper.createDataBase();
        dbHelper.openDataBase();

        //pobranie ostatnio używanych walut
        codeInput =sharedPreference.getString("codeInput","USD");
        codeOutput =sharedPreference.getString("codeOutput","PLN");
        setInput(codeInput);
        setOutput(codeOutput);

        //lisstenery do obsługi wyboru waluty

        codeInputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputOutput = 1;
                startActivityForResult(intentListView, 2);
            }

        });

        codeOutputButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputOutput = 2;
                startActivityForResult(intentListView, 2);

            }
        });

        intentListView= new Intent(this,ListViewActivity.class);


    }

    private  void change() {
        String change;
        change= codeInput;
        codeInput= codeOutput;
        codeOutput =change;
        setInput(codeInput);
        setOutput(codeOutput);
    }
    private void setInput(String code){
        CurrencyDescription currencyDescription= dbHelper.getCurrency(code);

        if (currencyDescription!=null) {
            nameInput.setText(currencyDescription.getName());
            codeInputButton.setText((currencyDescription.getCode()));
            codeInput =code;
        }
        exchange();

    }
    private void setOutput(String code) {
        CurrencyDescription currencyDescription= dbHelper.getCurrency(code);
        if (currencyDescription!=null) {
            nameOutput.setText(currencyDescription.getName());
            codeOutputButton.setText((currencyDescription.getCode()));
            codeOutput=code;
        }
        exchange();

    }

    @Override
    protected  void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String code;
        try {
            code = data.getStringExtra("code");
            if (requestCode == 2 && inputOutput == 1) {
                setInput(code);
                inputOutput = 0;
            }
            if (requestCode == 2 && inputOutput == 2) {
                setOutput(code);
                inputOutput = 0;
            }
        }catch (Exception e){
            Log.d("returning data","fault returning data");
        }
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item){
        int id=item.getItemId();

        if(id==R.id.update){
            updateTask();
        }else if(id==R.id.kursy_walut){
            Intent intent=new Intent(this,ListViewAllCurrency.class);
            startActivity(intent);
        } else if(id==R.id.setings){
            Intent intent=new Intent(this,SettingsActivity.class);
            startActivity(intent);
        }else if(id==R.id.aboutAplication){
            Intent intent=new Intent(this,AboutActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLayout.closeDrawer(GravityCompat.START);
        return  true;

    }


    @Override
    public void onBackPressed(){
        DrawerLayout drawerLayout=(DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else
            super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case  (R.id.action_settings): {
                Intent intent=new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;

            }
        }

        return super.onOptionsItemSelected(item);
    }


    //zapisanie ostatnio używanych walut
    @Override
    public void onDestroy(){
        sharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        SharedPreferences.Editor editor= sharedPreference.edit();
        editor.putString("codeInput",codeInput);
        editor.putString("codeOutput",codeOutput);
        editor.commit();
        super.onDestroy();
    }
}
