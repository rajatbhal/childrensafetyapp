package com.example.locationtracker;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

public class MyMap extends Activity{

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Intent i=getIntent();
		
	    String source=i.getStringExtra("source");
	    String dest=i.getStringExtra("dest");
	
		
		
		String uri = "http://maps.google.com/maps?saddr=" +source+"&daddr="+dest;
		Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
		intent.setComponent((new ComponentName("com.google.android.apps.maps", 
	    "com.google.android.maps.MapsActivity")));
		startActivity(intent);
	}
	

}
