package com.imci.ica;

/**
 * Stores an application user's details
 * @author Antonin
 *
 */
public class User {
	public int id;
	public String name;
	public Boolean admin;
	
	public User(int id, String name, Boolean admin) {
		this.id = id;
		this.name = name;
		this.admin = admin;
	}
}
