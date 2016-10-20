package com.dawidcisowski.Przelicznik_Walut;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;


public class ListViewAdapter extends ArrayAdapter<CurrencyDescription>{
    private Activity activity;
    private List<CurrencyDescription> currencyDescriptions;

    public ListViewAdapter(Activity activity,List<CurrencyDescription> currencyDescriptions){
        super(activity,R.layout.currency_item,currencyDescriptions);
        this.activity=activity;
        this.currencyDescriptions=currencyDescriptions;
    }

    static class ViewHolder{
        public TextView name;
        public TextView code;
        public TextView country;
        public TextView rate;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder;
        View rowView=convertView;
        if(rowView ==null){
            LayoutInflater layoutInflater =activity.getLayoutInflater();
            rowView= layoutInflater.inflate(R.layout.currency_item,null,true);
            viewHolder=new ViewHolder();

            viewHolder.name=(TextView)    rowView.findViewById(R.id.name);
            viewHolder.code=(TextView)    rowView.findViewById(R.id.code);
            viewHolder.country=(TextView) rowView.findViewById(R.id.country);
            viewHolder.rate =(TextView)   rowView.findViewById(R.id.rate);
            rowView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        CurrencyDescription currencyDescription=currencyDescriptions.get(position);

        viewHolder.name.setText(currencyDescription.getName());
        viewHolder.code.setText(currencyDescription.getCode());
        viewHolder.country.setText(currencyDescription.getCountry());

        //zaokrąglenie do 4 miejsc po przecinku
        viewHolder.rate.setText(String.format("%.4f",currencyDescription.getRate()));

        return  rowView;
    }



}
