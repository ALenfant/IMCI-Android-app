package com.imci.ica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import android.database.Cursor;

/**
 * Class with Javascript Utils
 * 
 * @author Miguel
 * 
 */
public class JSUtils {

	public final static String baseJS = "function AT_LEAST_TWO_OF(){var trueargs = 0;for (var i = 0; i < arguments.length; ++i) {if (arguments[i]) {trueargs++;}} return (trueargs >= 2);}";

	/**
	 * Converts a HashMap to its Javascript equivalent with format of
	 * classifications equations
	 * 
	 * @param hashmap
	 *            the hashmap to convert
	 * @param variableName
	 *            the target Javascript variable's name
	 * @return a String containign the Javascript code necessary to set all the
	 *         variables corresponding to the HashMap
	 */
	public static String hashMapToJavascript(HashMap<String, Object> hashmap,
			String variableName) {
		if (hashmap == null || hashmap.isEmpty()) {
			return "";
		}
		StringBuilder builder = new StringBuilder("data={};");
		Iterator<Entry<String, Object>> iterator = hashmap.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> pair = (Map.Entry<String, Object>) iterator
					.next();
			// System.out.println(pair.getKey() +" = "+
			// pair.getValue().toString());
			if (pair.getValue() instanceof String) {
				String value = ((String) pair.getValue()).replaceAll("'", "\'");
				builder.append(variableName + "['" + pair.getKey() + "']='"
						+ value + "';");
			} else {
				builder.append(variableName + "['" + pair.getKey() + "']="
						+ pair.getValue() + ";");
			}
		}
		return builder.toString();
	}

	/**
	 * Evaluate the equations of a equations content in cursor
	 * 
	 * @param age_group
	 * 				age group of questions
	 * @param jsDataVar
	 * 				string with Javascript format
	 * @param cursor
	 * 				cursor that contains equations
	 * @return an array with id's of diagnostic obtained
	 */
	public static ArrayList<Integer> evaluation(int age_group,
			String jsDataVar, Cursor cursor) {

		// Will contain the results to display
		ArrayList<Integer> results = new ArrayList<Integer>();

		// Get all the equations...
		do {
			String eqToEv = cursor.getString(2);

			if (!eqToEv.equals("")) {

				String eq = jsDataVar + " " + baseJS + " "
						+ cursor.getString(2);// " !(data['danger.boire'] || data['danger.vomit'] || data['danger.convulsions_passe'] || data['danger.lethargie'] || data['danger.convulsions_present'])";
				// System.out.println(eq);
				Context context = Context.enter();
				context.setOptimizationLevel(-1); // Disable compilation

				try {
					Scriptable scope = context.initStandardObjects();

					// scope.put("data", scope, data);

					Boolean result = (Boolean) context.evaluateString(scope,
							eq, "doit", 1, null);
					// System.out.println("Result: " + result);
					if (result) {
						results.add(cursor.getInt(cursor.getColumnIndex("_id")));
					}
				} catch (Exception ex) {
					if (ex instanceof java.lang.ClassCastException) {
						System.out.println("Result: Unknown");
					} else {
						System.out.println("Result ERROR: " + ex.toString());
					}
				} finally {
					Context.exit();
				}
			}
		} while (cursor.moveToNext());

		return results;

	}

}
