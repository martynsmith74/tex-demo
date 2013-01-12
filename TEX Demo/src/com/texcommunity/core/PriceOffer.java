package com.texcommunity.core;

import java.util.Date;

public class PriceOffer extends PriceIOI implements Cloneable {
	private Date offerTime;
	
	public PriceOffer() {}
	
	public PriceOffer(int amount) {
		super(amount);
	}

	public Date getOfferTime() {
		return offerTime;
	}
	public void setOfferTime(Date offerTime) {
		this.offerTime = offerTime;
	}
	public String getDisplayTime() {
		return DateTimeUtil.printTime(offerTime);
	}

	@Override
	public PriceOffer clone() {
		PriceOffer clone = new PriceOffer(amount);
		clone.setOfferTime(offerTime);
		clone.setPrice(price);
		clone.setBest(best);
		return clone;
	}
}
