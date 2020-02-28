package com.example.locationtracker;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {
	private String latitude;
	private String longitude;
	Context context;
	TextView tvLocation, tvTimer;
	
	SharedPreferences preferences;
	private String username;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		tvLocation = (TextView) findViewById(R.id.tvLocation1);
		tvTimer = (TextView) findViewById(R.id.tvTimer);
		
		context = this;
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		
		username = getIntent().getStringExtra("username");
	Intent i=new Intent(getBaseContext(),LocationService.class);
	i.putExtra("userid", username);
	startService(i);
	isStoragePermissionGranted();

	}
	String TAG="TAG";
	public  void isStoragePermissionGranted() {
		if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
				PackageManager.PERMISSION_GRANTED &&
				ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
						PackageManager.PERMISSION_GRANTED
				&&ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) ==
				PackageManager.PERMISSION_GRANTED&&
		ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) ==
				PackageManager.PERMISSION_GRANTED
				) {
			Log.i(TAG, "isStoragePermissionGranted: permission granted");
		} else {
			Log.i(TAG, "isStoragePermissionGranted: permission not granted");
			ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS,Manifest.permission.SEND_SMS,Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.WRITE_CONTACTS},
					1);
		}


	}
	public void logout(View v) {

	
			Intent i1 = new Intent(getBaseContext(),
					LoginActivity.class);
			i1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			startActivity(i1);
		
	}
	
	public void updateProfile(View v){
		Intent i=new Intent(getBaseContext(),UpdateProfile.class);
		i.putExtra("latitude",latitude);
		i.putExtra("longitude",longitude);
		i.putExtra("username",username);
		
		startActivity(i);
	}
public void myProfile(View v){
	Intent i=new Intent(getBaseContext(),Profile.class);
	i.putExtra("latitude",latitude);
	i.putExtra("longitude",longitude);
	i.putExtra("username",username);
	
	startActivity(i);
}
	public void viewContacts(View v) {
		Intent i=new Intent(getBaseContext(),AllContacts.class);
		i.putExtra("latitude",latitude);
		i.putExtra("longitude",longitude);
	
		
		startActivity(i);

	}

	public void emergency(View v) {
	Intent i=new Intent(getBaseContext(),AddEmergencyNo.class);
	i.putExtra("source",latitude+","+longitude);
	startActivity(i);
	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	
		FetchLocation fd = new FetchLocation();
		fd.execute();
	}
	public void editDetails(View v) {
		Intent i = new Intent(getBaseContext(), UpdateProfile.class);
		i.putExtra("username", username);

		startActivity(i);
	}

	public void refresh(View v) {
		FetchLocation fd = new FetchLocation();
		fd.execute();

	}

	private String latlon;
	LocationGetter location;

	class FetchLocation extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			location = new LocationGetter();
			runOnUiThread(new Runnable() {
				public void run() {
					try {
						latitude = Double.toString(location.getLocation(
								MainActivity.this).getLatitude());
						longitude = Double.toString(location.getLocation(
								MainActivity.this).getLongitude());
						Editor editor=preferences.edit();
						editor.putString("latitude",  latitude);
						editor.putString("longitude", longitude);
						editor.commit();
						GetCurrentAddress currentadd = new GetCurrentAddress();
						currentadd.execute();
					} catch (Exception e) {
					}

				}
			});

			/*
			 * latitude= Double.toString(newlat()); longitude=
			 * Double.toString(newlong());
			 */
			return null;
		}

		String address1 = "";

		private class GetCurrentAddress extends AsyncTask<String, Void, String> {

			@Override
			protected String doInBackground(String... urls) {
				// this lat and log we can get from current location but here we
				// given hard coded

				System.out.println("lati=" + latitude + " longi=" + longitude);

				String address = getAddress(context,
						Double.parseDouble(latitude),
						Double.parseDouble(longitude));
				System.out.println("address=" + address);

				return address;
			}

			@Override
			protected void onPostExecute(String resultString) {
				// dialog.dismiss();
				address1 = address1 + resultString;
				tvLocation.setText(address1 + latlon);

			}
		}

		public String getAddress(Context ctx, double latitude, double longitude) {
			StringBuilder result = new StringBuilder();
			try {
				Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());
				List<Address> addresses = geocoder.getFromLocation(latitude,
						longitude, 1);
				if (addresses.size() > 0) {
					Address address = addresses.get(0);

					String locality = address.getLocality();
					String region_code = address.getCountryCode();
					result.append("My Current Address is:"
							+ address.getAddressLine(0) + " "
							+ address.getAddressLine(1) + " "
							+ address.getPostalCode() + " ");
					result.append(locality + " ");

					result.append(region_code);

				}
			} catch (IOException e) {
				Log.e("tag", e.getMessage());
			}

			return result.toString();
		}

		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			runOnUiThread(new Runnable() {

				public void run() {
					if (latitude == null || longitude == null) {

					} else {
						Editor editor=preferences.edit();
						editor.putString("latitude",  latitude);
						editor.putString("longitude", longitude);
						editor.commit();
						Toast.makeText(
								getBaseContext(),
								"Latitude,Longitude " + latitude + ","
										+ longitude, Toast.LENGTH_SHORT).show();
						latlon = "\nCurrent Location: " + latitude + ","
								+ longitude;
						tvLocation.setText(address1 + latlon);
					}
				}
			});

		}
	}

	public void mylocation(View v) {
		Intent intent = new Intent(getBaseContext(), MyLocation.class);

		intent.putExtra("LAT", latitude);
		intent.putExtra("LON", longitude);
		startActivity(intent);
	}
}
