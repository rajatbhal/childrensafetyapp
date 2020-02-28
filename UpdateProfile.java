package com.example.locationtracker;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class UpdateProfile extends Activity {
	String firstname = null, lastname = null, fathername = null;
	String username = null;
	String mobile = null, emailid = null, address = null;
	EditText etemailid;
	EditText etfathername;
	View focusView = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_update_profile);

		EditText etFirstname = (EditText) findViewById(R.id.etFirstName);
		final EditText etLastname = (EditText) findViewById(R.id.etLastname);
		etfathername = (EditText) findViewById(R.id.etfathername);
		EditText etusername = (EditText) findViewById(R.id.etusername1);

		final EditText etmobile = (EditText) findViewById(R.id.etmobile);
		etemailid = (EditText) findViewById(R.id.etemailid);
		final EditText etaddress = (EditText) findViewById(R.id.etaddress);
		Button btnUpdate = (Button) findViewById(R.id.btnUpdate);
		Button btnCancel = (Button) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		Intent i = getIntent();
		String strusername = i.getStringExtra("username");
		final DBAdapter db = new DBAdapter(this);
		db.open();
		Cursor c = db.getUserByUsername(strusername);

		if (c.moveToFirst()) {
			firstname = c.getString(5);
			lastname = c.getString(6);
			username = c.getString(1);
			fathername = c.getString(4);
			mobile = c.getString(7);
			emailid = c.getString(8);
			address = c.getString(9);

		}
		c.close();
		db.close();
		etFirstname.setText(firstname);
		etLastname.setText(lastname);
		etfathername.setText(fathername);
		etusername.setText(username);
		etmobile.setText(mobile);
		etemailid.setText(emailid);
		etaddress.setText(address);
		btnUpdate.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				db.open();
				if (check_email(etemailid.getText().toString()) && check_fathername(etfathername.getText().toString())) {
					try {
						int val = db.updateContact(username, etLastname
								.getText().toString(), etmobile.getText()
								.toString(), etemailid.getText().toString(),
								etaddress.getText().toString(), etfathername
										.getText().toString());
						System.out.println("no. of rows updated=" + val);
						if (val > 0) {
							Toast.makeText(getApplication(), "Update Complete",
									Toast.LENGTH_SHORT).show();
						} else
							System.out.println("lolzz");
						db.close();
					} catch (Exception e) {
						// TODO: handle exception
						Toast.makeText(getApplication(), "Error occured",
								Toast.LENGTH_SHORT).show();
						e.printStackTrace();
					}
				}

			}
		});

	}

	public boolean check_email(String mEmail) {
		// Check for a valid email address.
		boolean check = true;
		if (TextUtils.isEmpty(mEmail)) {
			etemailid.setError(getString(R.string.error_field_required));
			focusView = etemailid;
			check = false;
		}
		if (!mEmail.contains("@")) {
			etemailid.setError(getString(R.string.error_invalid_email));
			focusView = etemailid;
			check = false;
		}
		return check;
	}
	public boolean check_fathername(String fname) {
		// Check for a valid email address.
		boolean check = true;
		if (TextUtils.isEmpty(fname)) {
			etfathername.setError("cannot be null");
			focusView = etfathername;
			check = false;
		}
		if (fname.contains("@")||fname.contains("1")) {
			etfathername.setError("Father name must be alphabet");
			focusView = etfathername;
			check = false;
		}
		return check;
	}

	
}
