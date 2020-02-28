package com.example.locationtracker;


import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

public class AllContacts extends Activity{
	
	private NamesAdapter adapter;
	ArrayList<String>lstNames=new ArrayList<String>(),lstMobile=new ArrayList<String>();
	ListView list;
String latitude,longitude;
EditText txtSearch;
Adapter1 adapter1;
int position11=0;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.mylist);
	list=(ListView) findViewById(R.id.list);
	latitude=getIntent().getStringExtra("latitude");
	longitude=getIntent().getStringExtra("longitude");
	txtSearch=(EditText) findViewById(R.id.txtSearch);
	adapter = new NamesAdapter(getApplicationContext(),
			R.layout.custum_list1, lstNames, lstMobile);
	adapter1=new Adapter1(this,R.layout.custum_list1,lstMobile);

	list.setAdapter(adapter1);
	txtSearch.addTextChangedListener(new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
			// When user changed the Text
            adapter1.getFilter().filter(cs);


		}

		@Override
		public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
									  int arg3) {
			// TODO Auto-generated method stub

		}

		@Override
		public void afterTextChanged(Editable arg0) {
			// TODO Auto-generated method stub
		}
	});



	list.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, final int position,
				long id) {
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
				case DialogInterface.BUTTON_POSITIVE:
					//Yes button clicked

                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" +adapter1.getItem(position)));
		        	startActivity(intent);
				
					break;

				case DialogInterface.BUTTON_NEGATIVE:
					//No button clicked
					Intent i=new Intent(getBaseContext(),GoogleMapActivity.class);
					i.putExtra("userid",adapter1.getItem(position));
					startActivity(i);
					break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(AllContacts.this);
		builder.setMessage("Call Or Track")
				.setPositiveButton("Call", dialogClickListener)
				.setNegativeButton("Track", dialogClickListener).show();
					


		}
		});



	dialog=ProgressDialog.show(AllContacts.this, "Please wait..", "Fetching contacts..",true,true);
	FetchContacts fetch=new FetchContacts();
	fetch.execute();

	
}
class FetchContacts extends AsyncTask<Void, Void, Void>{

	@Override
	protected Void doInBackground(Void... params) {
		// TODO Auto-generated method stub
	fetchContacts();
	
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		// TODO Auto-generated method stub
		super.onPostExecute(result);
	dialog.dismiss();
	runOnUiThread(new Runnable() {
		public void run() {
			adapter.notifyDataSetChanged();
		}
	});
		
	}
}

JSONArray jArray = new JSONArray();
JSONObject jObj;// =new JSONObject();
private JSONObject jName = new JSONObject();
private ProgressDialog dialog;
public JSONArray fetchContacts() {
	Log.i("PING", "fetching contcts inside service");
	String phoneNumber = null;

	Uri CONTENT_URI = ContactsContract.Contacts.CONTENT_URI;
	String _ID = ContactsContract.Contacts._ID;
	String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

	Uri PhoneCONTENT_URI = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
	String Phone_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
	String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;
	String CNAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

	ContentResolver contentResolver = getContentResolver();

	Cursor cursor = contentResolver.query(CONTENT_URI, null, null, null,
			CNAME);

	// Loop for every contact in the phone
	if (cursor.getCount() > 0) {

		while (cursor.moveToNext()) {
			String name;
			String contact_id = cursor
					.getString(cursor.getColumnIndex(_ID));

			int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor
					.getColumnIndex(HAS_PHONE_NUMBER)));

			if (hasPhoneNumber > 0) {

				// Query and loop for every phone number of the contact
				Cursor phoneCursor = contentResolver.query(
						PhoneCONTENT_URI, null, Phone_CONTACT_ID + " = ?",
						new String[] { contact_id }, null);

				while (phoneCursor.moveToNext()) {
					phoneNumber = phoneCursor.getString(phoneCursor
							.getColumnIndex(NUMBER));
					// output.append("\n Phone number:" + phoneNumber);
					try {
						name = phoneCursor.getString(phoneCursor
								.getColumnIndex(Contacts.DISPLAY_NAME));
						Log.i("ping", "CON=" + phoneNumber);
						if ((phoneNumber.length()>=10)
								) {
							Log.i("ping", "CON=" + phoneNumber);
							phoneNumber = phoneNumber.replaceAll(
									"[^+?0-9]+", "");
							// phoneNumber =
							// phoneNumber.replaceAll("[^?0-9]+", " ");
							phoneNumber = phoneNumber.replace(" ", "");
							if (phoneNumber.charAt(0) == 0) {
								phoneNumber = phoneNumber.substring(1);
							}
							if (phoneNumber.contains("+91")) {
								phoneNumber = phoneNumber
										.replace("+91", "").trim();
							}
							
							if (!lstMobile.contains(phoneNumber)) {
								lstMobile.add(phoneNumber);
								lstNames.add(name+" "+phoneNumber);
								jObj = new JSONObject();
								jName.put(phoneNumber, name);
								jObj.put("phone", phoneNumber);

								jArray.put(jObj);
							}

						}

					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

				phoneCursor.close();

			}

		}

	}
	return jArray;
}


	
}
