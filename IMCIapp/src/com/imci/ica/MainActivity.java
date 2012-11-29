package com.imci.ica;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
    	//Launch init activity
    	//Miguel : comment this if necessary
    	startActivity(new Intent(this, LoginActivity.class));
    	finish();
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
   	
    //Answer to Create button click
    public void createPatient(View view) {
    	Intent intent = new Intent(this, RegisterPatientActivity.class);
    	startActivity(intent);
    }

}
