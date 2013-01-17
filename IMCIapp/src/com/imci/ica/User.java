package com.imci.ica;

/**
 * Stores an application user's details
 * 
 * @author Antonin
 * 
 */
public class User {
	public int id; // The user's id
	public String name; // The user's name
	public Boolean admin; // If the user is an administrator

	public User(int id, String name, Boolean admin) {
		this.id = id;
		this.name = name;
		this.admin = admin;
	}
}
