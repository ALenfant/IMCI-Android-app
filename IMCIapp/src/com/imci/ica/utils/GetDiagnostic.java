package com.imci.ica.utils;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.AsyncTask;
import android.widget.Toast;

import com.imci.ica.MyActivity;
import com.imci.ica.R;

/**
 * Class to get diagnostic with answers got, in background
 * 
 * @author Miguel
 *
 */
public class GetDiagnostic extends AsyncTask<Void, Void, Void> {

	private MyActivity mActivity;
	private ProgressDialog pDialog;
	private HashMap<String, Object> data;
	private int patient_id;
	private String diagnostic_global_id;
	private ArrayList<Integer> results;

	public final static String baseJS = "function AT_LEAST_TWO_OF(){var trueargs = 0;for (var i = 0; i < arguments.length; ++i) {if (arguments[i]) {trueargs++;}} return (trueargs >= 2);}";

	/**
	 * Constructor. It sets the Activity and the JSONObject already created
	 * 
	 * @param activity
	 * @param json
	 */
	public GetDiagnostic(MyActivity myActivity, int patient_id,
			HashMap<String, Object> data) {
		super();
		mActivity = myActivity;
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
		// Get all the equations...
		Cursor classificationsCursor = db.getClassifications(age_group);

		// Will contain the results to display
		results = JSUtils.evaluation(age_group, jsDataVar, classificationsCursor);

		// Save diagnostic in database
		String child_global_id = patient.getString(patient
				.getColumnIndex("global_id"));
		int zone_id = patient.getInt(patient.getColumnIndex("zone_id"));
		String born_on = patient.getString(patient.getColumnIndex("born_on"));

		int muac = (Integer) data.get("enfant.muac");
		float weight = (Float) data.get("enfant.weight");
		float height = (Float) data.get("enfant.height");
		float temp = (Float) data.get("enfant.temperature");

		diagnostic_global_id = db.savePatientDiagnostic(child_global_id, muac,
				height, weight, temp, age_group, zone_id, born_on);

		// If diagnostic was saved correctly, save result
		if (!diagnostic_global_id.equals("-1")) {
			ArrayList<String> resultsText = new ArrayList<String>();
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
			mActivity.setResutls(results);
		}

	}
}
