package com.example.locationtracker;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

public class MyLocation extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		String latitude = getIntent().getStringExtra("LAT");
		String longitude = getIntent().getStringExtra("LON");
		
	
			String uri = "http://maps.google.com/maps?z=11&t=k&q=loc:"+latitude+" "+longitude;
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
		intent.setComponent((new ComponentName("com.google.android.apps.maps",  
	    "com.google.android.maps.MapsActivity")));
		startActivity(intent);
	}
	
}
