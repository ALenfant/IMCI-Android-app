package com.imci.ica;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.mozilla.javascript.*;

/**
 * Class responsible for the Signs Classification activity. Uses the answers
 * from GetsignsActivity and data about the child to compute the equations
 * stored in the database's classifications table to see if there is a problem
 * or not
 * 
 * @author Antonin
 * 
 */
public class SignsClassificationActivity extends Activity {
	public final static String EXTRA_ID_PATIENT = "com.imci.ica.ID_PATIENT";
	public final static String EXTRA_HASHMAP_DATA = "com.imci.ica.HASHMAP_DATA";
	public final static String baseJS = "function AT_LEAST_TWO_OF(){var trueargs = 0;for (var i = 0; i < arguments.length; ++i) {if (arguments[i]) {trueargs++;}} return (trueargs >= 2);}";

	private int patient_id; // the patient's id

	@SuppressLint("SimpleDateFormat")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signs_classification);

		@SuppressWarnings("unchecked")
		// We load the data of the answers given by the previous activity
		HashMap<String, Object> data = (HashMap<String, Object>) getIntent()
				.getSerializableExtra(EXTRA_HASHMAP_DATA);

		// And the patient id
		patient_id = getIntent().getIntExtra(EXTRA_ID_PATIENT, -1);

		/*
		 * HashMap<String, Object> data = new HashMap<String, Object>();
		 * data.put("danger.boire", false); data.put("danger.vomit", "lol");
		 * data.put("danger.convulsions_passe", false);
		 * data.put("danger.lethargie", false);
		 * data.put("danger.convulsions_present", false);
		 */

		/*
		 * We load the data about the child... We need the following values : -
		 * enfant.months; enfant.muac; enfant.wfh; enfant.wfa; enfant.weight
		 */
		HashMap<String, Object> child = new HashMap<String, Object>();
		Database db = new Database(this);
		Cursor cursor = db.getChildrenDiagnostics(patient_id);
		if (cursor.getCount() != 0) {
			// There is data about this child
			// We get it
			Date birthdate;
			try {
				birthdate = new SimpleDateFormat("yyyy-MM-dd").parse(cursor
						.getString(cursor.getColumnIndex("born_on")));
			} catch (Exception ex) {
				ex.printStackTrace();
				birthdate = new Date();
			}
			int muac = cursor.getInt(cursor.getColumnIndex("mac"));
			float height = cursor.getFloat(cursor.getColumnIndex("height"));
			float weight = cursor.getFloat(cursor.getColumnIndex("height"));

			// We calculate the various things we need
			int months = getMonthsDifference(birthdate, new Date());
			float wfh = weight / height; // weight/height
			float wfa = weight / months; // weight/age (in months???)

			// and put it in the HashMap
			child.put("months", months);
			child.put("muac", muac);
			child.put("wfh", wfh);
			child.put("wfa", wfa);
			child.put("weight", weight);
		} else {
			// No data about this child, we tell the user
			Toast.makeText(this, R.string.error_no_diagnostic_data,
					Toast.LENGTH_LONG).show();
		}

		// Convert HashMap to JS values...
		String jsDataVar = hashMapToJavascript(data, "data");
		String jsChildVar = hashMapToJavascript(child, "enfant");

		// Will contain the results to display
		ArrayList<String> results = new ArrayList<String>();

		// Get all the equations...
		Cursor classificationsCursor = db.getClassifications();
		do {
			String eq = jsDataVar + " " + jsChildVar + " " + baseJS + " "
					+ classificationsCursor.getString(2);// " !(data['danger.boire'] || data['danger.vomit'] || data['danger.convulsions_passe'] || data['danger.lethargie'] || data['danger.convulsions_present'])";
			System.out.println(eq);
			Context context = Context.enter();
			context.setOptimizationLevel(-1); // Disable compilation

			try {
				Scriptable scope = context.initStandardObjects();

				// scope.put("data", scope, data);

				Boolean result = (Boolean) context.evaluateString(scope, eq,
						"doit", 1, null);
				System.out.println("Result: " + result);
				if (result) {
					results.add(classificationsCursor.getString(1));
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
		} while (classificationsCursor.moveToNext());

		// We now put the results inside a ListView
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, results);
		((ListView) findViewById(R.id.listView_classifications))
				.setAdapter(arrayAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_signs_classification, menu);
		return true;
	}

	/**
	 * Converts a HashMap to its Javascript equivalent
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
	 * Gets the difference in months between two dates
	 * 
	 * @param date1
	 *            the first date
	 * @param date2
	 *            the second date
	 * @return the difference in months
	 */
	public static final int getMonthsDifference(Date date1, Date date2) {
		int m1 = date1.getYear() * 12 + date1.getMonth();
		int m2 = date2.getYear() * 12 + date2.getMonth();
		return m2 - m1 + 1;
	}

}
