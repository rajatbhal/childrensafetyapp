package com.example.locationtracker;



import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.*;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Activity which displays a login screen to the user, offering registration as
 * well.
 */
public class LoginActivity extends Activity {
	/**
	 * The default email to populate the email field with.
	 */
	public static final String EXTRA_EMAIL = "com.example.android.authenticatordemo.extra.EMAIL";

	/**
	 * Keep track of the login task to ensure we can cancel it if requested.
	 */
	private UserLoginTask mAuthTask = null;

	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;
	private View mLoginFormView;
	private View mLoginStatusView;
	private TextView mLoginStatusMessageView;
Context context;
	private SharedPreferences preferences;
TextView tvLocation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_login);
		tvLocation=(TextView) findViewById(R.id.tvLocation);
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		context=this;
		// Set up the login form.
		mEmail = getIntent().getStringExtra(EXTRA_EMAIL);
		mEmailView = (EditText) findViewById(R.id.email);
		mEmailView.setText(mEmail);

		mPasswordView = (EditText) findViewById(R.id.password);
		mPasswordView
				.setOnEditorActionListener(new TextView.OnEditorActionListener() {
					@Override
					public boolean onEditorAction(TextView textView, int id,
							KeyEvent keyEvent) {
						if (id == R.id.login || id == EditorInfo.IME_NULL) {
							attemptLogin();
							return true;
						}
						return false;
					}
				});

		mLoginFormView = findViewById(R.id.login_form);
		mLoginStatusView = findViewById(R.id.login_status);
		mLoginStatusMessageView = (TextView) findViewById(R.id.login_status_message);

		findViewById(R.id.sign_in_button).setOnClickListener(
				new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						attemptLogin();

					}
				});
	isStoragePermissionGranted();

	Intent intent=new Intent(getBaseContext(),MyService.class);
	startService(intent);


	}
	String TAG="TAG";
	public  boolean isStoragePermissionGranted() {
		if (Build.VERSION.SDK_INT >= 23) {
			if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
					== PackageManager.PERMISSION_GRANTED) {
				Log.v(TAG,"Permission is granted");
				return true;
			} else {

				Log.v(TAG,"Permission is revoked");
				ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_CONTACTS, android.Manifest.permission.SEND_SMS, android.Manifest.permission.ACCESS_COARSE_LOCATION, android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.WRITE_CONTACTS}, 1);
				return false;
			}
		}
		else { //permission is automatically granted on sdk<23 upon installation
			Log.v(TAG,"Permission is granted");
			return true;
		}
	}
	

	/**
	 * Attempts to sign in or register the account specified by the login form.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {
		if (mAuthTask != null) {
			return;
		}

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			// Show a progress spinner, and kick off a background task to
			// perform the user login attempt.
			mLoginStatusMessageView.setText(R.string.login_progress_signing_in);
			showProgress(true);
			mAuthTask = new UserLoginTask();
			mAuthTask.execute((Void) null);

			
		}
	}

	/**
	 * Shows the progress UI and hides the login form.
	 */
	@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
	private void showProgress(final boolean show) {
		// On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
		// for very easy animations. If available, use these APIs to fade-in
		// the progress spinner.
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			int shortAnimTime = getResources().getInteger(
					android.R.integer.config_shortAnimTime);

			mLoginStatusView.setVisibility(View.VISIBLE);
			mLoginStatusView.animate().setDuration(shortAnimTime)
					.alpha(show ? 1 : 0)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginStatusView.setVisibility(show ? View.VISIBLE
									: View.GONE);
						}
					});

			mLoginFormView.setVisibility(View.VISIBLE);
			mLoginFormView.animate().setDuration(shortAnimTime)
					.alpha(show ? 0 : 1)
					.setListener(new AnimatorListenerAdapter() {
						@Override
						public void onAnimationEnd(Animator animation) {
							mLoginFormView.setVisibility(show ? View.GONE
									: View.VISIBLE);
						}
					});
		} else {
			// The ViewPropertyAnimator APIs are not available, so simply show
			// and hide the relevant UI components.
			mLoginStatusView.setVisibility(show ? View.VISIBLE : View.GONE);
			mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
		}
	}

	/**
	 * Represents an asynchronous login/registration task used to authenticate
	 * the user.
	 */
	String username="";
	String usertype="";
	public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {
		private String firstname;
		private String password;
		
		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO: attempt authentication against a network service.
			Boolean check = false;
			try {
				// Simulate network access.
				Thread.sleep(2000);

			} catch (InterruptedException e) {
				return check;
			}

			DBAdapter db = new DBAdapter(getBaseContext());
			db.open();
			Cursor cursor = db.getAllRegisteredUserWithPassword();
			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				
				
				if (mEmailView.getText().toString().equalsIgnoreCase(cursor.getString(5)) & mPasswordView.getText().toString()
						.equalsIgnoreCase(cursor.getString(3))) {
					username=cursor.getString(1);
					firstname=cursor.getString(4);
					usertype=cursor.getString(2);
					password=cursor.getString(3);
				
					check = true;
					
					
					break;
				}
				cursor.moveToNext();
			}
			cursor.close();
			db.close();
			
			
			
			return check;
		}

		@Override
		protected void onPostExecute(final Boolean success) {
			mAuthTask = null;
			showProgress(false);

			if (success) {				
				 if(usertype.equalsIgnoreCase("user")){
					Intent i = new Intent(getBaseContext(), MainActivity.class);
					i.putExtra("username", username);
					i.putExtra("firstname", firstname);
					i.putExtra("password", password);
					System.out.println("here");
					startActivity(i);
				}
				

			} else{
				mPasswordView
						.setError(getString(R.string.error_incorrect_password));
				mPasswordView.requestFocus();
			}
			
			
		}
		@Override
		protected void onCancelled() {
			mAuthTask = null;
			showProgress(false);
		}
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		
		finish();
	}
	public void register(View v){
		Intent i=new Intent(getBaseContext(),Registeration.class);
		i.putExtra("title", "New user");
		i.putExtra("usertype", "user");
		startActivity(i);
	}
	public void refresh(View v) {
		FetchLocation fd=new FetchLocation();
		fd.execute();

	

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
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();

		FetchLocation fd=new FetchLocation();
		fd.execute();

		GetCurrentAddress currentadd = new GetCurrentAddress();
		currentadd.execute();

	}

	public void sendPanicAlert(View view) {
		/*Intent intent1 = new Intent(this, SendSMS.class);
		intent1.putExtra("LAT", latitude);
		intent1.putExtra("LON", longitude);
		intent1.putExtra("address", tvLocation.getText().toString());
		startActivity(intent1);*/
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					SMS();
					break;

				case DialogInterface.BUTTON_NEGATIVE:

					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setMessage("Are you sure?")
				.setPositiveButton("Yes!!", dialogClickListener)
				.setNegativeButton("No", dialogClickListener).show();
	
	}
	private class GetCurrentAddress extends AsyncTask<String, Void, String> {

		@Override
		protected String doInBackground(String... urls) {
			// this lat and log we can get from current location but here we
			// given hard coded
			address1="Current Address: ";
			System.out.println("lati=" + latitude + " longi=" + longitude);

			String address = getAddress(context,Double.parseDouble(latitude), Double.parseDouble(longitude));
			System.out.println("address=" + address);
			
			return address;
		}

		@Override
		protected void onPostExecute(String resultString) {
			// dialog.dismiss();
			address1=address1+resultString;
			tvLocation.setText(address1+latlon);
			preferences.edit().putString("address",address1+latlon ).commit();
			preferences.edit().putString("location",latlon ).commit();
		}
	}
