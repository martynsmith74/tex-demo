package com.texcommunity.web;

import java.util.List;

import com.texcommunity.core.PriceOffer;
import com.texcommunity.core.PriceRequest;

public class InvViewBean {
	private List<PriceRequest> requestList;
	private List<PriceOffer> offerList;
	
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
}
