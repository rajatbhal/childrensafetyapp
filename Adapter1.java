package com.example.locationtracker;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class Adapter1 extends ArrayAdapter<String> {

    @SuppressWarnings("unchecked")
	public Adapter1(Context context, int resID, @SuppressWarnings("rawtypes") List items) {
        super(context, resID, items);                       
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);
        v.setBackgroundColor(Color.argb(220, 74,121, 181));
       
       // if (position == 1) {
            ((TextView) v).setTextColor(Color.GREEN); 
       // }s
        return v;
    }

}