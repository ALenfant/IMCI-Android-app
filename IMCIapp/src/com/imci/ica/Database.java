package com.imci.ica;

import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Class managing the SQLite database Uses SQLiteAssetHelper
 * (https://github.com/jgilfelt/android-sqlite-asset-helper) to preload the
 * database if necessary
 * 
 * @author Antonin
 * 
 */
public class Database extends SQLiteAssetHelper {
	private static final String DATABASE_NAME = "database"; // Database filename
															// (SQLiteAssetHelper-related)
	private static final int DATABASE_VERSION = 1; // Database version
													// (SQLiteAssetHelper-related)

	private static final String PASSWORD_SALT = "5K1VLh4W3bfRwRwo2OvSJ42NL6sSUAZjS6KlIMCI"; // Salt
																							// used
																							// for
																							// passwords

	public Database(Context context) {
		// We call the parent method (provided by SQLiteAssetHelper)
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	// Data insertion and fetching methods
	/**
	 * Get the various signs to check for
	 * 
	 * @return the various signs to check for
	 */
	public Cursor getSigns() {
		SQLiteDatabase db = getReadableDatabase();
		// SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		/*
		 * String [] sqlSelect = {"0 id", "illness_id", "type", "key",
		 * "question"}; String sqlTables = "Employees";
		 * 
		 * qb.setTables("signs"); Cursor c = qb.query(db, sqlSelect, null, null,
		 * null, null, null);
		 */
		Cursor c = db
				.rawQuery(
						"SELECT `illness_id`, `illnesses`.`key` `illness_key`, `name`, `type`, `signs`.`key` `sign_key`, `question`, `values` FROM `illnesses` "
								+ "INNER JOIN `signs` ON `signs`.`illness_id` = `illnesses`.`_id` "
								+ "WHERE `signs`.`age_group` = 2 "
								+ "ORDER BY `illnesses`.`sequence`",
						new String[0]);
		// c.moveToFirst(); //Check if bugs?

		db.close();
		return c;
	}

	/**
	 * Get all the children zones to the zone parent_id
	 * 
	 * @param parent_id
	 *            the parent zone whose children zones we want to get
	 * @return all the children zones to the zone parent_id
	 */
	public Cursor getZones(int parent_id) {
		SQLiteDatabase db = getReadableDatabase();
		// SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		/*
		 * String [] sqlSelect = {"0 id", "illness_id", "type", "key",
		 * "question"}; String sqlTables = "Employees";
		 * 
		 * qb.setTables("signs"); Cursor c = qb.query(db, sqlSelect, null, null,
		 * null, null, null);
		 */
		Cursor c = db.rawQuery(
				"SELECT `_id`, `name` FROM `zones` WHERE `parent_id` = "
						+ parent_id, new String[0]);
		c.moveToFirst();

		db.close();
		return c;
	}

	/**
	 * Get the details about a specific zone
	 * 
	 * @param id
	 *            the id of the zone we want to get
	 * @return the details about a specific zone
	 */
	public Cursor getZone(int id) {
		SQLiteDatabase db = getReadableDatabase();
		// SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		/*
		 * String [] sqlSelect = {"0 id", "illness_id", "type", "key",
		 * "question"}; String sqlTables = "Employees";
		 * 
		 * qb.setTables("signs"); Cursor c = qb.query(db, sqlSelect, null, null,
		 * null, null, null);
		 */
		Cursor c = db.rawQuery(
				"SELECT `_id`, `name` FROM `zones` WHERE `_id` = " + id,
				new String[0]);
		c.moveToFirst();

		db.close();
		return c;
	}

	/**
	 * Get the name of a specific zone
	 * 
	 * @param id
	 *            the id of the zone whose name we want
	 * @return the name of a specific zone
	 */
	public String getNameOfZone(int id) {
		String str;
		Cursor c = getZone(id);
		if (c.getCount() > 0)
			str = c.getString(1);
		else
			str = "";

		return str;
	}

	/**
	 * Register a New Patient into Database
	 * 
	 * @param village_id
	 *            his village id
	 * @param first_name
	 *            his first name
	 * @param last_name
	 *            his last name
	 * @param gender
	 *            his gender
	 * @param born_on
	 *            his birth date
	 * @return if the operation succeeded
	 */
	public boolean insertNewPatient(int village_id, String first_name,
			String last_name, String gender, String born_on) {
		SQLiteDatabase db = getWritableDatabase();

		// Check database is right opened
		if (db != null) {
			// We set the values to be inserted...
			ContentValues values = new ContentValues();

			try {
				values.put("village_id", village_id);
				values.put("first_name", first_name);
				values.put("last_name", last_name);
				values.put("gender", gender);
				values.put("born_on", born_on);

				String currentdatetime = getCurrentDateTime();
				values.put("created_at", currentdatetime);
				values.put("updated_at", currentdatetime);

				long resultID = db.insert("children", null, values);
				if (resultID == -1)
					return false;
			} catch (Exception e) {
				return false;
			}
			db.close();

			return true;

		} else {
			return false;
		}

	}

	/**
	 * Add an application user (normally medical personnel)
	 * 
	 * @param name
	 *            his full name
	 * @param password
	 *            his password
	 * @param administrator
	 *            if he's an administrator
	 * @param zone_id
	 *            his zone id
	 * @param global_id
	 *            his global id (normally zone_name+"/"+user_number(for this
	 *            zone), not respected here)
	 * @return if the operation succeeded
	 */
	// Global id = zone_name/user_number
	public Boolean addUser(String name, String password, Boolean administrator,
			int zone_id, String global_id) {
		SQLiteDatabase db = getWritableDatabase();

		// Check database is right opened
		if (db != null) {
			// We set the values to be inserted...
			ContentValues values = new ContentValues();

			try {
				values.put("name", name);
				values.put("crypted_password",
						MD5.md5(PASSWORD_SALT + password));
				values.put("admin", administrator ? "t" : "f");
				values.put("zone_id", zone_id);
				values.put("global_id", global_id);

				String currentdatetime = getCurrentDateTime();
				values.put("created_at", currentdatetime);
				values.put("updated_at", currentdatetime);

				long resultID = db.insert("users", null, values);
				if (resultID == -1)
					return false;
			} catch (Exception e) {
				return false;
			}
			db.close();

			return true;

		} else {
			return false;
		}
	}

	/**
	 * Try to login with the provided information
	 * 
	 * @param name
	 *            the name to login with
	 * @param password
	 *            the password to login with
	 * @return if the operation succeeded
	 */
	public boolean LoginUser(String name, String password) {
		SQLiteDatabase db = getReadableDatabase();
		// Check database is right opened
		if (db != null) {

			try {
				Cursor mCursor = db
						.query("users",
								new String[] { "_id", "name", "admin" },
								"name=? AND crypted_password=?",
								new String[] { name,
										MD5.md5(PASSWORD_SALT + password) },
								null, null, null);

				if (mCursor.getCount() == 0)
					return false;

				mCursor.moveToFirst();
				Boolean admin = mCursor.getString(2).equals("t") ? true : false;
				User user = new User(mCursor.getInt(0), mCursor.getString(1),
						admin); // We create the user
				ApplicationPreferences.loggedin_user = user; // We set the user
																// as logged in

				System.out.println("LOGIN OKAY");
				mCursor.close();
				db.close();
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Get all the application users
	 * 
	 * @return all the application users
	 */
	public Cursor getUsers() {
		SQLiteDatabase db = getReadableDatabase();

		Cursor mCursor = db.query("users", new String[] { "_id", "name",
				"admin" }, "", new String[] {}, null, null, null);

		mCursor.moveToFirst();

		return mCursor;
	}

	/**
	 * Get a specific application user
	 * 
	 * @param userId
	 *            the id of the user
	 * @return the user
	 */
	public Cursor getUser(int userId) {
		SQLiteDatabase db = getReadableDatabase();

		Cursor mCursor = db.query("users", new String[] { "_id", "name",
				"admin" }, "_id=?", new String[] { Integer.toString(userId) },
				null, null, null);

		mCursor.moveToFirst();

		return mCursor;
	}

	/**
	 * Edit a specific application user
	 * 
	 * @param userId
	 *            the id of the user to edit
	 * @param name
	 *            his full name
	 * @param password
	 *            his password
	 * @param administrator
	 *            if he's an administrator
	 * @param zone_id
	 *            his zone id
	 * @param global_id
	 *            his global id
	 * @return if the operation succeeded
	 */
	public Boolean editUser(int userId, String name, String password,
			Boolean administrator, int zone_id, String global_id) {
		SQLiteDatabase db = getWritableDatabase();

		// Check database is right opened
		if (db != null) {
			// We set the values to be inserted...
			ContentValues values = new ContentValues();

			try {
				values.put("name", name);
				if (password.length() != 0) {
					values.put("crypted_password",
							MD5.md5(PASSWORD_SALT + password));
				}
				values.put("admin", administrator ? "t" : "f");
				values.put("zone_id", zone_id);
				values.put("global_id", global_id);

				String currentdatetime = getCurrentDateTime();
				values.put("updated_at", currentdatetime);

				long resultID = db.update("users", values, "_id=?",
						new String[] { Integer.toString(userId) });
				if (resultID == -1)
					return false;
			} catch (Exception e) {
				return false;
			}
			db.close();

			return true;

		} else {
			return false;
		}
	}

	/**
	 * Get all the classifications
	 * 
	 * @return all the classifications
	 */
	public Cursor getClassifications() {
		SQLiteDatabase db = getReadableDatabase();

		Cursor mCursor = db.query("classifications", new String[] { "_id",
				"name", "equation", "level" }, "", new String[] {}, null, null,
				null);

		mCursor.moveToFirst();

		return mCursor;
	}

	/**
	 * Get the patients corresponding to the criterias provided
	 * 
	 * @param first_name
	 *            the first name to look for
	 * @param last_name
	 *            the last name to look for
	 * @param gender
	 *            the gender to look for
	 * @param born_on
	 *            the birth date to look for
	 * @param village_id
	 *            the village id to look for
	 * @return the patients corresponding to the criterias
	 */
	public Cursor getPatients(String first_name, String last_name,
			String gender, String born_on, int village_id) {

		// String genderStr;
		// if (gender) {
		// genderStr = "t";
		// } else {
		// genderStr = "f";
		// }

		SQLiteDatabase db = getReadableDatabase();

		Cursor mCursor = db.rawQuery(
				"SELECT `_id`, `first_name`, `last_name`, `gender`, `born_on`, "
						+ "`village_id` FROM `children` WHERE `last_name`=\""
						+ last_name
						+ "\"" // + "\" AND `gender`=\"" + genderStr
						+ " AND CASE WHEN \""
						+ first_name
						+ "\" <> '' THEN `first_name`=\""
						+ first_name
						+ "\" ELSE `first_name`=`first_name` END"
						+ " AND CASE WHEN \""
						+ gender
						+ "\" <> '' THEN `gender`=\""
						+ gender
						+ "\" ELSE `gender`=`gender` END"
						+ " AND CASE WHEN \""
						+ born_on
						+ "\" <> '' THEN `born_on`=\""
						+ born_on
						+ "\" ELSE `born_on`=`born_on` END"
						+ " AND CASE WHEN "
						+ village_id
						+ " <> -1 THEN `village_id`="
						+ village_id
						+ " ELSE `village_id`=`village_id` END", null);

		if (mCursor.getCount() != 0)
			mCursor.moveToFirst();

		// mCursor = null;
		// else

		db.close();
		return mCursor;
	}

	/**
	 * Get a specific patient
	 * 
	 * @param id
	 *            the patient's id
	 * @return the selected patient
	 */
	public Cursor getPatientById(int id) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT * FROM `children` WHERE `_id` = "
				+ id, new String[0]);
		mCursor.moveToFirst();

		db.close();
		return mCursor;
	}

	/**
	 * Get all the illnesses
	 * 
	 * @return all the illnesses
	 */
	public Cursor getIllnesses() {
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCursor = db.rawQuery("SELECT `_id`, `key` FROM `illnesses`",
				new String[0]);
		mCursor.moveToFirst();

		db.close();
		return mCursor;
	}

	/**
	 * Get a specific illness
	 * 
	 * @param id
	 *            the illness' id
	 * @return the selected illness
	 */
	public String getIllnessKey(int id) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCursor = db.rawQuery(
				"SELECT `key` FROM `illnesses` WHERE `_id` = " + id,
				new String[0]);
		mCursor.moveToFirst();

		String key = mCursor.getString(mCursor.getColumnIndex("key"));
		mCursor.close();
		db.close();
		return key;
	}

	/**
	 * Get all the questions corresponding to the provided age group
	 * 
	 * @param age_group
	 *            the age group whose questions we want
	 * @return the questions corresponding to the provided age group
	 */
	public Cursor getQuestions(int age_group) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCursor = db
				.rawQuery(
						"SELECT `_id`, `illness_id`, `type`, `key`, `question`, `values`, `dep`, `negative` FROM `signs` WHERE `age_group`="
								+ age_group, new String[0]);
		mCursor.moveToFirst();

		db.close();
		return mCursor;
	}

	/**
	 * Get the diagnostics data for a specific child
	 * 
	 * @param id
	 *            the child id
	 * @return the diagnostics data for the specificed child
	 */
	public Cursor getChildrenDiagnostics(int id) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCursor = db
				.rawQuery(
						"SELECT `children`.`born_on`, `diagnostics`.`mac`, `diagnostics`.`weight`, `diagnostics`.`height` FROM `children`"
								+ "INNER JOIN `diagnostics` ON `children`.`global_id` = `diagnostics`.`child_global_id`"
								+ "WHERE `children`.`_id` =" + id,
						new String[0]);
		mCursor.moveToFirst();

		db.close();
		return mCursor;
	}

	// Helper functions
	/**
	 * Gives the current date and time adapted to a SQLite datetime column
	 * 
	 * @return the current time in SQLite datetime format
	 */
	@SuppressLint("SimpleDateFormat")
	private String getCurrentDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}
}

/**
 * Class to use MD5 hashing
 * 
 * @author http://mobile.dzone.com/news/android-snippet-making-md5
 * 
 */
class MD5 {
	/**
	 * Computes the MD5 hash of the given string
	 * 
	 * @param str
	 *            the string we want to hash
	 * @return the MD5 hash of the string
	 * @throws Exception
	 */
	public static String md5(String str) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (byte b : md5(str.getBytes()))
			sb.append(Integer.toHexString(0x100 + (b & 0xff)).substring(1));
		return sb.toString();
	}

	/**
	 * Computes the MD5 hash of the given bytes array
	 * 
	 * @param data
	 *            the data we want to hash
	 * @return the MD5 hash of the bytes array
	 * @throws Exception
	 */
	public static byte[] md5(byte[] data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(data);
		return md5.digest();
	}
}
