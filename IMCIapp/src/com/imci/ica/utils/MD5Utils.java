package com.imci.ica.utils;

import java.security.MessageDigest;

/**
 * Class to use MD5 hashing
 * 
 * @author http://mobile.dzone.com/news/android-snippet-making-md5
 * 
 */
class MD5Utils {
	/**
	 * Computes the MD5 hash of the given string
	 * 
	 * @param str
	 *            the string we want to hash
	 * @return the MD5 hash of the string
	 * @throws Exception
	 */
	public static String md5(String str) throws Exception {
		StringBuilder sb = new StringBuilder();
		for (byte b : md5(str.getBytes()))
			sb.append(Integer.toHexString(0x100 + (b & 0xff)).substring(1));
		return sb.toString();
	}

	/**
	 * Computes the MD5 hash of the given bytes array
	 * 
	 * @param data
	 *            the data we want to hash
	 * @return the MD5 hash of the bytes array
	 * @throws Exception
	 */
	public static byte[] md5(byte[] data) throws Exception {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(data);
		return md5.digest();
	}
}
