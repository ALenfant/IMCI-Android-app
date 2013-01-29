package com.imci.ica.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import android.annotation.SuppressLint;

/**
 * Class with Date Utils
 * 
 * @author Miguel
 * 
 */
public class DateUtils {

	/**
	 * Gives the current date and time adapted to a SQLite datetime column
	 * 
	 * @return the current time in SQLite datetime format
	 */
	@SuppressLint("SimpleDateFormat")
	public static String getCurrentDateTime() {
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");
		Date date = new Date();
		return dateFormat.format(date);
	}

	/**
	 * Gets the difference in months between two dates
	 * 
	 * @param date1
	 *            the first date
	 * @param date2
	 *            the second date
	 * @return the difference in months
	 */
	public static final int getMonthsDifference(Date date1, Date date2) {
		int m1 = date1.getYear() * 12 + date1.getMonth();
		int m2 = date2.getYear() * 12 + date2.getMonth();
		return (m2 - m1 + 1);
	}

	/**
	 * Gets the difference in months between two dates
	 * 
	 * @param year
	 *            the year of first date
	 * @param month
	 *            the month of first date
	 * @param date2
	 *            the second date
	 * @return the difference in months
	 */
	public static final int getMonthsDifference(int year, int month, Date date2) {
		int m1 = year * 12 + month + 1;
		int m2 = date2.getYear() * 12 + date2.getMonth();
		return (m2 - m1 + 1);
	}

	/**
	 * Get the Age Group the patient belongs
	 * 
	 * @param birth_date
	 * @return the age group
	 */
	public static int getAgeGroup(String birth_date) {

		int age_group;
		int days;

		String[] birthArray = birth_date.split("\\-");
		Integer bDay = Integer.parseInt(birthArray[0]);
		Integer bMonth = Integer.parseInt(birthArray[1]) - 1;
		Integer bYear = Integer.parseInt(birthArray[2]);
		GregorianCalendar birth = new GregorianCalendar(bYear, bMonth, bDay);
		GregorianCalendar current = new GregorianCalendar();
		int yearRange = current.get(Calendar.YEAR) - birth.get(Calendar.YEAR);
		days = current.get(Calendar.DAY_OF_YEAR)
				- birth.get(Calendar.DAY_OF_YEAR) + yearRange * 365;

		if (days >= 0 && days <= 7) {
			age_group = 0;
		} else if (days >= 8 && days <= 60) {
			age_group = 1;
		} else {
			age_group = 2;
		}

		return age_group;
	}

	/**
	 * Creating a string with Date format
	 * 
	 * @param day
	 * @param month
	 * @param year
	 * @return a string with date with format "yyyy-mm-dd"
	 */
	public static String dateString(Integer day, Integer month, Integer year) {
		String strDate = twoDigitsString(year) + "-"
				+ twoDigitsString(month + 1) + "-" + twoDigitsString(day);

		return strDate;
	}

	/**
	 * Creating a string from a Integer with two digits, even if number is less
	 * than 10.
	 * 
	 * @param number
	 * @return number with two digits
	 */
	public static String twoDigitsString(Integer number) {
		String str;

		if (number <= 9 & number >= 0)
			str = "0" + number.toString();
		else
			str = number.toString();

		return str;
	}

	/**
	 * Get the difference in days of two dates
	 * 
	 * @param year
	 * @param month
	 * @param day
	 * @return difference in days between two dates
	 */
	public static int differenceWithCurrentDate(int year, int month, int day) {
		int result;
		
		GregorianCalendar date1 = new GregorianCalendar(year, month + 1, day);
		GregorianCalendar date2 = new GregorianCalendar();

		// Check if it's the same year
		if (date1.get(Calendar.YEAR) == date2.get(Calendar.YEAR)) {
			result = date2.get(Calendar.DAY_OF_YEAR) - date1.get(Calendar.DAY_OF_YEAR);
			
		} else {
			int yearDiff = date2.get(Calendar.YEAR)
					- date1.get(Calendar.YEAR);

			result = (yearDiff * 365)
					+ (date2.get(Calendar.DAY_OF_YEAR)
							- date1.get(Calendar.DAY_OF_YEAR));

		}
		return result;
	}
}
