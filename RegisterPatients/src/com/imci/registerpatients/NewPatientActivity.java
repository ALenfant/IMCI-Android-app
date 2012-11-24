package com.imci.registerpatients;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Toast;

public class NewPatientActivity extends Activity {

	public final static String EXTRA_FIRST_NAME = "com.imci.register.FIRST_NAME";
	public final static String EXTRA_LAST_NAME = "com.imci.register.FAMILY_NAME";
	public final static String EXTRA_GENDER = "com.imci.register.GENDER";
	public final static String EXTRA_DAY = "com.imci.register.DAY";
	public final static String EXTRA_MONTH = "com.imci.register.MONTH";
	public final static String EXTRA_YEAR = "com.imci.register.YEAR";
	public final static String EXTRA_VILLAGE = "com.imci.register.VILLAGE";
	
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patient);
    }
    
    //Answer to Send button click
    public void sendInfo(View view) {

    	Intent intent = new Intent(this, DisplayInfoActivity.class);
    	
    	//Passing data to next activity
    	EditText editFirstName = (EditText) findViewById(R.id.edit_first_name);
    	if (editFirstName.getText().length() == 0) {
            Toast.makeText(this, R.string.invalidFirstName,
                Toast.LENGTH_LONG).show();
            return;
    	} else {    	
    		String name = editFirstName.getText().toString();
    		intent.putExtra(EXTRA_FIRST_NAME, name);
    	}
    	
    	EditText editLastName = (EditText) findViewById(R.id.edit_last_name);
    	if (editLastName.getText().length() == 0) {
            Toast.makeText(this, R.string.invalidFamilyName,
                Toast.LENGTH_LONG).show();
            return;
    	} else {    	
    		String last_name = editLastName.getText().toString();
        	intent.putExtra(EXTRA_LAST_NAME, last_name);
    	}
    	
    	//Check value of Gender Radio Group
    	RadioButton maleButton = (RadioButton) findViewById(R.id.radio0);
        RadioButton femaleButton = (RadioButton) findViewById(R.id.radio1);
        
        if (maleButton.isChecked()) {
        	intent.putExtra(EXTRA_GENDER, true);
        } else if (femaleButton.isChecked()) {
        	intent.putExtra(EXTRA_GENDER, false);
        }

        //Getting date of birth
    	DatePicker birth = (DatePicker) findViewById(R.id.datePicker1);
    	intent.putExtra(EXTRA_DAY, birth.getDayOfMonth());
    	intent.putExtra(EXTRA_MONTH, birth.getMonth());
    	intent.putExtra(EXTRA_YEAR, birth.getYear());

    	EditText editVillage = (EditText) findViewById(R.id.edit_village);
    	if (editVillage.getText().length() == 0) {
            Toast.makeText(this, R.string.invalidVillage,
                Toast.LENGTH_LONG).show();
            return;
    	} else {
    		//Checking field Village is a number
        	try {
        		Integer villageID = 
        			Integer.parseInt(editVillage.getText().toString());
        		intent.putExtra(EXTRA_VILLAGE, villageID);
        	} catch (NumberFormatException e) {
        		Toast.makeText(this, R.string.invalidVillage,
                        Toast.LENGTH_LONG).show();
                    return;
        	}
    	}
    	
    	//Starting new activity and hoping a result for finish by itself
        startActivityForResult(intent, 0);
    }
    
    //Answer to Cancel button
    public void cancel(View view) {
    	finish();
    }
    

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if (data != null) {
        	finish();
        }
    }
}