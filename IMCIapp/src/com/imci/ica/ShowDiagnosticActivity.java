package com.imci.ica;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.imci.ica.utils.ClassificationAdapter;
import com.imci.ica.utils.Database;

public class ShowDiagnosticActivity extends Activity {

	public static String EXTRA_GLOBAL_ID = "com.imci.ica.GLOBAL_ID";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_show_diagnostic);

		// Get global_id of diagnostic
		Intent intent = getIntent();
		String global_id = intent.getStringExtra(EXTRA_GLOBAL_ID);

		// Get results associated a this diagnostic
		Database db = new Database(this);
		Cursor diagnostic = db.getResultsByDiagId(global_id);

		// Set the date in up part of display
		String date = diagnostic.getString(diagnostic
				.getColumnIndex("created_at"));
		TextView textDate = (TextView) findViewById(R.id.textDate);
		textDate.setText(date);

		ArrayList<String> results = new ArrayList<String>();
		// Get classifications associated to this diagnostic
		while (!diagnostic.isLast()) {
			results.add(((Integer) diagnostic.getInt(diagnostic
					.getColumnIndex("classification_id"))).toString());
			diagnostic.moveToNext();
		}

		// We now put the results inside a ListView
		ClassificationAdapter arrayAdapter = new ClassificationAdapter(this,
				R.layout.layout_classifications, results);
		((ListView) findViewById(R.id.listDiagnostic)).setAdapter(arrayAdapter);

	}
	
	/**
	 * Finish this Activity and return to the previous
	 * 
	 * @param view
	 */
	public void goBack(View view) {
		finish();
	}

}
