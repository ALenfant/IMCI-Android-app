package com.imci.ica;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import android.os.Bundle;
import android.app.Activity;
import android.database.Cursor;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.mozilla.javascript.*;

public class SignsClassificationActivity extends Activity {
	public final static String EXTRA_HASHMAP_DATA = "com.imci.ica.HASHMAP_DATA";
	public final static String baseJS =
			"function AT_LEAST_TWO_OF(){var trueargs = 0;for (var i = 0; i < arguments.length; ++i) {if (arguments[i]) {trueargs++;}} return (trueargs >= 2);}";

	@Override
	protected void onCreate(Bundle savedInstanceState) {		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signs_classification);
		
		@SuppressWarnings("unchecked")
		HashMap<String, Object> data = (HashMap<String, Object>)getIntent().getSerializableExtra(EXTRA_HASHMAP_DATA);
		
        /*HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("danger.boire", false);
        data.put("danger.vomit", "lol");
        data.put("danger.convulsions_passe", false);
        data.put("danger.lethargie", false);
        data.put("danger.convulsions_present", false);*/
        
        //Convert HashMap to JS values...
        StringBuilder builder = new StringBuilder("data={};");
        Iterator<Entry<String, Object>> iterator = data.entrySet().iterator();
        while(iterator.hasNext()) {
            Map.Entry<String, Object> pair = (Map.Entry<String,Object>)iterator.next();
            //System.out.println(pair.getKey() +" = "+ pair.getValue().toString());
            if (pair.getValue() instanceof String) {
            	String value = ((String)pair.getValue()).replaceAll("'", "\'");
            	builder.append("data['"+pair.getKey()+"']='"+value+"';");
            } else {
            	builder.append("data['"+pair.getKey()+"']="+pair.getValue()+";");
            }
        }
        
        //Will contain the results to display
        ArrayList<String> results = new ArrayList<String>();
        
        //Get all the equations...
        Database db = new Database(this);
        Cursor classificationsCursor = db.getClassifications();
        do {
            String eq = builder.toString() + " " + baseJS + " " + classificationsCursor.getString(2);//" !(data['danger.boire'] || data['danger.vomit'] || data['danger.convulsions_passe'] || data['danger.lethargie'] || data['danger.convulsions_present'])";
            System.out.println(eq);
            Context context = Context.enter();
            context.setOptimizationLevel(-1); //Disable compilation
            
            try {
                Scriptable scope = context.initStandardObjects();
                
                //scope.put("data", scope, data);
                
                Boolean result = (Boolean)context.evaluateString(scope, eq, "doit", 1, null);
                System.out.println("Result: " + result);
                if (result) {
                	results.add(classificationsCursor.getString(1));
                }
            } catch(Exception ex) {
            	if (ex instanceof java.lang.ClassCastException) {
            		System.out.println("Result: Unknown");
            	} else {
            		System.out.println("Result ERROR: "+ex.toString());
                }
            } finally {
                Context.exit();
            }
        } while (classificationsCursor.moveToNext());
        
        ArrayAdapter<String> arrayAdapter = 
        		new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, results);
        ((ListView)findViewById(R.id.listView_classifications)).setAdapter(arrayAdapter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_signs_classification, menu);
		return true;
	}

}
