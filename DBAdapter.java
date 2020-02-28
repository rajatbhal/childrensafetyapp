 package com.example.locationtracker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {

	// ---------------for user details------------
	public static final String KEY_ROWID = Messages.getString("DBAdapter.0");// to save id //$NON-NLS-1$
	public static final String KEY_USERNAME = Messages.getString("DBAdapter.1"); //$NON-NLS-1$
	public static final String KEY_PASSWORD = Messages.getString("DBAdapter.2"); //$NON-NLS-1$
	public static final String KEY_USERTYPE = Messages.getString("DBAdapter.3"); //$NON-NLS-1$
	public static final String KEY_FIRSTNAME = Messages
			.getString("DBAdapter.4"); //$NON-NLS-1$
	public static final String KEY_LASTNAME = Messages.getString("DBAdapter.5"); //$NON-NLS-1$
	public static final String KEY_FATHERNAME = Messages
			.getString("DBAdapter.6"); //$NON-NLS-1$
	public static final String KEY_MOBILE = Messages.getString("DBAdapter.7"); //$NON-NLS-1$
	public static final String KEY_EMAILID = Messages.getString("DBAdapter.8"); //$NON-NLS-1$
	public static final String KEY_ADDRESS = Messages.getString("DBAdapter.9"); //$NON-NLS-1$
	public static final String KEY_DATE = Messages.getString("DBAdapter.10");// to save date of registration table //$NON-NLS-1$
	public static final String KEY_TIME = Messages.getString("DBAdapter.11");// to save time //$NON-NLS-1$
	public static final String KEY_STATUS = Messages.getString("DBAdapter.12");// to check status of task //$NON-NLS-1$
	private static final String TAG = Messages.getString("DBAdapter.13");// for database adapter //$NON-NLS-1$
	public static final String DATABASE_NAME = "FRIEND"; //$NON-NLS-1$
	public static final String DATABASE_TABLE = "REGISTERATION"; //$NON-NLS-1$
	private static final int DATABASE_VERSION = 1;

	private static final String DATABASE_CREATE = "create table REGISTERATION (_id integer primary key autoincrement, " //$NON-NLS-1$
			+ "username text unique not null,usertype text not null, password text not null,fathername text not null,firstname text not null,lastname text not null,mobile text not null,emailid text unique not null,address text not null, date1 text not null, time1 text not null, status text not null);"; //$NON-NLS-1$


	
	//
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		// super(ctx, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {

				db.execSQL(DATABASE_CREATE);
				
				
				saveInitialValues(db);
				// db.close();
				Log.w(TAG, "database created dea"); //$NON-NLS-1$
			} catch (SQLException e) {
				e.printStackTrace();
				// db.close();
			}
		}

		private static void save(SQLiteDatabase db, String username,
				String usertype, String password, String firstname,
				String lastname, String fathername, String mobile,
				String emailid, String address, String date, String time,
				String status) {
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_USERNAME, username);
			initialValues.put(KEY_PASSWORD, password);
			initialValues.put(KEY_USERTYPE, usertype);
			initialValues.put(KEY_FIRSTNAME, firstname);
			initialValues.put(KEY_LASTNAME, lastname);
			initialValues.put(KEY_FATHERNAME, fathername);
			initialValues.put(KEY_MOBILE, mobile);
			initialValues.put(KEY_EMAILID, emailid);
			initialValues.put(KEY_ADDRESS, address);
			initialValues.put(KEY_DATE, date);
			initialValues.put(KEY_TIME, time);
			initialValues.put(KEY_STATUS, status);

			db.insert(DATABASE_TABLE, null, initialValues);
		}
		
	
		
		private static void saveInitialValues(SQLiteDatabase db) {

			save(db,
					"admin", "admin", "asdfgh", "Admin", "Mono", "ABC", "989898998", "abc@gmail.com", "Delhi", "21/10/2013", "18:44:25", "True"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$

			save(db,
					"shilpi", "user", "asdfgh", "Shilpi", "Arya", "ABC", "989898998", "shilpi@gmail.com", "Delhi", "21/10/2013", "18:44:25", "True"); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$ //$NON-NLS-9$ //$NON-NLS-10$ //$NON-NLS-11$ //$NON-NLS-12$

		
		
		
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			Log.w(TAG, "Upgrading database from version " + oldVersion + " to " //$NON-NLS-1$ //$NON-NLS-2$
					+ newVersion + ", which will destroy all old data"); //$NON-NLS-1$
			db.execSQL("DROP TABLE IF EXISTS tasks"); //$NON-NLS-1$
			onCreate(db);// to create db
		}
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// ---register a user into the database---
	public long registerUser(String username, String usertype, String password,
			String firstname, String lastname, String fathername,
			String mobile, String emailid, String address, String date,
			String time, String status) {
		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_USERNAME, username);
		initialValues.put(KEY_PASSWORD, password);
		initialValues.put(KEY_USERTYPE, usertype);
		initialValues.put(KEY_FIRSTNAME, firstname);
		initialValues.put(KEY_LASTNAME, lastname);
		initialValues.put(KEY_FATHERNAME, fathername);
		initialValues.put(KEY_MOBILE, mobile);
		initialValues.put(KEY_EMAILID, emailid);
		initialValues.put(KEY_ADDRESS, address);
		initialValues.put(KEY_DATE, date);
		initialValues.put(KEY_TIME, time);
		initialValues.put(KEY_STATUS, status);

		return db.insert(DATABASE_TABLE, null, initialValues);

	}





	

	
	public String getDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		// DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");

		Calendar cal = Calendar.getInstance();

		String reg_date = dateFormat.format(cal.getTime());// "11/03/14 12:33:43";
		// String reg_time = dateFormat1.format(cal.getTime());
		return reg_date;

	}

	public String getTime() {
		// DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		DateFormat dateFormat1 = new SimpleDateFormat("HH:mm:ss");

		Calendar cal = Calendar.getInstance();

		// String reg_date = dateFormat.format(cal.getTime());//
		// "11/03/14 12:33:43";
		String reg_time = dateFormat1.format(cal.getTime());
		return reg_time;

	}

	// ---retrieves all the contacts---
	public Cursor getAllRegisteredActiveUser() {

		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_USERNAME,
				KEY_USERTYPE, KEY_FIRSTNAME, KEY_LASTNAME, KEY_FATHERNAME,
				KEY_MOBILE, KEY_ADDRESS, KEY_EMAILID, KEY_DATE, KEY_TIME,
				KEY_STATUS },
				KEY_STATUS + "=" + "'True'", null, null, null, null); //$NON-NLS-1$ //$NON-NLS-2$

	}

	public Cursor getAllRegisteredUser() {

		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_USERNAME,
				KEY_USERTYPE, KEY_FIRSTNAME, KEY_LASTNAME, KEY_FATHERNAME,
				KEY_MOBILE, KEY_ADDRESS, KEY_EMAILID, KEY_DATE, KEY_TIME,
				KEY_STATUS }, null, null, null, null, null);

	}

	// ---retrieves a particular contact---
	public Cursor getUserById(long rowId) throws SQLException {
		Cursor mCursor = db.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_USERNAME, KEY_USERTYPE, KEY_FIRSTNAME, KEY_LASTNAME,
				KEY_FATHERNAME, KEY_MOBILE, KEY_ADDRESS, KEY_EMAILID, KEY_DATE,
				KEY_TIME, KEY_STATUS },
				KEY_ROWID + "=" + rowId, null, null, null, null); //$NON-NLS-1$

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getUserByUsername(String username) throws SQLException {
		Cursor mCursor = db
				.query(DATABASE_TABLE, new String[] { "*" }, KEY_USERNAME
						+ "=" + "'" + username + "'", null, null, null, null); //$NON-NLS-1$

		System.out.println(KEY_USERNAME + "=" + "'" + username + "'");
		if (mCursor != null) {
			return mCursor;
		}
		return mCursor;
	}

	public Cursor getUserByUsername(long rowId) throws SQLException {
		Cursor mCursor = db.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_USERNAME, KEY_USERTYPE, KEY_FIRSTNAME, KEY_LASTNAME,
				KEY_FATHERNAME, KEY_MOBILE, KEY_ADDRESS, KEY_EMAILID, KEY_DATE,
				KEY_TIME, KEY_STATUS },
				KEY_ROWID + "=" + rowId, null, null, null, null); //$NON-NLS-1$

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor getUserMobileByEmail(String email) throws SQLException {
		Cursor mCursor = db.query(DATABASE_TABLE, new String[] { KEY_MOBILE,
				KEY_PASSWORD },
				KEY_EMAILID + "=" + "'" + email + "'", null, null, null, null); //$NON-NLS-1$

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	
	//
	public boolean validateUser(String username, String password)
			throws SQLException {
		boolean b = false;
		Cursor mCursor = db
				.query(DATABASE_TABLE,
						new String[] { KEY_USERNAME, KEY_PASSWORD, KEY_STATUS },
						KEY_USERNAME
								+ "=" + "'" + username + "'" + " AND " + KEY_PASSWORD + "=" + "'" + password + "'", null, null, null, null); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
		int val = mCursor.getCount();
		System.out.println("cursor count=" + val);
		if (val > 0) {
			b = true;
		}
		mCursor.close();
		return b;
	}

	public Cursor getAllRegisteredUserWithPassword() {

		return db.query(DATABASE_TABLE, new String[] { KEY_ROWID, KEY_USERNAME,
				KEY_USERTYPE, KEY_PASSWORD, KEY_FIRSTNAME, KEY_EMAILID,
				KEY_STATUS }, null, null, null, null, null);

	}

	// ---updates a contact---
	public int updateContact(String username/*
											 * , String password, String
											 * firstname
											 */, String lastname,
			String mobile, String emailid, String address, String fathername) {
		ContentValues args = new ContentValues();
		// args.put(KEY_FIRSTNAME, firstname);
		args.put(KEY_LASTNAME, lastname);
		args.put(KEY_FATHERNAME, fathername);
		args.put(KEY_MOBILE, mobile);
		args.put(KEY_EMAILID, emailid);
		args.put(KEY_ADDRESS, address);

		return db.update(DATABASE_TABLE, args, KEY_USERNAME
				+ "=" + "'" + username + "'", null); //$NON-NLS-1$

	}

	// ---updates a contact---
	public int updateMobile(String username, String mobile) {
		ContentValues args = new ContentValues();
		// args.put(KEY_FIRSTNAME, firstname);

		args.put(KEY_MOBILE, mobile);

		return db.update(DATABASE_TABLE, args, KEY_USERNAME
				+ "=" + "'" + username + "'", null); //$NON-NLS-1$

	}

	public int updatePassword(String username, String password) {
		ContentValues args = new ContentValues();

		args.put(KEY_PASSWORD, password);

		return db.update(DATABASE_TABLE, args, KEY_USERNAME
				+ "=" + "'" + username + "'", null); //$NON-NLS-1$

	}

	public int deactivateUser(String username) {

		ContentValues args = new ContentValues();

		args.put(KEY_STATUS, "False");

		return db.update(DATABASE_TABLE, args, KEY_USERNAME
				+ "=" + "'" + username + "'", null); //$NON-NLS-1$

	}

	public int activateUser(String username) {

		ContentValues args = new ContentValues();

		args.put(KEY_STATUS, "True");

		return db.update(DATABASE_TABLE, args, KEY_USERNAME
				+ "=" + "'" + username + "'", null); //$NON-NLS-1$

	}



}
