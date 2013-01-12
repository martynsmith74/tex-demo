package com.texcommunity.core;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.NavigableMap;
import java.util.TreeMap;

public class Market {
	private List<MarketPrice> priceList = null;
	private TreeMap<BigDecimal, MarketPrice> priceMap = null;
	
	public Market() {
		priceList = new ArrayList<MarketPrice>();
		priceMap = new TreeMap<BigDecimal, MarketPrice>();
	}
	
	
	/**
	 * 
	 * @return
	 */
	public List<MarketPrice> getPriceList() {
		return priceList;
	}

	/**
	 * 
	 * @param request
	 */
	public void addRequest(PriceRequest request) {
		BigDecimal price = request.getPrice();
		MarketPrice marketPrice = getOrAddMarketPrice(price);
		marketPrice.getRequestList().add(request);
	}

	/**
	 * 
	 * @param offer
	 */
	public void addOffer(PriceOffer offer) {
		BigDecimal price = offer.getPrice();
		MarketPrice marketPrice = getOrAddMarketPrice(price);
		marketPrice.getOfferList().add(offer);
	}
	
	private MarketPrice getOrAddMarketPrice(BigDecimal price) {
		MarketPrice marketPrice;
		if (priceMap.containsKey(price)) {
			marketPrice = priceMap.get(price);
		} else {
			marketPrice = new MarketPrice();
			marketPrice.setPrice(price);
			marketPrice.setRequestList(new ArrayList<PriceRequest>());
			marketPrice.setOfferList(new ArrayList<PriceOffer>());
			priceMap.put(price, marketPrice);
			insertPriceIntoList(marketPrice);
		}
		return marketPrice;
	}
	
	private void insertPriceIntoList(MarketPrice marketPrice) {
		ListIterator<MarketPrice> iter = priceList.listIterator();
		BigDecimal currPrice = new BigDecimal(1000);
		while (iter.hasNext() && marketPrice.getPrice().compareTo(currPrice) < 0) { 
			currPrice = iter.next().getPrice();
		}
		if (iter.hasPrevious() && iter.hasNext()) {
			iter.previous();
		}
		iter.add(marketPrice);
	}
	
	/**
	 * Returns a list of requests for prices >= the given price, sorted from the highest price downwards
	 * 
	 * @param price
	 * @return
	 */
	public List<PriceRequest> getRequestsFromPrice(BigDecimal price) {
		List<PriceRequest> requestList = new ArrayList<PriceRequest>();
		NavigableMap<BigDecimal, MarketPrice> prices = priceMap.tailMap(price, true);
		for (BigDecimal tmpPrice : prices.descendingKeySet()) {
			requestList.addAll(prices.get(tmpPrice).getRequestList());
		}
		return requestList;
	}
	
	/**
	 * Returns a list of offers for prices <= the given price, sorted from the lowest price upwards
	 * 
	 * @param price
	 * @return
	 */
	public List<PriceOffer> getOffersFromPrice(BigDecimal price) {
		List<PriceOffer> offerList = new ArrayList<PriceOffer>();
		NavigableMap<BigDecimal, MarketPrice> prices = priceMap.headMap(price, true);
		for (BigDecimal tmpPrice : prices.keySet()) {
			offerList.addAll(prices.get(tmpPrice).getOfferList());
		}
		return offerList;
	}
	
	/**
	 * 
	 * @param price
	 * @return
	 */
	public MarketPrice getMarketAtPrice(BigDecimal price) {
		return priceMap.get(price);
	}
	
	/**
	 * 
	 * @param price
	 */
	public void removePrice(MarketPrice price) {
		priceMap.remove(price.getPrice());
		priceList.remove(price);
	}
	
	/**
	 * 
	 * @param req
	 */
	public void removeRequest(PriceRequest req) {
		MarketPrice mPrice = priceMap.get(req.getPrice());
		mPrice.getRequestList().remove(req);
		if (mPrice.getRequestList().isEmpty() && mPrice.getOfferList().isEmpty()) {
			removePrice(mPrice);
		}
	}

	/**
	 * 
	 * @param offer
	 */
	public void removeOffer(PriceOffer offer) {
		MarketPrice mPrice = priceMap.get(offer.getPrice());
		mPrice.getOfferList().remove(offer);
		if (mPrice.getRequestList().isEmpty() && mPrice.getOfferList().isEmpty()) {
			removePrice(mPrice);
		}
	}

}
