package com.example.locationtracker;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;

public class MyService extends Service implements SensorEventListener {

	private SensorManager mSensorManager;
	private Sensor mAccelerometer;
	private float mAccel; // acceleration apart from gravity
	private float mAccelCurrent; // current acceleration including gravity
	private float mAccelLast; // last acceleration including gravity

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mAccelerometer = mSensorManager
				.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
		mSensorManager.registerListener(this, mAccelerometer,
				SensorManager.SENSOR_DELAY_UI, new Handler());
		return START_STICKY;
	}

	@Override
	public void onAccuracyChanged(Sensor sensor, int accuracy) {

	}

	private long mShakeTimestamp;
	private int mShakeCount;
	private static final float SHAKE_THRESHOLD_GRAVITY = 2.8F;
	private static final int SHAKE_SLOP_TIME_MS = 500;
	private static final int SHAKE_COUNT_RESET_TIME_MS = 3000;

	@Override
	public void onSensorChanged(SensorEvent event) {
		/*
		 * float x = event.values[0]; float y = event.values[1]; float z =
		 * event.values[2]; mAccelLast = mAccelCurrent; mAccelCurrent = (float)
		 * Math.sqrt((double) (x * x + y * y + z * z)); float delta =
		 * mAccelCurrent - mAccelLast; mAccel = mAccel * 0.9f + delta; //
		 * perform low-cut filter
		 * 
		 * 
		 * if(mAccel>10){ Log.i("SENSOR","mAccel"+mAccel); } if (mAccel > 15) {
		 * Log.i("SENSOR notification wala","mAccel"+mAccel);
		 * showNotification(); }
		 */

		float x = event.values[0];
		float y = event.values[1];
		float z = event.values[2];

		float gX = x / SensorManager.GRAVITY_EARTH;
		float gY = y / SensorManager.GRAVITY_EARTH;
		float gZ = z / SensorManager.GRAVITY_EARTH;

		// gForce will be close to 1 when there is no movement.
		float gForce = (float) Math.sqrt(gX * gX + gY * gY + gZ * gZ);
		if (gForce > 10) {
			Log.i("SENSOR", "mAccel-=" + mAccel);
		}

		if (gForce > SHAKE_THRESHOLD_GRAVITY) {
			final long now = System.currentTimeMillis();
			// ignore shake events too close to each other (500ms)
			if (mShakeTimestamp + SHAKE_SLOP_TIME_MS > now) {
				return;
			}

			// reset the shake count after 3 seconds of no shakes
			if (mShakeTimestamp + SHAKE_COUNT_RESET_TIME_MS < now) {
				mShakeCount = 0;
			}

			mShakeTimestamp = now;
			mShakeCount++;
			if (mShakeCount == 3) {
				showNotification();
			}
		}
	}

	/**
	 * show notification when Accel is more then the given int.
	 */
	private void showNotification() {
		if(SMS()>0){
			final NotificationManager mgr = (NotificationManager) this
					.getSystemService(Context.NOTIFICATION_SERVICE);
			NotificationCompat.Builder note = new NotificationCompat.Builder(this);
			note.setContentTitle("Sending Emergency Message");
			note.setTicker("New Message Alert!");
			note.setAutoCancel(true);
			// to set default sound/light/vibrate or all
			note.setDefaults(Notification.DEFAULT_ALL);
			// Icon to be set on Notification

			note.setSmallIcon(R.drawable.ic_launcher);
			// This pending intent will open after notification click
			PendingIntent pi = PendingIntent.getActivity(this, 0, new Intent(this,
					MainActivity.class), 0);
			// set pending intent to notification builder
			note.setContentIntent(pi);
			mgr.notify(101, note.build());
		}else{
			Toast.makeText(this, "No Contacts saved!!", Toast.LENGTH_SHORT).show();
		}

		

	}

	private String etone, ettwo, etthree, etfour, etfive;
	private String etMessage1;
	private SharedPreferences preferences;

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