String address1="Current Address: ";
String latitude = "0";
String longitude = "0";
String latlon;
	LocationGetter location;
	class FetchLocation extends AsyncTask<Void, Void, Void>{

		@Override
		protected Void doInBackground(Void... params) {
			// TODO Auto-generated method stub
			 location=new LocationGetter();
			runOnUiThread(new Runnable() {
				public void run() {
					try{
						latitude= Double.toString(location.getLocation(LoginActivity.this).getLatitude());
						longitude= Double.toString(location.getLocation(LoginActivity.this).getLongitude());
						GetCurrentAddress currentadd = new GetCurrentAddress();
						currentadd.execute();
					}catch(Exception e){}
				
				}
			});
			 
			/*latitude= Double.toString(newlat());
			longitude= Double.toString(newlong());*/
			return null;
		}
		@Override
		protected void onPostExecute(Void result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			runOnUiThread(new Runnable() {
				public void run() {
					if(latitude==null||longitude==null){
						
					}else{
						Toast.makeText(getBaseContext(), "Latitude,Longitude "+latitude+","+longitude, Toast.LENGTH_SHORT).show();
						latlon= "\nCurrent Location: "+latitude+","+longitude;
						tvLocation.setText(address1+latlon);
					}
				}
			});
			
		}
	}
	private String etone,ettwo,etthree,etfour,etfive;
	private String etMessage1;
	public int SMS() {
		int count = 0;
		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		etone = preferences.getString("no1", null);
		ettwo = preferences.getString("no2", null);
		etthree = preferences.getString("no3", null);
		etfour = preferences.getString("no4", null);
		etfive = preferences.getString("no5", null);
		etMessage1 = preferences.getString("msg", null);

		final String mylocation;
		// to change the location
		mylocation = preferences.getString("location", "NA");
		String arr[] = new String[1];
		arr[0] = mylocation;

		final String etMessage =etMessage1+"\n"+ preferences.getString("address",null) + "http://maps.google.com/maps?z=11&t=k&q=loc:"+preferences.getString("location","0.0");
		System.out.println(etMessage);
		ArrayList<String> strings = new ArrayList<>();
		int index = 0;
		while (index < etMessage.length()) {
			String value=etMessage.substring(index, Math.min(index + 150,etMessage.length()));
			Log.i("message parts", "SMS: "+value);
			strings.add(value);
			index += 150;

		}

		try {

			SmsManager smsManager = null;


			if (etone != null) {
				if(etone.length()>5){
					smsManager = SmsManager.getDefault();

					smsManager.sendMultipartTextMessage(etone.trim(),null,strings,null,null);

					//					smsManager.sendTextMessage(etone.trim(), null, etMessage, null,
//							null);
					Log.d("sms1 ", "sent" + etone.trim());
					count++;
				}
			}


			if (ettwo != null) {
				if(ettwo.length()>5){
					smsManager = SmsManager.getDefault();
					smsManager.sendMultipartTextMessage(ettwo.trim(),null,strings,null,null);

					Log.d("sms2 ", "sent" + ettwo.trim());
					count++;
				}

			}



			if (etthree != null) {
				if(etthree.length()>5){
					smsManager = SmsManager.getDefault();
					smsManager.sendMultipartTextMessage(etthree.trim(),null,strings,null,null);
					Log.d("sms3 ", "sent");
					count++;
				}

			}
			if (etfour != null) {
				if(etfour.length()>5){
					smsManager = SmsManager.getDefault();
					smsManager.sendMultipartTextMessage(etfour.trim(),null,strings,null,null);

					Log.d("sms4 ", "sent");
					count++;
				}
			}
			if (etfive != null) {
				if(etfive.length()>5){
					smsManager = SmsManager.getDefault();
					smsManager.sendMultipartTextMessage(etfive.trim(),null,strings,null,null);

					Log.d("sms5 ", "sent");
					count++;
				}
			}
			if (count > 0) {
				Toast.makeText(getApplicationContext(), "SMS sent on "+count+" Contacts",
						Toast.LENGTH_LONG).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"Please set contact first", Toast.LENGTH_LONG).show();
			}

		} catch (final Exception e) {

			Toast.makeText(getApplicationContext(),
					"SMS failed, please try again later!", Toast.LENGTH_LONG)
					.show();
			e.printStackTrace();

		}
		return count;
	}


}
