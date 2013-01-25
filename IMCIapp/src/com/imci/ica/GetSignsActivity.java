package com.imci.ica;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

/**
 * Class to take data to do a diagnostic
 * Questions are shown in screen by illness group of questions. 
 * 
 * @author Miguel
 *
 */
public class GetSignsActivity extends Activity {

	public final static String EXTRA_ID_PATIENT = "com.imci.ica.ID_PATIENT";
	public final static String EXTRA_AGE_GROUP = "com.imci.ica.AGE_GROUP";
	public final static String EXTRA_HASHMAP = "com.imci.ica.HASHMAP";

	private final int TYPE_BOOLEAN = 1;
	private final int TYPE_INTEGER = 2;

	int id_patient, age_group;
	String born_on;
	int illness_id; // , count;
	int index;
	boolean end = false;

	Cursor illnesses, questions;
	CursorQuestionsAdapter mAdapter;
	Cursor mCursor;
	TableLayout tableQuestions;

HashMap<String, String> types;		// Map containing types of answer of questions
	HashMap<String, String> answersAllQuestions;  // Map for all questions answered correctly
	HashMap<String, String> answersOneIllness;    // Aux Map to answer of one illness

	/**
	 * Show interface and initialize all variables
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_signs);
		
		ScrollView view = (ScrollView) findViewById(R.id.scrollViewQuestions);
		view.setVisibility(View.GONE);

		Intent mIntent = getIntent();
		id_patient = mIntent.getIntExtra(EXTRA_ID_PATIENT, 0);
		age_group = mIntent.getIntExtra(EXTRA_AGE_GROUP, 0);

		Database db = new Database(this);
		illnesses = db.getIllnesses();
		questions = db.getQuestions(age_group);
		startManagingCursor(illnesses);
		startManagingCursor(questions);

		mAdapter = new CursorQuestionsAdapter(this, questions);

		index = 0;

		types = new HashMap<String, String>();
		answersAllQuestions = new HashMap<String, String>();
		answersOneIllness = new HashMap<String, String>();

		tableQuestions = null;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_get_signs, menu);
		return true;
	}

	/**
	 * Show new illnesses group of questions
	 */
	public void showNextIllness() {
		String key;

		LinearLayout mLayout = (LinearLayout) findViewById(R.id.layoutQuestions);

		mCursor = mAdapter.getCursor();

		illness_id = Integer.parseInt(mAdapter.getIllnessId(mCursor));

		Database db = new Database(this);
		String illness_key = db.getIllnessKey(illness_id);

		tableQuestions = new TableLayout(GetSignsActivity.this);
		mLayout.addView(tableQuestions);

		while (illness_id == Integer.parseInt(mAdapter.getIllnessId(mCursor))
				&& !end) {
			View view = mAdapter.getView(index, null, tableQuestions);

			key = mAdapter.createKey(illness_key);
			view.setTag(key);

			types.put(key, mAdapter.getType(mCursor));

			tableQuestions.addView(view);
			if (!mAdapter.isLast()) {
				mAdapter.moveToNext();
				mCursor = mAdapter.getCursor();
				index++;
			} else {
				end = true;
			}
		}

	}

	/**
	 * Answer to Next button.
	 * Save answers in current screen if all them are answered
	 * 
	 * @param view
	 */
	public void saveAnswers(View view) {

		View mView;
		String key;
		String type;

		// If is the first entry, hide measures layout
		if (index == 0) {
			if(!takeMeasures()) {
				answersOneIllness.clear();
				return;				
			}
			ScrollView scrollMeasures = (ScrollView) findViewById(R.id.scrollViewMeasures);
			scrollMeasures.setVisibility(View.GONE);

			ScrollView scrollQuestions = (ScrollView) findViewById(R.id.scrollViewQuestions);
			scrollQuestions.setVisibility(View.VISIBLE);
			
		} else {
			Iterator<Entry<String, String>> iterator = types.entrySet()
					.iterator();

			while (iterator.hasNext()) {
				Entry<String, String> entry = iterator.next();
				key = entry.getKey();
				type = entry.getValue();

				mView = (View) tableQuestions.findViewWithTag(key);

				switch (mAdapter.getTypeQuestion(type)) {
				case TYPE_BOOLEAN: {
					if (!takeAnswersBoolean(mView, key)) {
						answersOneIllness.clear();
						return;
					}
				}
					break;
				case TYPE_INTEGER: {
					if (!takeAnswersInteger(mView, key)) {
						answersOneIllness.clear();
						return;
					}
				}
					break;
				default:
					takeAnswersList(mView, key);
					break;
				}

			}

			tableQuestions.setVisibility(View.GONE);
		}

		registerAnswersOneIllness(answersOneIllness);

		types.clear();
		answersOneIllness.clear();

		// If it isn't the end, show next questions
		if (!end) {
			showNextIllness();
		} else {
			// Only for check result of data entry (debug purposes)
			Iterator<String> itKey = answersAllQuestions.keySet().iterator();
			String value;
			while (itKey.hasNext()) {
				key = itKey.next();
				value = answersAllQuestions.get(key);

				Log.w(key, value);
			}

			// Go to next activity
			Intent intent = new Intent(this, SignsClassificationActivity.class);
			intent.putExtra(SignsClassificationActivity.EXTRA_HASHMAP_DATA,
			answersAllQuestions); //The answers
			intent.putExtra(SignsClassificationActivity.EXTRA_ID_PATIENT,
					id_patient); // The patient's id
			startActivity(intent);
			finish();

		}
	}

