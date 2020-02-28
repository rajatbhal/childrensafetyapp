package com.example.locationtracker;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

public class LocationService extends Service {

	boolean inProcess = false;
	long minTime=10*1000;//for 10 seconds
	 float minDistance=1;//for 1 meter

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		getLocation();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		Log.i("LOC", "on start");
		getLocation();
	}

	String message;

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		Log.i("LOC", "SERVICE stopped");
	}

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	public void getLocation() {

		try {
			LocationManager locationManager;
			String context = Context.LOCATION_SERVICE;
			locationManager = (LocationManager) getSystemService(context);
			Criteria criteria = new Criteria();
			criteria.setAccuracy(Criteria.ACCURACY_FINE);
			criteria.setAltitudeRequired(false);
			criteria.setBearingRequired(false);
			criteria.setCostAllowed(true);
			criteria.setPowerRequirement(Criteria.POWER_LOW);
			String provider = locationManager.getBestProvider(criteria, true);
			// Location location =
			// locationManager.getLastKnownLocation(provider);

			// seconds and meter
			locationManager.requestLocationUpdates(provider,minTime, minDistance,
					locationListener);

		} catch (Exception e) {

		}

	}

	private final LocationListener locationListener = new LocationListener() {
		private SharedPreferences preferences;

		@Override
		public void onLocationChanged(Location location) {

			if (isInternetAvailable()) {
				preferences = PreferenceManager
						.getDefaultSharedPreferences(getApplicationContext());
			
				Double latitude = location.getLatitude();
				Double longitude = location.getLongitude();
			
				
				boolean check_maponUI=preferences.getBoolean("mapOnUI", false);
		
					if(check_maponUI){
						Editor editor=preferences.edit();
						editor.putFloat("latitude_source",  Float.parseFloat( Double.toString(latitude)));
						editor.putFloat("longitude_source", Float.parseFloat( Double.toString(longitude)));
						editor.commit();
						Intent i1 = new Intent(
								"personal_safety.location.update.source_location");						
					
						sendBroadcast(i1);
									
			    	}else{
			    		Editor editor=preferences.edit();
						editor.putString("latitude",  Double.toString(latitude));
						editor.putString("longitude", Double.toString(longitude));
						editor.commit();
			    	}
				
				
			   
			}

		}

		@Override
		public void onProviderDisabled(String provider) {

		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};

	public boolean isInternetAvailable() {
		ConnectivityManager cm = (ConnectivityManager) getApplicationContext()
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();

		return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
	}

	

}
