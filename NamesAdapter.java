package com.example.locationtracker;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class NamesAdapter extends ArrayAdapter<String> {

	
	
	String strMobile, strName;
	ArrayList<String>  lstNames, lstMobiles;
	private LayoutInflater inflater;
	Context ctx;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public NamesAdapter(Context context, int resID, ArrayList lstNames,
			ArrayList<String> lstMobiles) {
		super(context, resID, lstMobiles);
		
		this.lstNames = lstNames;
		this.lstMobiles = lstMobiles;
		this.ctx = context;
		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	public class Holder {
		
		TextView txtNames;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		// View v= super.getView(position, convertView, parent);

		View rowView = inflater.inflate(R.layout.custum_list, null);
	
		TextView txtNames = (TextView) rowView.findViewById(R.id.tvName);
		TextView tvMobile = (TextView) rowView.findViewById(R.id.tvMobile);
	
		strName = lstNames.get(position).toString();
		strMobile = lstMobiles.get(position).toString();
		txtNames.setText(strName);
	tvMobile.setText(strMobile);
		
		

		return rowView;

	}

	

	

	

}