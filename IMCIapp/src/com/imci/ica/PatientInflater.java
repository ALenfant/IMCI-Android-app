package com.imci.ica;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class PatientInflater {
	

	public final static int POS_FIRST_NAME = 2;
	public final static int POS_LAST_NAME = 3;
	public final static int POS_GENDER = 5;
	public final static int POS_BIRTH = 4;
	public final static int POS_VILLAGE = 1;
	
	Activity activity;
	
	public PatientInflater(Activity activity) {
		this.activity = activity;
	}
	
	public void showPatientInScreen(int id, ViewGroup group, Cursor patient, Database db){
		View view = activity.getLayoutInflater().inflate(R.layout.layout_info_patient, group, false);
		
		group.addView(view);
		
		TextView first = (TextView) activity.findViewById(R.id.textFirst);
		first.setText(patient.getString(POS_FIRST_NAME));

		TextView last = (TextView) activity.findViewById(R.id.textLast);
		last.setText(patient.getString(POS_LAST_NAME));
		
		TextView gender = (TextView) activity.findViewById(R.id.textGender);
		ImageView image = (ImageView) activity.findViewById(R.id.imageView1);
		Resources res = activity.getResources();
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
		
		TextView birth = (TextView) activity.findViewById(R.id.textBirth);
		birth.setText(patient.getString(POS_BIRTH));
		
		TextView village = (TextView) activity.findViewById(R.id.textVillage);
		village.setText(db.getNameOfZone(patient.getInt(POS_VILLAGE)));
	}
}
