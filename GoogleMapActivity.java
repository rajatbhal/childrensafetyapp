package com.example.locationtracker;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class GoogleMapActivity extends FragmentActivity implements OnMapReadyCallback {
	private GoogleMap map;
	LocationBroadcastListenerSource locationupdateSource;
	LocationBroadcastListenerDest locationupdateDest;
	ArrayList<LatLng> markerPoints;
	Intent intent = new Intent();
	String userid;
	private SharedPreferences preferences;
	// String from;
	// ProgressDialog dialog;
	
Context context;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.google_map);
		context=this;
		// Initializing
		markerPoints = new ArrayList<LatLng>();
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		// Getting reference to SupportMapFragment of the activity_main
		SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.map);
		userid = getIntent().getStringExtra("userid");
		

		Log.i("PS", "tracking user=" + userid);
		// from=getIntent().getStringExtra("from");
		// Getting Map for the SupportMapFragment
		fm.getMapAsync(this);


	}

	@Override
	public void onMapReady(GoogleMap googleMap) {
		map=googleMap;
		if (map != null) {

			// Enable MyLocation Button in the Map
			map.setMyLocationEnabled(true);
			map.setTrafficEnabled(true);

			map.getUiSettings().setIndoorLevelPickerEnabled(true);
			map.getUiSettings().setZoomControlsEnabled(true);
			map.getUiSettings().setZoomGesturesEnabled(true);
			locationupdateSource = new LocationBroadcastListenerSource();
			locationupdateDest = new LocationBroadcastListenerDest();
			// Intent intent=new Intent();

			/*
			 * // Setting onclick event listener for the map
			 * map.setOnMapClickListener(new OnMapClickListener() {
			 *
			 * @Override public void onMapClick(LatLng point) {
			 *
			 * // Already two locations if(markerPoints.size()>1){
			 * markerPoints.clear(); map.clear(); }
			 *
			 * // Adding new item to the ArrayList markerPoints.add(point);
			 *
			 * // Creating MarkerOptions MarkerOptions options = new
			 * MarkerOptions();
			 *
			 * // Setting the position of the marker options.position(point);
			 *//**
			 * For the start location, the color of marker is GREEN and for
			 * the end location, the color of marker is RED.
			 */
			/*
			 * if(markerPoints.size()==1){
			 * options.icon(BitmapDescriptorFactory.defaultMarker
			 * (BitmapDescriptorFactory.HUE_GREEN)); }else
			 * if(markerPoints.size()==2){
			 * options.icon(BitmapDescriptorFactory.defaultMarker
			 * (BitmapDescriptorFactory.HUE_RED)); }
			 *
			 * // Add new marker to the Google Map Android API V2
			 * map.addMarker(options);
			 *
			 * // Checks, whether start and end locations are captured
			 * if(markerPoints.size() >= 2){ LatLng origin =
			 * markerPoints.get(0); LatLng dest = markerPoints.get(1);
			 *
			 * // Getting URL to the Google Directions API String url =
			 * getDirectionsUrl(origin, dest);
			 * //dialog=ProgressDialog.show(MainActivity.this, "please wait..",
			 * "Downloading route..",true,true); DownloadTask downloadTask = new
			 * DownloadTask();
			 *
			 * // Start downloading json data from Google Directions API
			 * downloadTask.execute(url); } } });
			 */

		}
	}

	protected Marker createMarker(double latitude, double longitude, String title, String snippet, int iconResID) {

	        return map.addMarker(new MarkerOptions()
	                .position(new LatLng(latitude, longitude))
	                .anchor(0.5f, 0.5f)
	                .title(title)
	                .snippet(snippet)

	            .icon(BitmapDescriptorFactory.fromResource(iconResID)));
	    }
	public void start(View v) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					SmsManager sms = SmsManager.getDefault();
					sms.sendTextMessage(userid, null, "@start", null, null);
					Toast.makeText(context, "SMS Sent", Toast.LENGTH_SHORT).show();
					break;

				case DialogInterface.BUTTON_NEGATIVE:

					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Are you sure?")
				.setPositiveButton("Send Tracking SMS!!", dialogClickListener)
				.setNegativeButton("Cancel", dialogClickListener).show();

	}
	public void stop(View v) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					SmsManager sms = SmsManager.getDefault();
					sms.sendTextMessage(userid, null, "@stop", null, null);
