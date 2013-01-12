package com.texcommunity.core;

public enum UserRole {
	NONE, ADMIN, BORROWER, INVESTOR;
	
	public String getValue() {
		return this.toString();
	}
}
