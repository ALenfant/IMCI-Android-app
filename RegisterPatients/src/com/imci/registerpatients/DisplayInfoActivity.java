package com.imci.registerpatients;

import java.util.Calendar;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class DisplayInfoActivity extends Activity {
	
	String first_name;
    String last_name;
//    String genderString;
    boolean gender;
    Integer day;
    Integer month;
    Integer year;
    String born_on;
//    String address;
//    String village;
    int village_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        setContentView(R.layout.activity_display_info);

        first_name = intent.getStringExtra(NewPatientActivity.EXTRA_FIRST_NAME);
        last_name = intent.getStringExtra(NewPatientActivity.EXTRA_LAST_NAME);
//        genderString = intent.getStringExtra(NewPatientActivity.EXTRA_GENDER);
//        gender = Boolean.parseBoolean(genderString);
        gender = intent.getBooleanExtra(NewPatientActivity.EXTRA_GENDER, true);
        day = intent.getIntExtra(NewPatientActivity.EXTRA_DAY, 0);
        month = intent.getIntExtra(NewPatientActivity.EXTRA_MONTH, 0) + 1;
        year = intent.getIntExtra(NewPatientActivity.EXTRA_YEAR, 0);
        
//        address = intent.getStringExtra(NewPatientActivity.EXTRA_ADDRESS);
        village_id = intent.getIntExtra(NewPatientActivity.EXTRA_VILLAGE, 0);
//        village_id = Integer.parseInt(village);
        
//        current_date = intent.getStringExtra(NewPatientActivity.EXTRA_CURRENT_DATE);

        TextView textName = (TextView) findViewById(R.id.TextFirstName);
        textName.setText(first_name);
        
        TextView textLastName = (TextView) findViewById(R.id.TextLastName);
        textLastName.setText(last_name);
        
        TextView textGender = (TextView) findViewById(R.id.TextGender);
        if (gender == true)       
        	textGender.setText(R.string.male);
        else
        	textGender.setText(R.string.female);
        
        String strDay;
    	String strMonth;
    	String strYear;
    	
        if (day <=9 & day >= 0)
    		strDay = "0" + day.toString();
    	else
    		strDay = day.toString();
    	
    	if (month <=9 & month >= 0)
    		strMonth = "0" + month.toString();
    	else
    		strMonth = month.toString();
    	
    	if (year <=9 & year >= 0)
    		strYear = "0" + year.toString();
    	else
    		strYear = year.toString();
    	
    	born_on = strYear + "-" + 
    						strMonth + "-" + 
    						strDay;
        
        TextView textBornOn = (TextView) findViewById(R.id.TextBirth);
    	textBornOn.setText(born_on);
        
        TextView textVillage = (TextView) findViewById(R.id.TextVillage);
        textVillage.setText(Integer.toString(village_id));
                
    }

    public void confirmInfo(View view) {

    	Intent intentNew = new Intent(this, DoneRegisterActivity.class);

    	//Abrimos la base de datos 'DBPatients' en modo escritura
        PatientsSQLiteHelper usdbh =
        		new PatientsSQLiteHelper(this, "database.sqlite3", null, 1);
      		
        SQLiteDatabase db = usdbh.getWritableDatabase();
        
        //Si hemos abierto correctamente la base de datos
        if(db != null) {
        	
        	Calendar curDate = Calendar.getInstance();
        	Integer curDay = curDate.get(Calendar.DAY_OF_MONTH);
        	Integer curMonth = curDate.get(Calendar.MONTH) + 1;
        	Integer curYear = curDate.get(Calendar.YEAR);
        	Integer curHour = curDate.get(Calendar.HOUR_OF_DAY);
        	Integer curMin = curDate.get(Calendar.MINUTE);
        	Integer curSec = curDate.get(Calendar.SECOND);
        	 
        	String strDay;
        	String strMonth;
        	String strYear;
        	String strHour;
        	String strMin;
        	String strSec;
        	
        	if (curDay <=9 & curDay >= 0)
        		strDay = "0" + curDay.toString();
        	else
        		strDay = curDay.toString();
        	
        	if (curMonth <=9 & curMonth >= 0)
        		strMonth = "0" + curMonth.toString();
        	else
        		strMonth = curMonth.toString();
        	
        	if (curYear <=9 & curYear >= 0)
        		strYear = "0" + curYear.toString();
        	else
        		strYear = curYear.toString();
        	
        	if (curHour <=9 & curHour >= 0)
        		strHour = "0" + curHour.toString();
        	else
        		strHour = curHour.toString();
        	
        	if (curMin <=9 & curMin >= 0)
        		strMin = "0" + curMin.toString();
        	else
        		strMin = curMin.toString();
        	
        	if (curSec <=9 & curSec >= 0)
        		strSec = "0" + curSec.toString();
        	else
        		strSec = curSec.toString();     	
        	
        	String created_at = strYear + "-" + 
        						strMonth + "-" + 
        						strDay + " " + 
        						strHour + ":" + 
        						strMin + ":" + 
        						strSec;
        	
        	//Insertamos los datos en la tabla Usuarios
			db.execSQL("INSERT INTO children (village_id, first_name, " +
						"last_name," + "gender, born_on, created_at) " +
						"VALUES ('" + village_id + "', '" + first_name + 
						"', '" + last_name + "', '" + gender + "', '" + 
						born_on +"', '" + created_at +"')");
						
//						+ "', '" + current_date +"')");
		
			//Cerramos la base de datos
			db.close();
        }
        Intent intentPrev = new Intent();
        setResult(Activity.RESULT_OK, intentPrev);
        
        startActivity(intentNew);
        
        finish();
        
    }
    
    public void modifyInfo(View view) {
    	finish();
    }    

}
