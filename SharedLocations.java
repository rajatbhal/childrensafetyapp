package com.example.locationtracker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class SharedLocations extends Activity{
	ListView list;
	String source;
@Override
protected void onCreate(Bundle savedInstanceState) {
	// TODO Auto-generated method stub
	super.onCreate(savedInstanceState);
	setContentView(R.layout.shared_locations);
	list=(ListView) findViewById(R.id.list);
	source=getIntent().getStringExtra("source");
	Adapter1 adapter=new Adapter1(getBaseContext(), R.layout.custum_list1, lstDetails);
	list.setAdapter(adapter);
	getAllSms();
	
	list.setOnItemClickListener(new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			// TODO Auto-generated method stub
			Intent i=new Intent(getBaseContext(),MyMap.class);
			i.putExtra("source",source);
			i.putExtra("dest", lstMsg.get(position));
			startActivity(i);
		}
	});
}

ArrayList<String> lstMsgType = new ArrayList<String>();
ArrayList<String> lstSentFrom = new ArrayList<String>();
ArrayList<String> lstMsg = new ArrayList<String>();
ArrayList<String> lstDate = new ArrayList<String>();
ArrayList<String> lstStatus = new ArrayList<String>();
ArrayList<String> lstDetails = new ArrayList<String>();
public void getAllSms() {

	Uri message = Uri.parse("content://sms/");
	ContentResolver cr = getContentResolver();

	Cursor c = cr.query(message, null, null, null, null);
	// startManagingCursor(c);

	while (c.moveToNext()) {
		try {
			String msg = c.getString(c.getColumnIndexOrThrow("body"));
			if (msg.contains("My Location is: ")) {

				if (c.getColumnIndexOrThrow("type") == 1) {
					lstMsgType.add("Inbox");

				} else {

					lstMsgType.add("Outbox");
				}
				String date=millisToDate(c.getLong(c.getColumnIndex("date")));
				lstDate.add(date);

				lstSentFrom
						.add(c.getString(c.getColumnIndexOrThrow("address")));
				lstStatus.add(c.getString(c.getColumnIndex("read")));
				msg = msg.substring(msg.indexOf("(")+1, msg.indexOf(")"));
				lstMsg.add(msg);
				lstDetails.add("Receiver: "+c.getString(c.getColumnIndexOrThrow("address"))+
						"\nLocation: "+msg+"\nDated: "+date);
			} else {
				Log.i("LS", "no need to share" + msg);
			}

		} catch (Exception e) {
			// TODO: handle exception
			Log.i("SL","error"+e.getLocalizedMessage());
		}
		
	}

	c.close();

}


public static String millisToDate(long currentTime) {
    String finalDate;
    Calendar calendar = Calendar.getInstance();
    calendar.setTimeInMillis(currentTime);
    Date date = calendar.getTime();
    finalDate = date.toString();
    return finalDate;
}


}
