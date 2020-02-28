package com.example.locationtracker;

import java.util.ArrayList;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;




public class AddEmergencyNo extends Activity {
	
	private static final String DEBUG_TAG = null;

	EditText etname1;
	EditText etname2;
	EditText etname3;
	EditText etname4;
	EditText etname5;
	Button btngetcontact1;
	Button btngetcontact2;
	Button btngetcontact3;
	Button btngetcontact4;
	Button btngetcontact5;
	EditText etone;
	EditText etwo;
	EditText ethree;
	EditText etfour;
	EditText etfive;
	SharedPreferences preferences;
	EditText etMessage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_add_emergency_no);

		etone = (EditText) findViewById(R.id.etone);
		etwo = (EditText) findViewById(R.id.ettwo);
		ethree = (EditText) findViewById(R.id.etthree);
		etfour = (EditText) findViewById(R.id.etfour);
		etfive = (EditText) findViewById(R.id.etfive);
		etMessage = (EditText) findViewById(R.id.etMessage);
		try {
			etname1 = (EditText) findViewById(R.id.etonename);
			etname2 = (EditText) findViewById(R.id.ettwoname);
			etname3 = (EditText) findViewById(R.id.etthereename);
			etname4 = (EditText) findViewById(R.id.etfourname);
			etname5 = (EditText) findViewById(R.id.etfivename);
		} catch (Exception e) {
			e.printStackTrace();
		}

		preferences = PreferenceManager
				.getDefaultSharedPreferences(getApplicationContext());
		readFromFiles("name1", "no1", etone, etname1);
		readFromFiles("name2", "no2", etwo, etname2);
		readFromFiles("name3", "no3", ethree, etname3);
		readFromFiles("name4", "no4", etfour, etname4);
		readFromFiles("name5", "no5", etfive, etname5);
		etMessage.setText(preferences.getString("msg",""));

		Button btnSave = (Button) findViewById(R.id.btnsavecontact);

		// Button btnSend = (Button) findViewById(R.id.btnsendMessage);
		btnSave.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				Editor edit = preferences.edit();
				edit.putString("name1", etname1.getText().toString());
				edit.putString("name2", etname2.getText().toString());
				edit.putString("name3", etname3.getText().toString());
				edit.putString("name4", etname4.getText().toString());
				edit.putString("name5", etname5.getText().toString());
			
				
				
				
				edit.putString("no1", validateMobile(etone.getText().toString()));
				edit.putString("no2",validateMobile( etwo.getText().toString()));
				edit.putString("no3",validateMobile( ethree.getText().toString()));
				edit.putString("no4", validateMobile(etfour.getText().toString()));
				edit.putString("no5", validateMobile(etfive.getText().toString()));
				edit.putString("msg", etMessage.getText().toString());
				edit.commit();
				Toast.makeText(getBaseContext(), "Saved", Toast.LENGTH_SHORT)
						.show();
				finish();

			}
		});

		try {
			btngetcontact1 = (Button) findViewById(R.id.btnGetContact1);
			btngetcontact2 = (Button) findViewById(R.id.btnGetContact2);
			btngetcontact3 = (Button) findViewById(R.id.btnGetContact3);
			btngetcontact4 = (Button) findViewById(R.id.btnGetContact4);
			btngetcontact5 = (Button) findViewById(R.id.btnGetContact5);
		} catch (Exception e) {
			e.printStackTrace();
		}

		btngetcontact1.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doLaunchContactPicker1(v);
			}
		});
		btngetcontact2.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doLaunchContactPicker2(v);
			}
		});
		btngetcontact3.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doLaunchContactPicker3(v);
			}
		});
		btngetcontact4.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doLaunchContactPicker4(v);
			}
		});
		btngetcontact5.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				doLaunchContactPicker5(v);
			}
		});

	}

	public void readFromFiles(String filename, String filenamen0,
			EditText etPhone, EditText etName) {
		try {
			etName.setText(preferences.getString(filename, null));
			etPhone.setText(preferences.getString(filenamen0, null));

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
public String validateMobile(String mobile){
	String no1=mobile;
	if(no1!=null){
		if(no1.startsWith("0")){
			no1=no1.substring(1, no1.length());
			
		}else if(no1.startsWith("+91")){
			no1=no1.substring(3,no1.length());
			
		}
		no1=no1.replace(" ", "");
		return no1.trim();
	}else{
		return null;
	}
	
	
}
	public void doLaunchContactPicker1(View view) {
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
				Contacts.CONTENT_URI);
		startActivityForResult(contactPickerIntent, 001);
	}

	public void doLaunchContactPicker2(View view) {
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
				Contacts.CONTENT_URI);
		startActivityForResult(contactPickerIntent, 002);
	}

	public void doLaunchContactPicker3(View view) {
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
				Contacts.CONTENT_URI);
		startActivityForResult(contactPickerIntent, 003);
	}

	public void doLaunchContactPicker4(View view) {
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
				Contacts.CONTENT_URI);
		startActivityForResult(contactPickerIntent, 004);
	}

	public void doLaunchContactPicker5(View view) {
		Intent contactPickerIntent = new Intent(Intent.ACTION_PICK,
				Contacts.CONTENT_URI);
		startActivityForResult(contactPickerIntent, 005);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case 001:
				setEditTextValue(etname1, etone, data);
				break;
			case 002:
				setEditTextValue(etname2, etwo, data);
				break;
			case 003:
				setEditTextValue(etname3, ethree, data);
				break;
			case 004:
				setEditTextValue(etname4, etfour, data);
				break;
			case 005:
				setEditTextValue(etname5, etfive, data);
				break;
			}
		} else {
			Log.w(DEBUG_TAG, "Warning: activity result not ok");
		}
	}

	public void setEditTextValue(EditText etname, EditText etnumber, Intent data) {
		Cursor cursor = null;
		String email = "";
		String name = "";
		try {
			Uri result = data.getData();
			Log.v(DEBUG_TAG, "Got a contact result: " + result.toString());
			// get the contact id from the Uri
			String id = result.getLastPathSegment();
			// query for everything email
			cursor = getContentResolver().query(Phone.CONTENT_URI, null,
					Phone.CONTACT_ID + "=?", new String[] { id }, null);
			int emailIdx = cursor.getColumnIndex(Phone.DATA);
			int nameidx = cursor.getColumnIndex(Contacts.DISPLAY_NAME);
			// let's just get the first email
			if (cursor.moveToFirst()) {
				email = cursor.getString(emailIdx);
				name = cursor.getString(nameidx);
				Log.v(DEBUG_TAG, "Got email: " + email);
			} else {
				Log.w(DEBUG_TAG, "No results");
			}
		} catch (Exception e) {
			Log.e(DEBUG_TAG, "Failed to get email data", e);
		} finally {
			if (cursor != null) {
				cursor.close();
			}

			etnumber.setText(email);
			etname.setText(name);

		}
	}

	public void save(String name, String mobile) {
		String DisplayName = name;
		String MobileNumber = mobile;

		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();

		ops.add(ContentProviderOperation
				.newInsert(ContactsContract.RawContacts.CONTENT_URI)
				.withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
				.withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
				.build());

		// ------------------------------------------------------ Names
		if (DisplayName != null) {
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
					.withValue(
							ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
							DisplayName).build());
		}

		// ------------------------------------------------------ Mobile Number
		if (MobileNumber != null) {
			ops.add(ContentProviderOperation
					.newInsert(ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(
							ContactsContract.Data.RAW_CONTACT_ID, 0)
					.withValue(
							ContactsContract.Data.MIMETYPE,
							ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
					.withValue(ContactsContract.CommonDataKinds.Phone.NUMBER,
							MobileNumber)
					.withValue(ContactsContract.CommonDataKinds.Phone.TYPE,
							ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE)
					.build());
		}

		// Asking the Contact provider to create a new contact
		try {
			getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
		} catch (Exception e) {
			e.printStackTrace();
			Toast.makeText(getBaseContext(), "Exception: " + e.getMessage(),
					Toast.LENGTH_SHORT).show();
		}
	}
}
