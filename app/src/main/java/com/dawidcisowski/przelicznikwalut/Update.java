package com.dawidcisowski.przelicznikwalut;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.Calendar;

class Update extends AsyncTask<Void, Void, Void>{
    private Context context;
    private ProgressBar progressBarUpdate;
    private Boolean ifUpdate=true;
    Activity activity;

    private SharedPreferences sharedPreferences;
    public Update(Activity activity){
        super();
        this.activity=activity;
        context=activity.getApplicationContext();
    }

    @Override
    protected  void onPreExecute( ){
        progressBarUpdate=(ProgressBar) activity.findViewById(R.id.progressBarUpdate);
        progressBarUpdate.setVisibility(View.VISIBLE);
        sharedPreferences= PreferenceManager.getDefaultSharedPreferences(context);
    }
    @Override
    protected Void doInBackground(Void... params) {


       SaveToDatabase saveToDatabase=new SaveToDatabase(context);
       try {
          saveToDatabase.initial();
          saveToDatabase.check();
          saveToDatabase.update();
          saveToDatabase.close();


       } catch (Exception e) {
           ifUpdate=false;
        }
        finally {
           saveToDatabase.close();
       }
        return null;
    }
    @Override
    protected  void onPostExecute(Void result){
        progressBarUpdate.setVisibility(View.INVISIBLE);
        if(ifUpdate) {
            Toast.makeText(context,R.string.toastUpdateTrue, Toast.LENGTH_SHORT).show();

            //zapis czasu aktualizacji
            String date=java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.putString("updateTime",date);
            editor.commit();
        }else{
            Toast.makeText(context,R.string.toastUpdateFalse, Toast.LENGTH_SHORT).show();

        }


    }
}

