package com.imci.registerpatients;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Register extends Activity {

	
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
	}
	   	
	/** Called when the user clicks the Send button */
	public void pushCreate(View view) {
		// Do something in response to button
		Intent intent = new Intent(this, NewPatient.class);
		startActivity(intent);
	}
		
}