package com.texcommunity.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import com.texcommunity.core.DateTimeUtil;
import com.texcommunity.core.Market;
import com.texcommunity.core.MarketPrice;
import com.texcommunity.core.PriceOffer;
import com.texcommunity.core.PriceRequest;
import com.texcommunity.core.RequestOfferMatch;


/**
 * 
 * @author Martyn Smith
 */
public class MarketDao {
	
	private static Market market = null;
	private static LinkedList<RequestOfferMatch> matchList = null;
	
	public MarketDao() {
		if (market == null) {
			initialiseDefaultMarket();
		}
	}
	
	private void initialiseDefaultMarket() {
		market = new Market();
		matchList = new LinkedList<RequestOfferMatch>();
		
		PriceOffer po780_1 = new PriceOffer(1000000);
		po780_1.setPrice(new BigDecimal(7.80));
		po780_1.setOfferTime(DateTimeUtil.createDateFromTime(1, 20));
		market.addOffer(po780_1);
		
		PriceOffer po780_2 = new PriceOffer(1000000);
		po780_2.setPrice(new BigDecimal(7.80));
		po780_2.setOfferTime(DateTimeUtil.createDateFromTime(1, 30));
		market.addOffer(po780_2);
		
		PriceOffer po775_1 = new PriceOffer(500000);
		po775_1.setPrice(new BigDecimal(7.75));
		po775_1.setOfferTime(DateTimeUtil.createDateFromTime(0, 10));
		market.addOffer(po775_1);
		
		PriceRequest pr765_1 = new PriceRequest(250000);
		pr765_1.setPrice(new BigDecimal(7.65));
		pr765_1.setRequestTime(DateTimeUtil.createDateFromTime(0, 40));
		pr765_1.setTier(1);
		market.addRequest(pr765_1);
	}
	
	/**
	 * 
	 * @return
	 */
	public List<MarketPrice> getDefaultMarketPrices() {
		return cloneList(market.getPriceList());
	}
	
	private List<MarketPrice> cloneList(List<MarketPrice> origList) {
		List<MarketPrice> copy = new ArrayList<MarketPrice>();
		for (MarketPrice price: origList) {
			copy.add(price.clone());
		}
		return copy;
	}
	
	private List<RequestOfferMatch> cloneMatchList(List<RequestOfferMatch> origList) {
		List<RequestOfferMatch> copy = new ArrayList<RequestOfferMatch>();
		for (RequestOfferMatch match: origList) {
			copy.add(match.clone());
		}
		return copy;
	}
	
	/**
	 * 
	 * @return
	 */
	public List<RequestOfferMatch> getRecentMatches() {
		return cloneMatchList(matchList);
	}
	
	/**
	 * 
	 * @param request
	 * @return details of a match if there is one, otherwise null
	 */
	public RequestOfferMatch saveRequest(PriceRequest request) {
		market.addRequest(request);
		return checkForMatchingOffers(request);
	}
	
	/**
	 * 
	 * @param offer
	 * @return details of a match if there is one, otherwise null
	 */
	public RequestOfferMatch saveOffer(PriceOffer offer) {
		market.addOffer(offer);
		return checkForMatchingRequests(offer);
	}
	
	
	
