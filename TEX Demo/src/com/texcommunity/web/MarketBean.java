package com.texcommunity.web;

import java.util.List;

import com.texcommunity.core.MarketPrice;
import com.texcommunity.core.RequestOfferMatch;

public class MarketBean {
	private List<MarketPrice> investorPrices;
	private List<MarketPrice> borrowerPrices;
	private List<MarketPrice> adminPrices;
	private List<RequestOfferMatch> matchList;
	
	public List<MarketPrice> getInvestorPrices() {
		return investorPrices;
	}
	public void setInvestorPrices(List<MarketPrice> investorPrices) {
		this.investorPrices = investorPrices;
	}
	public List<MarketPrice> getBorrowerPrices() {
		return borrowerPrices;
	}
	public void setBorrowerPrices(List<MarketPrice> borrowerPrices) {
		this.borrowerPrices = borrowerPrices;
	}
	public List<MarketPrice> getAdminPrices() {
		return adminPrices;
	}
	public void setAdminPrices(List<MarketPrice> adminPrices) {
		this.adminPrices = adminPrices;
	}
	public List<RequestOfferMatch> getMatchList() {
		return matchList;
	}
	public void setMatchList(List<RequestOfferMatch> matchList) {
		this.matchList = matchList;
	}
}
