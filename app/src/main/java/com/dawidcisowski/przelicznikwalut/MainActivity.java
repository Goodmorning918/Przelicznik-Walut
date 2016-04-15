package com.dawidcisowski.przelicznikwalut;

import android.app.ActionBar;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
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


public class MainActivity extends AppCompatActivity  {

    private Intent intentListView;

    private TextView nameInput;
    private Button codeInput;
    private EditText valueInput;


    private TextView nameOutput;
    private Button codeOutput;
    private TextView valueOutput;

    private ImageButton buttonRevert;

    private int inputOutput=0;

    private BaseAdapter baseAdapter;

    private CurrencyDescription currencyDescriptionInpout;
    private CurrencyDescription currencyDescriptionOutput;

    private double averagePriceInput;
    private double averagePriceOutput;

    private int conversionInput;
    private int conversionOutput;

    private int positionInput;
    private int positionOutput;

    private boolean update;
    private String color;

    private boolean isUpdated=false;

    SharedPreferences sharedPreference;

    private DrawerLayout mDrawer;
   // private Toolbar toolbar;
    NavigationView nvDrawer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if(savedInstanceState!=null){
            isUpdated=true;
        }


        Init();
       // double x=Double.MAX;
        //Log.d("Ada",Double.toString(x));
    }
    private void setupDrawerContent(NavigationView navigationView){
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.kursy_walut: {
                        Intent intent = new Intent(getApplicationContext(), ListViewAllCurrency.class);
                        startActivity(intent);
                        break;

                    }
                    case R.id.update: {
                        updateTask();
                        // Toast.makeText(getApplicationContext(), R.string.toastUpdate, Toast.LENGTH_SHORT).show();
                        break;

                    }
                    case R.id.setings: {
                        Intent intent = new Intent(getApplicationContext(), SettingsActivity.class);
                        startActivity(intent);
                        break;
                    }
                    case R.id.aboutAplication: {
                        Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
                        startActivity(intent);
                        break;
                    }

                }
                  //YselectDrawerItem(menuItem);
                return true;
            }
        });
    }
    private void updateTask(){
        new Update(this).execute();

    }
    private void exchange(){
        boolean execute=false;
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
        double x= 0;
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
        double w;
        if (x!=0&&y!=0&&editTextValue!=0) {
            try {
                value=Count.convert(x, y, editTextValue);
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(),"Błąd",Toast.LENGTH_SHORT).show();
            }
        }
        /*Log.d("prelicznie", Double.toString(currencyDescriptionInput.getAveragePrice()));
        Log.d("prelicznie", Double.toString(currencyDescriptionOutput.getAveragePrice()));
        Log.d("przelicznie", Double.toString(editTextValue));
        Log.d("przelicznie", value);*/
        valueOutput.setText(value);
    }

    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
    }

    private void Init(){
        nameInput=(TextView) findViewById(R.id.nameInput);
        codeInput=(Button) findViewById(R.id.codeInput);
        valueInput=(EditText) findViewById(R.id.valueInput);
        nameOutput=(TextView) findViewById(R.id.nameOutput);
        codeOutput=(Button) findViewById(R.id.codeOutput);
        valueOutput=(TextView) findViewById(R.id.textViewOutput);
        buttonRevert=(ImageButton) findViewById(R.id.buttonRevert);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.canShowOverflowMenu();
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        //  toolbar.setNavigationIcon(R.mipmap.);
        //actionBar.setDisplayHomeAsUpEnabled(true);
        mDrawer= (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer=(NavigationView) findViewById(R.id.nav_view);
       // setupDrawerContent(nvDrawer);


        sharedPreference= PreferenceManager.getDefaultSharedPreferences(getBaseContext());

        color=sharedPreference.getString("color_list", "1");

       /* switch(color){
            case "0":{
                //getApplicationContext().get
                setTheme(R.style.RedAppTheme);
                //recreate();
            }case "1":{
                setTheme(R.style.GreenAppTheme);
            }case "2":{
                setTheme(R.style.BlueAppTheme);
            }
        }*/

        update=sharedPreference.getBoolean("sync_switch",true);


        if(update&&!isUpdated){
            updateTask();
            isUpdated=true;
 //           Toast.makeText(getApplicationContext(),R.string.toastUpdate,Toast.LENGTH_SHORT).show();
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
        baseAdapter=new BaseAdapter(getApplicationContext());
        baseAdapter.open();
        positionInput=2;
        positionOutput=36;
        setInput(2);
        setOutput(36);

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


       //setInput();
    }

    private  void change() {
        int change=0;
        change=positionInput;
        positionInput=positionOutput;
        positionOutput=change;
        setInput(positionInput);
        setOutput(positionOutput);
    }
    private void setInput(int id){
        CurrencyDescription currencyDescription;
        currencyDescription=baseAdapter.getCurrency(id);

        if (currencyDescription!=null){
            nameInput.setText(currencyDescription.getName());
            codeInput.setText((currencyDescription.getCode()));
            averagePriceInput = currencyDescription.getAveragePrice();
            conversionInput = currencyDescription.getConversion();
            positionInput = id;

        }else{
            conversionInput =1;
        }
        exchange();


    }
    private void setOutput(int id) {
        CurrencyDescription currencyDescription;
        currencyDescription = baseAdapter.getCurrency(id);
        if (currencyDescription!=null) {
            nameOutput.setText(currencyDescription.getName());
            codeOutput.setText((currencyDescription.getCode()));
            averagePriceOutput = currencyDescription.getAveragePrice();
            conversionOutput = currencyDescription.getConversion();
            positionOutput = id;
        }else {
            conversionOutput = 1;
        }
        exchange();


/*        codeOutput.setText((currencyDescription.getCode()));
        averagePriceOutput=currencyDescription.getAveragePrice();
        conversionOutput=currencyDescription.getConversion();
        positionOutput=id;
        exchange();*/

    }

    @Override
    protected  void onActivityResult(int requestCode,int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        int position;
        try {
            position = data.getIntExtra("position", 2);
            Log.d("powrot", Integer.toString(position));
            if (requestCode == 2 && inputOutput == 1) {
                setInput(position);
                inputOutput = 0;
            }
            if (requestCode == 2 && inputOutput == 2) {
                setOutput(position);
                inputOutput = 0;
            }
        }catch (Exception e){

        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }



    public void selectDrawerItem( MenuItem menuItem){
        Fragment fragment=null;
        Class fragmentClass;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()){
            case  (R.id.action_settings): {
                Intent intent=new Intent(this,SettingsActivity.class);
                startActivity(intent);
                return true;

            }case (R.id.home):{
                mDrawer.openDrawer(GravityCompat.START);
                return true;
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
