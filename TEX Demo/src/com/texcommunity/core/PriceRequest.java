package com.texcommunity.core;

import java.util.Date;

public class PriceRequest extends PriceIOI implements Cloneable {
	private Date requestTime;
	private int tier;
	
	public PriceRequest() {}
	
	public PriceRequest(int amount) {
		super(amount);
	}

	public Date getRequestTime() {
		return requestTime;
	}
	public void setRequestTime(Date requestTime) {
		this.requestTime = requestTime;
	}
	public String getDisplayTime() {
		return DateTimeUtil.printTime(requestTime);
	}

	public int getTier() {
		return tier;
	}
	public void setTier(int tier) {
		this.tier = tier;
	}

	@Override
	public PriceRequest clone() {
		PriceRequest clone = new PriceRequest(amount);
		clone.setPrice(price);
		clone.setRequestTime(requestTime);
		clone.setTier(tier);
		clone.setBest(best);
		return clone;
	}
}
