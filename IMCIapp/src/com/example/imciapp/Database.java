package com.example.imciapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

;

public class Database extends SQLiteAssetHelper {

	private static final String DATABASE_NAME = "database";
	private static final int DATABASE_VERSION = 1;

	public Database(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public Cursor getSigns() {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

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
		return c;
	}

	public Cursor getZones(int parent_id) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

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
		return c;
	}
	
	public Cursor getZone(int id) {
		SQLiteDatabase db = getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();

		/*
		 * String [] sqlSelect = {"0 id", "illness_id", "type", "key",
		 * "question"}; String sqlTables = "Employees";
		 * 
		 * qb.setTables("signs"); Cursor c = qb.query(db, sqlSelect, null, null,
		 * null, null, null);
		 */
		Cursor c = db.rawQuery(
				"SELECT `_id`, `name` FROM `zones` WHERE `_id` = "
						+ id, new String[0]);
		c.moveToFirst();
		return c;
	}

	// SELECT * FROM zones WHERE IFNULL(parent_id,0) = 0
}
