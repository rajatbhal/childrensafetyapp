package com.example.locationtracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Registeration extends Activity {

	String strFirstname = "";
	String strLastname = "";
	String strusername = "";
	String strpassword = "";
	String strFathername = "";
	String strMobile = "";
	String strEmailid = "";
	String strAddress = "";
	String strUsertype = "";
	private ProgressDialog dialog = null;
	private Context context = null;
	AsyncActivity mAuthTask = null;
	long check = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.registeration);
		TextView tvtitle = (TextView) findViewById(R.id.tvTitle);
		Intent i = getIntent();
		String title = i.getStringExtra("title");
		tvtitle.setText(title);
		strUsertype = i.getStringExtra("usertype");
		System.out.println("usertype=" + strUsertype);
		final EditText etFirstname = (EditText) findViewById(R.id.etFirstName);
		final EditText etLastname = (EditText) findViewById(R.id.etLastname);
		final EditText etusername = (EditText) findViewById(R.id.etusername1);
		final EditText etpassword = (EditText) findViewById(R.id.etpassword);
		final EditText etFathername = (EditText) findViewById(R.id.etfathername);
		final EditText etMobile = (EditText) findViewById(R.id.etmobile);
		final EditText etEmailid = (EditText) findViewById(R.id.etemailid);
		final EditText etAddress = (EditText) findViewById(R.id.etaddress);
		context = this;
		Button btnSubmit = (Button) findViewById(R.id.btnUpdate);

		btnSubmit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try {
					strFirstname = etFirstname.getText().toString();
					strLastname = etLastname.getText().toString();

					strusername = etusername.getText().toString();
					strpassword = etpassword.getText().toString();
					strFathername = etFathername.getText().toString();
					strMobile = etMobile.getText().toString();
					strEmailid = etEmailid.getText().toString();
					strAddress = etAddress.getText().toString();
				} catch (Exception e) {
					e.printStackTrace();
				}

				System.out.println("name=" + strFirstname + ":" + strLastname
						+ ":" + strusername + ":" + strUsertype);
				Context context = Registeration.this;
				String title = "Warning!!";
				String message = "Save Details";
				String button1String = "Save";
				String button2String = "Cancel";
				AlertDialog.Builder ad = new AlertDialog.Builder(context);
				ad.setTitle(title);
				ad.setMessage(message);
				ad.setPositiveButton(button1String, new OnClickListener() {

					@Override
					public void onClick(DialogInterface arg0, int arg1) {
					saveDetails();
					}
				});
				ad.setNegativeButton(button2String, new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						Toast.makeText(getBaseContext(), "Canceled!",
								Toast.LENGTH_SHORT).show();
						finish();
					}
				});

				ad.show();

			}
		});

	}

	public class AsyncActivity extends AsyncTask<Void, Void, Boolean> {

		@Override
		protected Boolean doInBackground(Void... params) {
			// TODO Auto-generated method stub

			Boolean check = false;
			try {
				// Simulate network access.
				Thread.sleep(2000);

			} catch (InterruptedException e) {
				return check;
			}
			try {
				saveDetails();
			} catch (Exception e) {
				// TODO: handle exception
				e.printStackTrace();
			}

			return check;

		}

		@Override
		protected void onPreExecute() {
			dialog = ProgressDialog.show(context, "", "Please wait..", true);

		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			dialog.dismiss();
			mAuthTask = null;
			if (check == 1){
				Toast.makeText(getBaseContext(), "Registration successfull",
						Toast.LENGTH_SHORT).show();
			finish();
			}
			else if (check == 2)
				Toast.makeText(getBaseContext(), "Please check the fields",
						Toast.LENGTH_SHORT).show();
			else if (check == 3)
				Toast.makeText(getBaseContext(), "Please check Emailid",
						Toast.LENGTH_SHORT).show();
			// to do here
			//finish();
		}
	}

	public void saveDetails() {

		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");

		Calendar cal = Calendar.getInstance();

		String reg_date = dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
		String reg_time = dateFormat1.format(cal.getTime());

		DBAdapter db1 = new DBAdapter(getBaseContext());
		db1.open();
		if (!(strusername.length() < 6 || strpassword.length() < 6
				|| strFirstname.length() < 3 || strEmailid.length() < 6
				|| strMobile.length() < 10)) {
			
			if ((strEmailid.contains("@gmail.com"))) {
				
				check = db1.registerUser(strusername, strUsertype, strpassword,
						strFirstname, strLastname, strFathername, strMobile,
						strEmailid, strAddress, reg_date, reg_time, "True");
				if(check>0){
					Toast.makeText(getBaseContext(), "Registration successfull",
							Toast.LENGTH_SHORT).show();
				}else{
					Toast.makeText(getBaseContext(), "Registration unsuccessfull",
							Toast.LENGTH_SHORT).show();
				}
			}else{
				Toast.makeText(getBaseContext(), "Please enter a vailid gmail id",
						Toast.LENGTH_SHORT).show();
			}
			
			
		}else{
			Toast.makeText(getBaseContext(), "Please check all the fields",
					Toast.LENGTH_SHORT).show();
		}

		db1.close();

	}

}