	private RequestOfferMatch checkForMatchingOffers(PriceRequest req) {
		RequestOfferMatch match = null;
		List<PriceOffer> matchingOffers = market.getOffersFromPrice(req.getPrice());

		if (matchingOffers.isEmpty()) {
			return null;
		} else {
			int amountToFill = req.getAmount();
			int totalMatchedAmount = 0;
			match = new RequestOfferMatch();
			// The first match will be the best price
			match.setPrice(matchingOffers.get(0).getPrice());
			match.setRequestList(new ArrayList<PriceRequest>());
			match.setOfferList(new ArrayList<PriceOffer>());

			ListIterator<PriceOffer> iter = matchingOffers.listIterator();
			while (amountToFill > 0 && iter.hasNext()) {
				PriceOffer offer = iter.next();
				
				if (offer.getAmount() >= amountToFill) {
					PriceOffer matchedOffer = offer.clone();
					matchedOffer.setAmount(amountToFill);
					offer.setAmount(offer.getAmount() - amountToFill);
					if (offer.getAmount() == 0) {
						market.removeOffer(offer);
					}
					
					match.getOfferList().add(matchedOffer);
					if (match.getRequestList().isEmpty()) {
						match.getRequestList().add(req.clone());
					} else {
						PriceRequest matchedRequest = match.getRequestList().get(0);
						matchedRequest.setAmount(matchedRequest.getAmount() + amountToFill);
					}
					market.removeRequest(req);
					totalMatchedAmount += amountToFill;
					amountToFill = 0;
				} else {
					PriceOffer matchedOffer = offer.clone();
					match.getOfferList().add(matchedOffer);
					
					PriceRequest matchedRequest;
					if (match.getRequestList().isEmpty()) {
						matchedRequest = req.clone();
						matchedRequest.setAmount(0);
						match.getRequestList().add(matchedRequest);
					} else {
						matchedRequest = match.getRequestList().get(0);
					}
					int matchedAmount = offer.getAmount();
					matchedRequest.setAmount(matchedRequest.getAmount() + matchedAmount);
					
					amountToFill -= matchedAmount;
					totalMatchedAmount += matchedAmount;
					req.setAmount(amountToFill);
					
					iter.remove();
					market.removeOffer(offer);
				}
			}

			match.setMatchedAmount(totalMatchedAmount);
			match.setMatchTime(new Date());
			matchList.push(match);
			return match.clone();
		}
	}
	
	private RequestOfferMatch checkForMatchingRequests(PriceOffer offer) {
		RequestOfferMatch match = null;
		List<PriceRequest> matchingReqs = market.getRequestsFromPrice(offer.getPrice());

		if (matchingReqs.isEmpty()) {
			return null;
		} else {
			int amountToFill = offer.getAmount();
			int totalMatchedAmount = 0;
			match = new RequestOfferMatch();
			// The first match will be the best price
			match.setPrice(matchingReqs.get(0).getPrice());
			match.setRequestList(new ArrayList<PriceRequest>());
			match.setOfferList(new ArrayList<PriceOffer>());

			ListIterator<PriceRequest> iter = matchingReqs.listIterator();
			while (amountToFill > 0 && iter.hasNext()) {
				PriceRequest request = iter.next();
				
				if (request.getAmount() >= amountToFill) {
					PriceRequest matchedReq = request.clone();
					matchedReq.setAmount(amountToFill);
					request.setAmount(request.getAmount() - amountToFill);
					if (request.getAmount() == 0) {
						market.removeRequest(request);
					}
					
					match.getRequestList().add(matchedReq);
					if (match.getOfferList().isEmpty()) {
						match.getOfferList().add(offer.clone());
					} else {
						PriceOffer matchedOffer = match.getOfferList().get(0);
						matchedOffer.setAmount(matchedOffer.getAmount() + amountToFill);
					}
					market.removeOffer(offer);
					totalMatchedAmount += amountToFill;
					amountToFill = 0;
				} else {
					PriceRequest matchedReq = request.clone();
					match.getRequestList().add(matchedReq);
					
					PriceOffer matchedOffer;
					if (match.getOfferList().isEmpty()) {
						matchedOffer = offer.clone();
						matchedOffer.setAmount(0);
						match.getOfferList().add(matchedOffer);
					} else {
						matchedOffer = match.getOfferList().get(0);
					}
					int matchedAmount = request.getAmount();
					matchedOffer.setAmount(matchedOffer.getAmount() + matchedAmount);
					
					amountToFill -= matchedAmount;
					totalMatchedAmount += matchedAmount;
					offer.setAmount(amountToFill);
					
					iter.remove();
					market.removeRequest(request);
				}
			}

			match.setMatchedAmount(totalMatchedAmount);
			match.setMatchTime(new Date());
			matchList.push(match);
			return match.clone();
		}
	}
	
	
	/**
	 * 
	 */
	public void resetMarketPrices() {
		initialiseDefaultMarket();
	}
}
