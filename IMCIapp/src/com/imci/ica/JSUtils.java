package com.imci.ica;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class JSUtils {
	
	/**
	 * Converts a HashMap to its Javascript equivalent
	 * 
	 * @param hashmap
	 *            the hashmap to convert
	 * @param variableName
	 *            the target Javascript variable's name
	 * @return a String containign the Javascript code necessary to set all the
	 *         variables corresponding to the HashMap
	 */
	public static String hashMapToJavascript(HashMap<String, Object> hashmap,
			String variableName) {
		if (hashmap == null || hashmap.isEmpty()) {
			return "";
		}
		StringBuilder builder = new StringBuilder("data={};");
		Iterator<Entry<String, Object>> iterator = hashmap.entrySet()
				.iterator();
		while (iterator.hasNext()) {
			Map.Entry<String, Object> pair = (Map.Entry<String, Object>) iterator
					.next();
			// System.out.println(pair.getKey() +" = "+
			// pair.getValue().toString());
			if (pair.getValue() instanceof String) {
				String value = ((String) pair.getValue()).replaceAll("'", "\'");
				builder.append(variableName + "['" + pair.getKey() + "']='"
						+ value + "';");
			} else {
				builder.append(variableName + "['" + pair.getKey() + "']="
						+ pair.getValue() + ";");
			}
		}
		return builder.toString();
	}


}
