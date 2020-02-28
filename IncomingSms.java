package com.example.locationtracker;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.telephony.SmsManager;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

public class IncomingSms extends BroadcastReceiver {

	// Get the object of SmsManager
	final SmsManager sms = SmsManager.getDefault();
	private SharedPreferences preferences;

	public void onReceive(Context context, Intent intent) {
		preferences = PreferenceManager.getDefaultSharedPreferences(context);
		// Retrieves a map of extended data from the intent.
		final Bundle bundle = intent.getExtras();

		try {

			if (bundle != null) {

				final Object[] pdusObj = (Object[]) bundle.get("pdus");

				for (int i = 0; i < pdusObj.length; i++) {

					SmsMessage currentMessage = SmsMessage
							.createFromPdu((byte[]) pdusObj[i]);
					String phoneNumber = currentMessage
							.getDisplayOriginatingAddress();

					String senderNum = phoneNumber;
					String message = currentMessage.getDisplayMessageBody();

					Log.i("SmsReceiver", "senderNum: " + senderNum
							+ "; message: " + message);
					if(phoneNumber.contains(preferences.getString("no1", null))
							||phoneNumber.contains(preferences.getString("no2", null))
							||phoneNumber.contains(preferences.getString("no3", null))
							||phoneNumber.contains(preferences.getString("no4", null))
							||phoneNumber.contains(preferences.getString("no5", null))
							) {


						// @30

						if (message.contains("@start") || message.contains("@stop")) {

							String latitude = preferences.getString("latitude", "0.0");
							String longitude = preferences.getString("longitude",
									"0.0");

							if (message.contains("@start")) {

								preferences.edit().putBoolean("send_msg", true)
										.commit();

							} else if (message.contains("@stop")) {
								preferences.edit().putBoolean("send_msg", false)
										.commit();

							}


							Thread t = new Thread();
							t.start();
							while (preferences.getBoolean("send_msg", false)) {
								// ##-28.77888$$:77.223
								String smsText = "##-" + latitude + "$$:"
										+ longitude;
								Log.i("SMS", "from address=" + senderNum
										+ " sms text " + smsText);
								SmsManager sms = SmsManager.getDefault();
								sms.sendTextMessage(senderNum, null, smsText, null,
										null);
								Thread.sleep(30 * 1000);

							}

						} else if (message.contains("##:")) {

							Intent i1 = new Intent(
									"personal_safety.location.update.dest_location");
							i1.putExtra("latitude_dest", message.substring(message.indexOf("-"), message.indexOf("$")));
							i1.putExtra("longitude_dest", message.substring(message.indexOf(":"), message.length()));
							context.sendBroadcast(i1);

						}


						// Show Alert

						Toast.makeText(
								context,
								"Message Received from " + senderNum + ", message: " + message,
								Toast.LENGTH_LONG).show();

					}
				} // end for loop
			} // bundle is null

		} catch (Exception e) {
			Log.e("SmsReceiver", "Exception smsReceiver" + e);

		}
	}
}