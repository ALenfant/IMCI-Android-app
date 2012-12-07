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

;

public class Database extends SQLiteAssetHelper {
	private static final String DATABASE_NAME = "database";
	private static final int DATABASE_VERSION = 1;

	private static final String PASSWORD_SALT = "5K1VLh4W3bfRwRwo2OvSJ42NL6sSUAZjS6KlIMCI";

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

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

	// SELECT * FROM zones WHERE IFNULL(parent_id,0) = 0

	// Register a New Patient into Database
	public boolean insertNewPatient(int village_id, String first_name,
			String last_name, boolean gender, String born_on) {
		SQLiteDatabase db = getWritableDatabase();

		// Check database is right opened
		if (db != null) {
			// We set the values to be inserted...
			ContentValues values = new ContentValues();

			try {
				values.put("village_id", village_id);
				values.put("first_name", first_name);
				values.put("last_name", last_name);
				values.put("gender", gender ? "t" : "f");
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

	public boolean LoginUser(String name, String password) {
		SQLiteDatabase db = getReadableDatabase();
		// Check database is right opened
		if (db != null) {

			try {
				Cursor cursor = db
						.query("users",
								new String[] { "name", "admin" },
								"name=? AND crypted_password=?",
								new String[] { name,
										MD5.md5(PASSWORD_SALT + password) },
								null, null, null);
				// Cursor cursor = db.query("users", new String[] {"name",
				// "admin",
				// "crypted_password"}, "name LIKE ?", new String[] {name},
				// null,
				// null, null);
				// Cursor cursor = db.query("users", new String[] {"name",
				// "admin",
				// "crypted_password"}, "crypted_password = ?", new String[]
				// {MD5.md5(PASSWORD_SALT + password)}, null, null, null);
				if (cursor.getCount() == 0)
					return false;
				cursor.moveToFirst();
				// dbname = cursor.getString(0);
				// String admin = cursor.getString(1);
				/*
				 * System.out.println("DBNAME : " + dbname);
				 * System.out.println("  NAME : " + name);
				 * System.out.println("PWD IN DB : " + cursor.getString(2));
				 * System.out.println("PWD ENCOD : " + MD5.md5(PASSWORD_SALT +
				 * password));
				 */
				System.out.println("LOGIN OKAY");
				cursor.close();
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

	public Cursor getPatients(String first_name, String last_name,
			boolean gender, String born_on, int village_id) {

		String genderStr;
		if (gender) {
			genderStr = "t";
		} else {
			genderStr = "f";
		}

		SQLiteDatabase db = getReadableDatabase();
		
		Cursor c = db.rawQuery(
				"SELECT _id, first_name, last_name, gender, born_on, "
						+ "village_id FROM children WHERE last_name=\""
						+ last_name
						+ "\" AND gender=\""
						+ genderStr
						+ "\" AND CASE WHEN \""
						+ first_name
						+ "\" <> '' THEN first_name=\""
						+ first_name
						+ "\" ELSE first_name=first_name END"
						+ " AND CASE WHEN \""
						+ born_on
						+ "\" <> '' THEN born_on=\""
						+ born_on
						+ "\" ELSE born_on=born_on END"
						+ " AND CASE WHEN "
						+ village_id
						+ " <> -1 THEN village_id="
						+ village_id + " ELSE village_id=village_id END", null);
		
		if (c.getCount()==0) {
			c = null;
		}
		db.close();
		
		
		//c.moveToFirst();
		return c;
	}
}

class MD5 {
	public static String md5(String str) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (byte b : md5(str.getBytes()))
			sb.append(Integer.toHexString(0x100 + (b & 0xff)).substring(1));
		return sb.toString();
	}

	public static byte[] md5(byte[] data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(data);
		return md5.digest();
	}
}
