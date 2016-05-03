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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.dawidcisowski.przelicznikwalut.R.string.navigation_drawer_open;


public class MainActivity extends AppCompatActivity
    implements NavigationView.OnNavigationItemSelectedListener{

    private Intent intentListView;

    private TextView nameInput;
    private Button codeInput;
    private EditText valueInput;


    private TextView nameOutput;
    private Button codeOutput;
    private TextView valueOutput;

    private int inputOutput=0;

    private BaseAdapter baseAdapter;

    private int positionInput;
    private int positionOutput;

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
        double editTextValue;
        CurrencyDescription currencyDescriptionInput=baseAdapter.getCurrency(positionInput);
        CurrencyDescription currencyDescriptionOutput=baseAdapter.getCurrency(positionOutput);



        String text=(valueInput.getText().toString());

        try{
            editTextValue=Double.parseDouble(text);
        }catch (Exception e){
            editTextValue=0.000;

        }

        double x;
        try {
            x = currencyDescriptionInput.getAveragePrice()/currencyDescriptionInput.getConversion();
        } catch (Exception e) {
            x=0;
        }
        double y = 0;
        try {
            y = currencyDescriptionOutput.getAveragePrice()/currencyDescriptionOutput.getConversion();
        } catch (Exception e) {
            x=0;
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
        codeInput=(Button) findViewById(R.id.codeInput);
        valueInput=(EditText) findViewById(R.id.valueInput);
        nameOutput=(TextView) findViewById(R.id.nameOutput);
        codeOutput=(Button) findViewById(R.id.codeOutput);
        valueOutput=(TextView) findViewById(R.id.textViewOutput);
        ImageButton buttonRevert = (ImageButton) findViewById(R.id.buttonRevert);

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

        baseAdapter=new BaseAdapter(getApplicationContext());
        baseAdapter.open();
        positionInput=2; //pozycja dolara, domyślna
        positionOutput=36;//pozycja złotówki,domyślna
        setInput(2);
        setOutput(36);

        //lisstenery do obsługi wyboru waluty

        codeInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputOutput = 1;
                startActivityForResult(intentListView, 2);
            }

        });

        codeOutput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputOutput = 2;
                startActivityForResult(intentListView, 2);

            }
        });

        intentListView= new Intent(this,ListViewActivity.class);


    }

    private  void change() {
        int change;
        change=positionInput;
        positionInput=positionOutput;
        positionOutput=change;
        setInput(positionInput);
        setOutput(positionOutput);
    }
    private void setInput(int id){
        CurrencyDescription currencyDescription;
        currencyDescription=baseAdapter.getCurrency(id);

        if (currencyDescription!=null) {
            nameInput.setText(currencyDescription.getName());
            codeInput.setText((currencyDescription.getCode()));
            positionInput = id;
        }
        exchange();


    }
    private void setOutput(int id) {
        CurrencyDescription currencyDescription;
        currencyDescription = baseAdapter.getCurrency(id);
        if (currencyDescription!=null) {
            nameOutput.setText(currencyDescription.getName());
            codeOutput.setText((currencyDescription.getCode()));
            positionOutput = id;
        }
        exchange();



    }

    @Override
    protected  void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int position;
        try {
            position = data.getIntExtra("position", 2);
            if (requestCode == 2 && inputOutput == 1) {
                setInput(position);
                inputOutput = 0;
            }
            if (requestCode == 2 && inputOutput == 2) {
                setOutput(position);
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
        }else if(id==R.id.setings){
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
}
