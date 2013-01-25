package com.imci.ica;

import java.util.ArrayList;
import java.util.HashMap;

import org.mozilla.javascript.Context;
import org.mozilla.javascript.Scriptable;

import android.app.Activity;
import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Class to get diagnostic with answers got, in background
 * 
 * @author Miguel
 *
 */
public class GetDiagnostic extends AsyncTask<Void, Void, Void> {

	private Activity mActivity;
	private ProgressDialog pDialog;
	private HashMap<String, Object> data;
	private int patient_id;
	private String diagnostic_global_id;
	private ArrayList<String> resultsText;

	public final static String baseJS = "function AT_LEAST_TWO_OF(){var trueargs = 0;for (var i = 0; i < arguments.length; ++i) {if (arguments[i]) {trueargs++;}} return (trueargs >= 2);}";

	/**
	 * Constructor. It sets the Activity and the JSONObject already created
	 * 
	 * @param activity
	 * @param json
	 */
	public GetDiagnostic(Activity activity, int patient_id,
			HashMap<String, Object> data) {
		super();
		mActivity = activity;
		this.data = data;
		this.patient_id = patient_id;
	}

	/**
	 * Done while executing. We received the answer from the servlet as a
	 * jsonString
	 */
	@Override
	protected Void doInBackground(Void... arg0) {

		// Convert HashMap to JS values...
		String jsDataVar = JSUtils.hashMapToJavascript(data, "data");

		// We get the child data
		int age_group;

		Database db = new Database(mActivity);
		Cursor patient = db.getPatientById(patient_id);
		if (patient.getCount() > 0) {
			String birth_date = patient.getString(patient
					.getColumnIndex("born_on"));
			age_group = DateUtils.getAgeGroup(birth_date);
		} else {
			age_group = 2; // For debugging purposes
		}

		// Will contain the results to display
		ArrayList<Integer> results = new ArrayList<Integer>();

		// Get all the equations...
		Cursor classificationsCursor = db.getClassifications(age_group);
		do {
			String eq = jsDataVar + " " + baseJS + " "
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
					results.add(classificationsCursor
							.getInt(classificationsCursor.getColumnIndex("_id")));
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

		String child_global_id = patient.getString(patient
				.getColumnIndex("global_id"));
		int zone_id = patient.getInt(patient.getColumnIndex("zone_id"));
		String born_on = patient.getString(patient.getColumnIndex("born_on"));

		String muac = (String) data.get("enfant.muac");
		String weight = (String) data.get("enfant.weight");
		String height = (String) data.get("enfant.height");
		String temp = (String) data.get("enfant.temperature");

		diagnostic_global_id = db.savePatientDiagnostic(child_global_id, muac,
				height, weight, temp, age_group, zone_id, born_on);

		if (!diagnostic_global_id.equals("-1")) {
			resultsText = new ArrayList<String>();
			int classification_id;

			for (int i = 0; i < results.size(); i++) {
				classification_id = results.get(i);
				resultsText.add(db.getClassificationText(classification_id));
				db.saveDiagnosticResults(classification_id,
						diagnostic_global_id, zone_id);
			}
		}
		db.close();

		return null;
	}

	/**
	 * What should be done before executing
	 * 
	 * @see ProgressDialog It shows the "connecting" when android is thinking.
	 */
	@Override
	protected void onPreExecute() {
		pDialog = new ProgressDialog(mActivity);
		pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		pDialog.setMessage("Wait few seconds...");
		pDialog.show();
	}

	/**
	 * What should be done after executing Analyzing the jsonString received.
	 */
	@Override
	protected void onPostExecute(Void param) {
		pDialog.dismiss();
		if (diagnostic_global_id.equals("-1")) {
			Toast.makeText(mActivity, R.string.databaseError, Toast.LENGTH_LONG)
					.show();
			mActivity.finish();
		} else {

			// We now put the results inside a ListView
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					mActivity, android.R.layout.simple_list_item_1, resultsText);
			((ListView) mActivity.findViewById(R.id.listView_classifications))
					.setAdapter(arrayAdapter);

		}

	}
}
