package com.texcommunity.core;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RequestOfferMatch extends MarketPrice {

	private int matchedAmount;
	private Date matchTime;
	
	public int getMatchedAmount() {
		return matchedAmount;
	}
	public void setMatchedAmount(int matchedAmount) {
		this.matchedAmount = matchedAmount;
	}

	public Date getMatchTime() {
		return matchTime;
	}
	public void setMatchTime(Date matchTime) {
		this.matchTime = matchTime;
	}
	public String getDisplayTime() {
		return DateTimeUtil.printTime(matchTime);
	}

	@Override
	public RequestOfferMatch clone() {
		RequestOfferMatch clone = new RequestOfferMatch();
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
		clone.setMatchedAmount(matchedAmount);
		clone.setMatchTime(matchTime);
		return clone;
	}
}
