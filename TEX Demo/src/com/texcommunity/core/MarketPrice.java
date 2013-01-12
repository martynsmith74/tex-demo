package com.texcommunity.core;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.ArrayList;
import java.util.List;

public class MarketPrice implements Cloneable {
	protected BigDecimal price;
	protected List<PriceRequest> requestList;
	protected List<PriceOffer> offerList;

	public BigDecimal getPrice() {
		return price;
	}
	public void setPrice(BigDecimal price) {
		this.price = price;
	}
	public String getDisplayPrice() {
		return price.round(new MathContext(3)).toString();
	}
	public List<PriceRequest> getRequestList() {
		return requestList;
	}
	public void setRequestList(List<PriceRequest> requestList) {
		this.requestList = requestList;
	}
	public List<PriceOffer> getOfferList() {
		return offerList;
	}
	public void setOfferList(List<PriceOffer> offerList) {
		this.offerList = offerList;
	}
	
	@Override
	public MarketPrice clone() {
		MarketPrice clone = new MarketPrice();
		clone.setPrice(price);
		List<PriceRequest> requestClone = new ArrayList<PriceRequest>();
		for (PriceRequest request : requestList) {
			requestClone.add(request.clone());
		}
		clone.setRequestList(requestClone);
		List<PriceOffer> offerClone = new ArrayList<PriceOffer>();
		for (PriceOffer offer : offerList) {
			offerClone.add(offer.clone());
		}
		clone.setOfferList(offerClone);
		return clone;
	}
}
