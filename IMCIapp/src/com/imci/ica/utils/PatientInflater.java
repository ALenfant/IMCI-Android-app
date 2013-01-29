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

	public final static int POS_FIRST_NAME = 2;
	public final static int POS_LAST_NAME = 3;
	public final static int POS_GENDER = 5;
	public final static int POS_BIRTH = 4;
	public final static int POS_VILLAGE = 1;

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
	 * 			his id
	 * @param group
	 * 			view that contains layout
	 * @param patient
	 * 			cursor with patient information
	 * @param db
	 * 			reference to opened database
	 */
	public void showPatientInScreen(int id, ViewGroup group, Cursor patient,
			Database db) {
		View view = mActivity.getLayoutInflater().inflate(
				R.layout.layout_info_patient, group, false);

		group.addView(view);

		String name = new String(patient.getString(POS_FIRST_NAME) + " "
				+ patient.getString(POS_LAST_NAME));
		TextView textName = (TextView) mActivity.findViewById(R.id.textName);
		textName.setText(name);

		TextView textGenderBirth = (TextView) mActivity
				.findViewById(R.id.textGenderBirth);
		ImageView image = (ImageView) mActivity.findViewById(R.id.imageView1);
		Resources res = mActivity.getResources();
		Drawable drawable;

		String genderValue = patient.getString(POS_GENDER).toString();
		if (genderValue.equals("t")) {
			String genderBirth = new String(mActivity.getString(R.string.male)
					+ " - " + patient.getString(POS_BIRTH));
			textGenderBirth.setText(genderBirth);
			drawable = res.getDrawable(R.drawable.male_icon);
			image.setImageDrawable(drawable);
		} else {
			String genderBirth = new String(
					mActivity.getString(R.string.female) + " - "
							+ patient.getString(POS_BIRTH));
			textGenderBirth.setText(genderBirth);
			drawable = res.getDrawable(R.drawable.female_icon);
			image.setImageDrawable(drawable);
		}

		TextView village = (TextView) mActivity.findViewById(R.id.textVillage);
		village.setText(db.getNameOfZone(patient.getInt(POS_VILLAGE)));
	}
}
