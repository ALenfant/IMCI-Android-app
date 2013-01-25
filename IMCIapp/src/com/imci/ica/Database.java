package com.imci.ica;

import java.security.MessageDigest;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

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

	Context mContext;

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
		mContext = context;
	}

	// Data insertion and fetching methods
	/**
	 * Get the various signs to check for
	 * 
	 * @return the various signs to check for
	 */
	public Cursor getSigns() {
		SQLiteDatabase db = getReadableDatabase();

		Cursor c = db
				.rawQuery(
						"SELECT `illness_id`, `illnesses`.`key` `illness_key`, `name`, `type`, `signs`.`key` `sign_key`, `question`, `values` FROM `illnesses` "
								+ "INNER JOIN `signs` ON `signs`.`illness_id` = `illnesses`.`_id` "
								+ "WHERE `signs`.`age_group` = 2 "
								+ "ORDER BY `illnesses`.`sequence`",
						new String[0]);

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
	public Cursor getZonesInside(int parent_id) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(
				"SELECT `_id`, `name` FROM `zones` WHERE `parent_id` = "
						+ parent_id, new String[0]);
		c.moveToFirst();

		db.close();
		return c;
	}

	/**
	 * Get the parent zone for the village given
	 * 
	 * @param village_id
	 * 
	 * @return the parent zone
	 */
	public int getParentZone(int village_id) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor c = db.rawQuery(
				"SELECT `parent_id`, `name` FROM `zones` WHERE `_id` = "
						+ village_id, new String[0]);
		c.moveToFirst();

		int parent_id = c.getInt(c.getColumnIndex("parent_id"));
		c.close();
		db.close();
		return parent_id;
	}

	/**
	 * Get the details about a specific zone
	 * 
	 * @param id
	 *            the id of the zone we want to get
	 * @return the details about a specific zone
	 */
	public Cursor getZoneById(int id) {
		SQLiteDatabase db = getReadableDatabase();

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
		Cursor c = getZoneById(id);
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

		int zone_id = getParentZone(village_id);

		String global_id = new String(getNameOfZone(zone_id) + "/"
				+ getAutoIncrements("'children'"));

		Toast.makeText(mContext, global_id, Toast.LENGTH_LONG).show();

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
				values.put("global_id", global_id);
				values.put("zone_id", zone_id);

				String currentdatetime = DateUtils.getCurrentDateTime();
				values.put("created_at", currentdatetime);
				values.put("updated_at", currentdatetime);

				long resultID = db.insert("children", null, values);
				if (resultID == -1)
					return false;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
			db.close();

			return true;

		} else {
			return false;
		}

	}

	/**
	 * Get the next id for a table with autoincrement id.
	 * 
	 * @param table
	 * @return the next id
	 */
	public String getAutoIncrements(String table) {
		SQLiteDatabase db = getWritableDatabase();
		String query = "SELECT * FROM SQLITE_SEQUENCE WHERE `name` = " + table;
		Cursor cursor = db.rawQuery(query, null);
		Integer id;
		if (cursor.moveToFirst()) {
			do {
				System.out.println("tableName: "
						+ cursor.getString(cursor.getColumnIndex("name")));
				System.out.println("autoInc: "
						+ cursor.getString(cursor.getColumnIndex("seq")));
				id = cursor.getInt(cursor.getColumnIndex("seq")) + 1;

			} while (cursor.moveToNext());

		} else {
			id = 1;
		}

		cursor.close();
		db.close();

		return id.toString();
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
			int zone_id) {

		String global_id = new String(getNameOfZone(zone_id) + "/"
				+ getAutoIncrements("'users'"));

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

				String currentdatetime = DateUtils.getCurrentDateTime();
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
				db.close();
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
	public Cursor getAllUsers() {
		SQLiteDatabase db = getReadableDatabase();

		Cursor mCursor = db.query("users", new String[] { "_id", "name",
				"admin" }, "", new String[] {}, null, null, null);

		mCursor.moveToFirst();

		return mCursor;
	}

	/**
	 * Get a specific application user by his Id
	 * 
	 * @param userId
	 *            the id of the user
	 * @return the user
	 */
	public Cursor getUserById(int userId) {
		SQLiteDatabase db = getReadableDatabase();

		Cursor mCursor = db.query("users", new String[] { "_id", "name",
				"admin" }, "_id=?", new String[] { Integer.toString(userId) },
				null, null, null);

		mCursor.moveToFirst();
		db.close();
		return mCursor;
	}

	/**
	 * Get a specific application user by his Name
	 * 
	 * @param userName
	 *            the id of the user
	 * @return the user
	 */
	public Cursor getUserByName(String userName) {
		SQLiteDatabase db = getReadableDatabase();

		Cursor mCursor = db.query("users", new String[] { "_id", "name",
				"admin", "global_id" }, "name=?", new String[] { userName },
				null, null, null);

		mCursor.moveToFirst();
		db.close();
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

				String currentdatetime = DateUtils.getCurrentDateTime();
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
	 * @param age_group
	 *            the age group whose classifications we want
	 * 
	 * @return all the classifications
	 */
	public Cursor getClassifications(int age_group) {
		SQLiteDatabase db = getReadableDatabase();

		Cursor mCursor = db.query("classifications", new String[] { "_id",
				"name", "equation", "level" }, "age_group = ?",
				new String[] { Integer.toString(age_group) }, null, null, null);

		mCursor.moveToFirst();
		db.close();
		return mCursor;
	}

	/**
	 * Get text of a classification
	 * 
	 * @param id
	 *            of classification
	 * 
	 * @return text of classification
	 */
	public String getClassificationText(int id) {
		SQLiteDatabase db = getReadableDatabase();

		Cursor mCursor = db.query("classifications", new String[] { "_id",
				"name" }, "_id = ?", new String[] { Integer.toString(id) },
				null, null, null);

		mCursor.moveToFirst();

		String text = mCursor.getString(mCursor.getColumnIndex("name"));

		mCursor.close();
		db.close();
		return text;
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
	public Cursor getPatientDiagnostics(int id) {
		SQLiteDatabase db = getReadableDatabase();
		Cursor mCursor = db
				.rawQuery(
						"SELECT `diagnostics`.`mac`, `diagnostics`.`weight`, `diagnostics`.`height`, `diagnostics`.`temperature` FROM `children`"
								+ "INNER JOIN `diagnostics` ON `children`.`global_id` = `diagnostics`.`child_global_id`"
								+ "WHERE `children`.`_id` =" + id,
						new String[0]);
		mCursor.moveToLast();

		db.close();
		return mCursor;
	}

	/**
	 * Save in system the got measures for a patient
	 * 
	 * @param child_global_id
	 * @param muac
	 * @param height
	 * @param weight
	 * @param temp
	 * @param age_group
	 * @param zone_id
	 * @return true if process is successful
	 */
	public String savePatientDiagnostic(String child_global_id, String muac,
			String height, String weight, String temp, int age_group,
			int zone_id, String born_on) {

		String global_id = new String(getNameOfZone(zone_id) + "/"
				+ getAutoIncrements("'diagnostics'"));

		String author = ApplicationPreferences.getRememberedUserName(mContext);
		Cursor authorCursor = getUserByName(author);
		String author_global_id = authorCursor.getString(authorCursor
				.getColumnIndex("global_id"));

		SQLiteDatabase db = getWritableDatabase();
		// Check database is right opened
		if (db != null) {
			// We set the values to be inserted...
			ContentValues values = new ContentValues();

			try {
				values.put("child_global_id", child_global_id);
				values.put("author_global_id", author_global_id);
				values.put("mac", muac);
				values.put("height", height);
				values.put("weight", weight);
				values.put("temperature", temp);
				values.put("saved_age_group", age_group);
				values.put("global_id", global_id);
				values.put("born_on", born_on);
				values.put("state", "closed");

				String currentdatetime = DateUtils.getCurrentDateTime();
				values.put("done_on", currentdatetime);
				values.put("created_at", currentdatetime);
				values.put("updated_at", currentdatetime);

				long resultID = db.insert("diagnostics", null, values);
				db.close();

				if (resultID == -1)
					return "-1";
			} catch (Exception e) {
				return "-1";
			}
			return global_id;

		} else {
			return "-1";
		}
	}

	/**
	 * Save in system got results for a patient
	 * 
	 * @param classification_id
	 *            the result for presented symptoms
	 * @param diagnostic_global_id
	 *            the diagnostic id
	 * @param zone_id
	 *            the zone id
	 * @return true if process is successful
	 */
	public boolean saveDiagnosticResults(int classification_id,
			String diagnostic_global_id, int zone_id) {
		String global_id = new String(getNameOfZone(zone_id) + "/"
				+ getAutoIncrements("'results'"));

		SQLiteDatabase db = getWritableDatabase();
		// Check database is right opened
		if (db != null) {
			// We set the values to be inserted...
			ContentValues values = new ContentValues();

			try {
				values.put("classification_id", classification_id);
				values.put("diagnostic_global_id", diagnostic_global_id);
				values.put("zone_id", zone_id);
				values.put("global_id", global_id);

				String currentdatetime = DateUtils.getCurrentDateTime();
				values.put("created_at", currentdatetime);
				values.put("updated_at", currentdatetime);

				long resultID = db.insert("results", null, values);
				db.close();

				if (resultID == -1)
					return false;
			} catch (Exception e) {
				return false;
			}
			return true;

		} else {
			return false;
		}

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
