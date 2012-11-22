package com.imci.registerpatients;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class NewPatient extends Activity {

	public final static String EXTRA_NAME = "com.imci.register.NAME";
	public final static String EXTRA_FAMILY = "com.imci.register.FAMILY";
	public final static String EXTRA_IDCARD = "com.imci.register.IDCARD";
	public final static String EXTRA_BIRTH = "com.imci.register.BIRTH";
	public final static String EXTRA_ADDRESS = "com.imci.register.ADDRESS";
	public final static String EXTRA_TOWN = "com.imci.register.TOWN";
	public final static String EXTRA_POSTCODE = "com.imci.register.POSTCODE";

	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_patient);
    }
    
    /** Called when the user clicks the Send button */
    public void sendInfo(View view) {

    	Intent intent = new Intent(this, DisplayInfo.class);
    	
    	EditText editName = (EditText) findViewById(R.id.edit_name);
    	if (editName.getText().length() == 0) {
            Toast.makeText(this, R.string.invalidName,
                Toast.LENGTH_LONG).show();
            return;
    	} else {    	
    		String name = editName.getText().toString();
    		intent.putExtra(EXTRA_NAME, name);
    	}
    	
    	EditText editFamily = (EditText) findViewById(R.id.edit_family);
    	if (editFamily.getText().length() == 0) {
            Toast.makeText(this, R.string.invalidFamily,
                Toast.LENGTH_LONG).show();
            return;
    	} else {    	
    		String family_name = editFamily.getText().toString();
        	intent.putExtra(EXTRA_FAMILY, family_name);
    	}
    	
    	EditText editID = (EditText) findViewById(R.id.edit_idcard);
    	if (editName.getText().length() == 0) {
            Toast.makeText(this, R.string.invalidName,
                Toast.LENGTH_LONG).show();
            return;
    	} else {    	
        	String idcard = editID.getText().toString();
        	intent.putExtra(EXTRA_IDCARD, idcard);
    	}
    	
    	EditText editBirth = (EditText) findViewById(R.id.edit_birth);
    	if (editName.getText().length() == 0) {
            Toast.makeText(this, R.string.invalidName,
                Toast.LENGTH_LONG).show();
            return;
    	} else {    	
        	String birth = editBirth.getText().toString();
        	intent.putExtra(EXTRA_BIRTH, birth);
    	}
    	
    	EditText editAddress = (EditText) findViewById(R.id.edit_address);
    	if (editName.getText().length() == 0) {
            Toast.makeText(this, R.string.invalidName,
                Toast.LENGTH_LONG).show();
            return;
    	} else {    	
        	String address = editAddress.getText().toString();
        	intent.putExtra(EXTRA_ADDRESS, address);
    	}
    	
    	EditText editTown = (EditText) findViewById(R.id.edit_town);
    	if (editName.getText().length() == 0) {
            Toast.makeText(this, R.string.invalidName,
                Toast.LENGTH_LONG).show();
            return;
    	} else {    	
        	String town = editTown.getText().toString();
        	intent.putExtra(EXTRA_TOWN, town);
    	}
    	
    	EditText editPostcode = (EditText) findViewById(R.id.edit_postcode);
    	if (editName.getText().length() == 0) {
            Toast.makeText(this, R.string.invalidName,
                Toast.LENGTH_LONG).show();
            return;
    	} else {    	
        	String postcode = editPostcode.getText().toString();
        	intent.putExtra(EXTRA_POSTCODE, postcode);
    	}
    	    	
        startActivity(intent);
    }
    
    public void cancel(View view) {
    	finish();
    }    
}