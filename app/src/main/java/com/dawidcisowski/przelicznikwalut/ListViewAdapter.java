package com.dawidcisowski.przelicznikwalut;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;


import java.util.List;

/**
 * Created by Goodmorning on 2016-02-17.
 */
public class ListViewAdapter extends ArrayAdapter<CurrencyDescription>{
    private Activity context;
    private List<CurrencyDescription> currencyDescriptions;

    public ListViewAdapter(Activity context,List<CurrencyDescription> currencyDescriptions){
        super(context,R.layout.currency_item,currencyDescriptions);
        this.context=context;
        this.currencyDescriptions=currencyDescriptions;
    }

    static class ViewHolder{
        public TextView name;
        public TextView code;
        public TextView avaragePrice;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        ViewHolder viewHolder;
        View rowView=convertView;
        if(rowView ==null){
            LayoutInflater layoutInflater =context.getLayoutInflater();
            rowView= layoutInflater.inflate(R.layout.currency_item,null,true);
            viewHolder=new ViewHolder();
            viewHolder.name=(TextView) rowView.findViewById(R.id.name);
            viewHolder.code=(TextView)rowView.findViewById(R.id.code);
            viewHolder.avaragePrice=(TextView) rowView.findViewById(R.id.averagePrice);
            rowView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) rowView.getTag();
        }
        CurrencyDescription currencyDescription=currencyDescriptions.get(position);

        viewHolder.name.setText(currencyDescription.getName());
        viewHolder.code.setText(currencyDescription.getCode());
        viewHolder.avaragePrice.setText(Double.toString(currencyDescription.getAveragePrice()/currencyDescription.getConversion()));

        return  rowView;
    }



}