	/**
	 * Get answers from a question of boolean type
	 * 
	 * @param view
	 * 			the layout of the question
	 * @param key
	 * 			the key of the question
	 * @return true if lecture was successful
	 */
	public boolean takeAnswersBoolean(View view, String key) {
		RadioButton yesButton = (RadioButton) view.findViewById(R.id.buttonYes);
		RadioButton noButton = (RadioButton) view.findViewById(R.id.buttonNo);

		if (!yesButton.isChecked() && !noButton.isChecked()) {
			Toast.makeText(this, R.string.allQuestionsMarked, Toast.LENGTH_LONG)
					.show();
			return false;
		} else {
			if (yesButton.isChecked()) {
				answersOneIllness.put(key, "true");
			} else if (noButton.isChecked()) {
				answersOneIllness.put(key, "false");
			}
			return true;
		}
	}

	/**
	 * Get answers from a question of integer type
	 * 
	 * @param view
	 * 			the layout of the question
	 * @param key
	 * 			the key of the question
	 * @return true if lecture was successful
	 */
	public boolean takeAnswersInteger(View view, String key) {
		EditText entry = (EditText) view.findViewById(R.id.editValue);
		String number = entry.getText().toString();

		if (number.length() == 0) {
			Toast.makeText(this, R.string.allQuestionsMarked, Toast.LENGTH_LONG)
					.show();
			return false;
		} else {
			answersOneIllness.put(key, number);
			return true;
		}
	}

	/**
	 * Get answers from a question of list type
	 * 
	 * @param view
	 * 			the layout of the question
	 * @param key
	 * 			the key of the question
	 * @return true if lecture was successful
	 */
	public boolean takeAnswersList(View view, String key) {
		Spinner list = (Spinner) view.findViewById(R.id.spinner);
		String election = list.getSelectedItem().toString();

		answersOneIllness.put(key, election);
		return true;
	}

	/**
	 * Register answers of a illness group of questions, in final result
	 * hashmap
	 * 
	 * @param hm
	 */
	public void registerAnswersOneIllness(HashMap<String, String> hm) {
		String key;
		String value;
		Iterator<Entry<String, String>> iterator = hm.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, String> e = iterator.next();
			key = e.getKey();
			value = e.getValue();

			answersAllQuestions.put(key, value);
		}
	}

	/**
	 * Get the measures in the first group of questions
	 * 
	 * @return true if process was successful
	 */
	@SuppressLint("SimpleDateFormat")
	public boolean takeMeasures() {
		EditText editHeight = (EditText) findViewById(R.id.editHeight);
		String height = editHeight.getText().toString();
		EditText editWeight = (EditText) findViewById(R.id.editWeight);
		String weight = editWeight.getText().toString();
		EditText editTemp = (EditText) findViewById(R.id.editTemp);
		String temp = editTemp.getText().toString();
		EditText editMuac = (EditText) findViewById(R.id.editMuac);
		String muac = editMuac.getText().toString();

		if (height.length() == 0 || weight.length() == 0 || temp.length() == 0
				|| muac.length() == 0) {
			Toast.makeText(this, R.string.allQuestionsMarked, Toast.LENGTH_LONG)
					.show();
			return false;
		} else {

			Database db = new Database(this);
			Cursor patient = db.getPatientById(id_patient);
			Date birthdate;
			try {
				birthdate = new SimpleDateFormat("yyyy-MM-dd").parse(patient
						.getString(patient.getColumnIndex("born_on")));
			} catch (Exception ex) {
				ex.printStackTrace();
				birthdate = new Date();
			}

			Integer months = DateUtils.getMonthsDifference(birthdate,
					new Date());

			Float wfh = Float.parseFloat(weight) / Float.parseFloat(height);
			Float wfa = Float.parseFloat(weight) / months;

			answersOneIllness.put("enfant.months", months.toString());
			answersOneIllness.put("enfant.height", height);
			answersOneIllness.put("enfant.weight", weight);
			answersOneIllness.put("enfant.temperature", temp);
			answersOneIllness.put("enfant.muac", muac);
			answersOneIllness.put("enfant.wfh", wfh.toString());
			answersOneIllness.put("enfant.wfa", wfa.toString());
			return true;
		}

	}
}
