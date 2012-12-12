package com.imci.ica;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class InfoPatientActivity extends Activity {
	
	public final static String EXTRA_ID_PATIENT = "com.imci.ica.ID_PATIENT";

	public final static int POS_FIRST_NAME = 2;
	public final static int POS_LAST_NAME = 3;
	public final static int POS_GENDER = 5;
	public final static int POS_BIRTH = 4;
	public final static int POS_VILLAGE = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info_patient);
		
		Intent intent = getIntent();
		int id = intent.getIntExtra(EXTRA_ID_PATIENT, 0);
		
		Database db = new Database(this);
		Cursor patient = db.getPatientById(id);
		
		LinearLayout mainLayout = (LinearLayout)findViewById(R.id.main_layout);		
		View view = getLayoutInflater().inflate(R.layout.layout_info_patient, mainLayout, false);
		
		mainLayout.addView(view);
		
		TextView first = (TextView) findViewById(R.id.textFirst);
		first.setText(patient.getString(POS_FIRST_NAME));

		TextView last = (TextView) findViewById(R.id.textLast);
		last.setText(patient.getString(POS_LAST_NAME));
		
		TextView gender = (TextView) findViewById(R.id.textGender);
		ImageView image = (ImageView) findViewById(R.id.imageView1);
		Resources res = getResources();
		Drawable drawable;
		
		String genderValue = patient.getString(POS_GENDER).toString();
		if(genderValue.equals("t")) {
			gender.setText(R.string.male);
	        drawable = res.getDrawable(R.drawable.male_icon);
	        image.setImageDrawable(drawable);
		} else {
			gender.setText(R.string.female);
	        drawable = res.getDrawable(R.drawable.female_icon);
	        image.setImageDrawable(drawable);
		}
		
		TextView birth = (TextView) findViewById(R.id.textBirth);
		birth.setText(patient.getString(POS_BIRTH));
		
		TextView village = (TextView) findViewById(R.id.textVillage);
		village.setText(db.getNameOfZone(patient.getInt(POS_VILLAGE)));			
		}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_info_patient, menu);
		return true;
	}

	
	public void new_diagnostic (View view) {
		Intent intent = new Intent(this, GetSignsActivity.class);
		startActivityForResult(intent, 0);
	}
}
