package com.texcommunity.core;

public class UserCredentials {
	private UserRole userRole;
	private int tier;
	
	public UserCredentials(UserRole userRole) {
		this.userRole = userRole;
	}
	
	public UserRole getUserRole() {
		return userRole;
	}
	public String getUserRoleDisplay() {
		String tierString = "";
		if (hasTier()) {
			tierString = ", tier "+tier;
		}
		return userRole.toString() + tierString;
	}

	public int getTier() {
		return tier;
	}
	public void setTier(int tier) {
		this.tier = tier;
	}
	
	public boolean hasTier() {
		return userRole == UserRole.BORROWER;
	}
	
	public boolean isAbleToEnterRequest() {
		return (userRole == UserRole.BORROWER || userRole == UserRole.ADMIN);
	}
	public boolean isAbleToEnterOffer() {
		return (userRole == UserRole.INVESTOR || userRole == UserRole.ADMIN);
	}
	public boolean isAbleToEnterRequestAnyTier() {
		return (userRole == UserRole.ADMIN);
	}
	
	public boolean isShowAllMarket() {
		return (userRole == UserRole.ADMIN);
	}
	public boolean isShowUserMarket() {
		return (userRole == UserRole.BORROWER || userRole == UserRole.INVESTOR || userRole == UserRole.ADMIN);
	}
}
