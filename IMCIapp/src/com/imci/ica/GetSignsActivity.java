package com.imci.ica;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

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
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.Toast;

public class GetSignsActivity extends Activity {

	public final static String EXTRA_ID_PATIENT = "com.imci.ica.ID_PATIENT";
	public final static String EXTRA_AGE_GROUP = "com.imci.ica.AGE_GROUP";
	public final static String EXTRA_HASHMAP = "com.imci.ica.HASHMAP";

	private final int TYPE_BOOLEAN = 1;
	private final int TYPE_INTEGER = 2;

	int id_patient, age_group;
	int illness_id, count;
	int index;
	boolean end = false;

	Cursor illnesses, questions;
	CursorQuestionsAdapter mAdapter;
	TableLayout tableQuestions;

	HashMap<String, String> types;
	// HashMap<String, View> views;
	HashMap<String, String> answersAllQuestions;
	HashMap<String, String> answersOneIllness;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_get_signs);

		Intent mIntent = getIntent();
		id_patient = mIntent.getIntExtra(EXTRA_ID_PATIENT, 0);
		age_group = mIntent.getIntExtra(EXTRA_AGE_GROUP, 0);

		Database db = new Database(this);
		illnesses = db.getIllnesses();
		questions = db.getQuestions(age_group);
		startManagingCursor(illnesses);
		startManagingCursor(questions);

		mAdapter = new CursorQuestionsAdapter(this, questions);
		count = mAdapter.getCount();

		index = 0;

		types = new HashMap<String, String>();
		// views = new HashMap<String, View>();
		answersAllQuestions = new HashMap<String, String>();
		answersOneIllness = new HashMap<String, String>();

		tableQuestions = null;

		showNextIllness();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_get_signs, menu);
		return true;
	}

	public void showNextIllness() {
		String key;

		LinearLayout mLayout = (LinearLayout) findViewById(R.id.layoutQuestions);

		illness_id = Integer.parseInt(mAdapter.getIllnessId(mAdapter
				.getCursor()));

		Database db = new Database(this);
		String illness_key = db.getIllnessKey(illness_id);

		tableQuestions = new TableLayout(GetSignsActivity.this);
		mLayout.addView(tableQuestions);

		while (illness_id == Integer.parseInt(mAdapter.getIllnessId(mAdapter
				.getCursor())) && !end) {
			View view = mAdapter.getView(index, null, tableQuestions);

			key = mAdapter.createKey(illness_key);
			view.setTag(key);

			types.put(key, mAdapter.getType(mAdapter.getCursor()));

			tableQuestions.addView(view);
			if (!mAdapter.isLast()) {
				mAdapter.moveToNext();
				index++;
			} else {
				end = true;
			}
		}

	}

	public void saveAnswers(View view) {

		View mView;
		String key;
		String type;

		Iterator<Entry<String, String>> iterator = types.entrySet().iterator();

		while (iterator.hasNext()) {
			Entry<String, String> e = iterator.next();
			key = e.getKey();
			type = e.getValue();

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
		registerAnswersOneIllness(answersOneIllness);

		types.clear();
		answersOneIllness.clear();
		// Hiding previous illnesses questions
		tableQuestions.setVisibility(View.GONE);

		if (!end) {
			showNextIllness();
		} else {
			// Only for check result of data entry
			Iterator<String> itKey = answersAllQuestions.keySet().iterator();
			String value;
			while (itKey.hasNext()) {
				key = itKey.next();
				value = answersAllQuestions.get(key);

				Log.w(key, value);
			}

			// ANTONIN, YOU HAVE HERE THE CODE TO PASS TO THE NEW ACTIVITY
			// JUST CHANGE THE NAME "NameNewActivity" FOR THE NAME OF THIS
			//
			// Intent intent = new Intent(this, NameNewActivity.class);
			// intent.putExtra(EXTRA_HASHMAP, answers);
			// startActivity(intent);
			// finish();

		}
	}

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

	public boolean takeAnswersList(View view, String key) {
		Spinner list = (Spinner) view.findViewById(R.id.spinner);
		String election = list.getSelectedItem().toString();

		answersOneIllness.put(key, election);
		return true;
	}

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
}