Toast.makeText(context, "SMS Sent", Toast.LENGTH_SHORT).show();
					break;

				case DialogInterface.BUTTON_NEGATIVE:

					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Are you sure?")
				.setPositiveButton("Send Cancel SMS!!", dialogClickListener)
				.setNegativeButton("Cancel", dialogClickListener).show();

	}
	

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Editor editor = preferences.edit();
		editor.putBoolean("mapOnUI", true);

		editor.commit();
		boolean check_maponUI = preferences.getBoolean("mapOnUI", true);
		if (check_maponUI) {

			IntentFilter filter1 = new IntentFilter(
					"personal_safety.location.update.source_location");
			registerReceiver(locationupdateSource, filter1);
			IntentFilter filter = new IntentFilter(
					"personal_safety.location.update.dest_location");
			registerReceiver(locationupdateDest, filter);
		}

	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Editor editor = preferences.edit();
		editor.putBoolean("mapOnUI", false);
		editor.commit();
		unregisterReceiver(locationupdateSource);
		unregisterReceiver(locationupdateDest);
		
	}

	public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

		List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
		JSONArray jRoutes = null;
		JSONArray jLegs = null;
		JSONArray jSteps = null;
		try {

			jRoutes = jObject.getJSONArray("routes");

			/** Traversing all routes */
			for (int i = 0; i < jRoutes.length(); i++) {
				jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
				List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

				/** Traversing all legs */
				for (int j = 0; j < jLegs.length(); j++) {
					jSteps = ((JSONObject) jLegs.get(j)).getJSONArray("steps");

					/** Traversing all steps */
					for (int k = 0; k < jSteps.length(); k++) {
						String polyline = "";
						polyline = (String) ((JSONObject) ((JSONObject) jSteps
								.get(k)).get("polyline")).get("points");
						List<LatLng> list = decodePoly(polyline);

						/** Traversing all points */
						for (int l = 0; l < list.size(); l++) {
							HashMap<String, String> hm = new HashMap<String, String>();
							hm.put("lat",
									Double.toString(((LatLng) list.get(l)).latitude));
							hm.put("lng",
									Double.toString(((LatLng) list.get(l)).longitude));
							path.add(hm);
						}
					}
					routes.add(path);
				}
			}

		} catch (JSONException e) {
			e.printStackTrace();
		} catch (Exception e) {
		}

		return routes;
	}

	private List<LatLng> decodePoly(String encoded) {

		List<LatLng> poly = new ArrayList<LatLng>();
		int index = 0, len = encoded.length();
		int lat = 0, lng = 0;

		while (index < len) {
			int b, shift = 0, result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lat += dlat;

			shift = 0;
			result = 0;
			do {
				b = encoded.charAt(index++) - 63;
				result |= (b & 0x1f) << shift;
				shift += 5;
			} while (b >= 0x20);
			int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
			lng += dlng;

			LatLng p = new LatLng((((double) lat / 1E5)),
					(((double) lng / 1E5)));
			poly.add(p);
		}

		return poly;
	}

	/*
	 * public void onMapReady(GoogleMap map) { map.addMarker(new MarkerOptions()
	 * .position(new LatLng(10, 10)) .title("Hello world")); }
	 */

	private String getDirectionsUrl(LatLng origin, LatLng dest) {

		// Origin of route
		String str_origin = "origin=" + origin.latitude + ","
				+ origin.longitude;

		// Destination of route
		String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

		// Sensor enabled
		String sensor = "sensor=false";

		// Building the parameters to the web service
		String parameters = str_origin + "&" + str_dest + "&" + sensor;

		// Output format
		String output = "json";

		// Building the url to the web service
		String url = "https://maps.googleapis.com/maps/api/directions/"
				+ output + "?" + parameters;
		Log.i("MAP", "url=" + url);
		return url;
	}

	/** A method to download json data from url */
	private String downloadUrl(String strUrl) throws IOException {
		String data = "";
		InputStream iStream = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(strUrl);

			// Creating an http connection to communicate with url
			urlConnection = (HttpURLConnection) url.openConnection();
			;
			urlConnection.setReadTimeout(10000);
			urlConnection.setConnectTimeout(10000);
			// Connecting to url
			urlConnection.connect();

			// Reading data from url
			iStream = urlConnection.getInputStream();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					iStream));

			StringBuffer sb = new StringBuffer();

			String line = "";
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}

			data = sb.toString();

			br.close();

		} catch (Exception e) {
			Log.d("Exception wrl", e.toString());
		} finally {
			iStream.close();
			urlConnection.disconnect();
		}
		return data;
	}

	// Fetches data from url passed
	private class DownloadTask extends AsyncTask<String, Void, String> {

		// Downloading data in non-ui thread
		@Override
		protected String doInBackground(String... url) {

			// For storing data from web service
			String data = "";

			try {
				// Fetching the data from web service
				data = downloadUrl(url[0]);
			} catch (Exception e) {
				Log.d("Background Task", e.toString());
			}
			return data;
		}

		// Executes in UI thread, after the execution of
		// doInBackground()
		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			// dialog.dismiss();
			ParserTask parserTask = new ParserTask();

			// Invokes the thread for parsing the JSON data
			parserTask.execute(result);
		}
	}

	/** A class to parse the Google Places in JSON format */
	private class ParserTask extends
			AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

		// Parsing the data in non-ui thread
		@Override
		protected List<List<HashMap<String, String>>> doInBackground(
				String... jsonData) {

			JSONObject jObject;
			List<List<HashMap<String, String>>> routes = null;

			try {
				jObject = new JSONObject(jsonData[0]);
				DirectionsJSONParser parser = new DirectionsJSONParser();

				// Starts parsing data
				routes = parser.parse(jObject);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return routes;
		}

		// Executes in UI thread, after the parsing process
		@Override
		protected void onPostExecute(List<List<HashMap<String, String>>> result) {
			ArrayList<LatLng> points = null;
			PolylineOptions lineOptions = null;
			// MarkerOptions markerOptions = new MarkerOptions();

			// Traversing through all the routes
			for (int i = 0; i < result.size(); i++) {
				points = new ArrayList<LatLng>();
				lineOptions = new PolylineOptions();

				// Fetching i-th route
				List<HashMap<String, String>> path = result.get(i);

				// Fetching all the points in i-th route
				for (int j = 0; j < path.size(); j++) {
					HashMap<String, String> point = path.get(j);

					double lat = Double.parseDouble(point.get("lat"));
					double lng = Double.parseDouble(point.get("lng"));
					LatLng position = new LatLng(lat, lng);

					points.add(position);
				}

				// Adding all the points in the route to LineOptions
				lineOptions.addAll(points);
				lineOptions.width(2);
				lineOptions.color(Color.RED);
			}

			// Drawing polyline in the Google Map for the i-th route
			if (lineOptions.isVisible())
				map.addPolyline(lineOptions);

		}
	}

	public class DirectionsJSONParser {

		/**
		 * Receives a JSONObject and returns a list of lists containing latitude
		 * and longitude
		 */
		public List<List<HashMap<String, String>>> parse(JSONObject jObject) {

			List<List<HashMap<String, String>>> routes = new ArrayList<List<HashMap<String, String>>>();
			JSONArray jRoutes = null;
			JSONArray jLegs = null;
			JSONArray jSteps = null;

			try {

				jRoutes = jObject.getJSONArray("routes");

				/** Traversing all routes */
				for (int i = 0; i < jRoutes.length(); i++) {
					jLegs = ((JSONObject) jRoutes.get(i)).getJSONArray("legs");
					List<HashMap<String, String>> path = new ArrayList<HashMap<String, String>>();

					/** Traversing all legs */
					for (int j = 0; j < jLegs.length(); j++) {
						jSteps = ((JSONObject) jLegs.get(j))
								.getJSONArray("steps");

						/** Traversing all steps */
						for (int k = 0; k < jSteps.length(); k++) {
							String polyline = "";
							polyline = (String) ((JSONObject) ((JSONObject) jSteps
									.get(k)).get("polyline")).get("points");
							List<LatLng> list = decodePoly(polyline);

							/** Traversing all points */
							for (int l = 0; l < list.size(); l++) {
								HashMap<String, String> hm = new HashMap<String, String>();
								hm.put("lat", Double.toString(((LatLng) list
										.get(l)).latitude));
								hm.put("lng", Double.toString(((LatLng) list
										.get(l)).longitude));
								path.add(hm);
							}
						}
						routes.add(path);
					}
				}

			} catch (JSONException e) {
				e.printStackTrace();
			} catch (Exception e) {
			}

			return routes;
		}
	}

	public class LocationBroadcastListenerSource extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub

			drawLine();
		}

	}

	public class LocationBroadcastListenerDest extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated m

			Editor editor = preferences.edit();
			editor.putFloat("latitude_dest",
					Float.parseFloat(intent.getStringExtra("latitude_dest")));
			editor.putFloat("longitude_dest",
					Float.parseFloat(intent.getStringExtra("longitude_dest")));

			editor.commit();

			drawLine();
		}

	}

	public void drawLine() {
		// Log.i(MyUrl.tag,
		// "befor lat"+temp_latitude_source+","+temp_longitude_source+"dest"+temp_latitude_dest+","+temp_longitude_dest);
		Double latitude_source = (double) preferences.getFloat(
				"latitude_source", 0);
		Double longitude_source = (double) preferences.getFloat(
				"longitude_source", 0);
		Double latitude_dest = (double) preferences
				.getFloat("latitude_dest", 0);
		Double longitude_dest = (double) preferences.getFloat("longitude_dest",
				0);
		LatLng pointSource = new LatLng(latitude_source, longitude_source);
		LatLng pointDestination = new LatLng(latitude_dest, longitude_dest);
		CameraPosition cameraPosition = new CameraPosition.Builder()
				.target(new LatLng(latitude_source, longitude_source)).zoom(10)
				.build();

		map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
		// markerPoints.clear();

		if (markerPoints.size() > 1) {
			markerPoints.clear();
			map.clear();
		}

		// Adding new item to the ArrayList
		if (latitude_source != 0)
			markerPoints.add(pointSource);
		if (latitude_dest != 0)
			markerPoints.add(pointDestination);
		// Creating MarkerOptions
		MarkerOptions options_source = new MarkerOptions();
		MarkerOptions options_dest = new MarkerOptions();
		// Setting the position of the marker
		options_source.position(pointSource);
		options_dest.position(pointDestination);
		/*
		 * For the start location, the color of marker is GREEN and for the end
		 * location, the color of marker is RED.
		 */
		// if(markerPoints.size()==1){
		options_source.icon(BitmapDescriptorFactory
				.fromResource(R.drawable.men));
		// }
		// if(markerPoints.size()==2){
		options_dest
				.icon(BitmapDescriptorFactory.fromResource(R.drawable.men1));
		// }

		// Add new marker to the Google Map Android API V2
		map.addMarker(options_source);
		map.addMarker(options_dest);
		// Checks, whether start and end locations are captured
		if (markerPoints.size() >= 2) {
			LatLng origin = markerPoints.get(0);
			LatLng dest = markerPoints.get(1);

			// Getting URL to the Google Directions API
			String url = getDirectionsUrl(origin, dest);
			// dialog=ProgressDialog.show(MainActivity.this, "please wait..",
			// "Downloading route..",true,true);
			DownloadTask downloadTask = new DownloadTask();

			// Start downloading json data from Google Directions API
			downloadTask.execute(url);
		}
		createMarker(latitude_dest, longitude_dest, "New Location", "Location", R.drawable.men1);
	}
}
