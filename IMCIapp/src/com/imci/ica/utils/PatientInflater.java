package com.imci.ica.utils;

import android.app.Activity;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.imci.ica.R;

/**
 * Class to inflate the module of patient information
 * 
 * @author Miguel
 * 
 */
public class PatientInflater {

	Activity mActivity;

	/**
	 * Constructor receive the reference of current activity
	 * 
	 * @param activity
	 */
	public PatientInflater(Activity activity) {
		this.mActivity = activity;
	}

	/**
	 * Show in screen the layout with patient data
	 * 
	 * @param id
	 *            his id
	 * @param group
	 *            view that contains layout
	 * @param patient
	 *            cursor with patient information
	 * @param db
	 *            reference to opened database
	 */
	public void showPatientInScreen(int id, ViewGroup group, Cursor patient,
			Database db) {
		View view = mActivity.getLayoutInflater().inflate(
				R.layout.layout_info_patient, group, false);

		group.addView(view);

		String name = new String(patient.getString(patient
				.getColumnIndex("first_name")) + " "
				+ patient.getString(patient.getColumnIndex("last_name")));
		TextView textName = (TextView) mActivity.findViewById(R.id.textName);
		textName.setText(name);

		TextView textGenderBirth = (TextView) mActivity
				.findViewById(R.id.textGenderBirth);
		ImageView image = (ImageView) mActivity.findViewById(R.id.imageView1);
		Resources res = mActivity.getResources();
		Drawable drawable;

		String genderValue = patient
				.getString(patient.getColumnIndex("gender")).toString();
		if (genderValue.equals("t")) {
			String genderBirth = new String(mActivity.getString(R.string.male)
					+ " - "
					+ patient.getString(patient.getColumnIndex("born_on")));
			textGenderBirth.setText(genderBirth);
			drawable = res.getDrawable(R.drawable.male_icon);
			image.setImageDrawable(drawable);
		} else {
			String genderBirth = new String(
					mActivity.getString(R.string.female)
							+ " - "
							+ patient.getString(patient
									.getColumnIndex("born_on")));
			textGenderBirth.setText(genderBirth);
			drawable = res.getDrawable(R.drawable.female_icon);
			image.setImageDrawable(drawable);
		}

		TextView village = (TextView) mActivity.findViewById(R.id.textVillage);
		String villageName = db.getNameOfZone(patient.getInt(patient
				.getColumnIndex("village_id")));
		String zoneName = db.getNameOfZone(patient.getInt(patient
				.getColumnIndex("zone_id")));
		village.setText(villageName + " - " + zoneName);
	}
}
