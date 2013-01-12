package com.texcommunity.core;

import java.math.BigDecimal;
import java.util.List;

import junit.framework.TestCase;

import static com.texcommunity.core.TestUtils.*;

/**
 * 
 * @author Martyn Smith
 */
public class MarketTest extends TestCase {
	
	private Market market;
	
	/** 
	 * This will create a default market with:
	 * REQUESTS		PRICE	OFFERS
	 * 				7.80	1m 01:20	1m 01:30
	 * 				7.75	500k 00:10
	 * 	250k 00:40	7.65
	 * 	600k 00:30	7.60
	 */ 
	protected void setUp() throws Exception {
		super.setUp();
		market = new Market();
		
		PriceOffer po780_1 = new PriceOffer(1000000);
		po780_1.setPrice(P780);
		po780_1.setOfferTime(DateTimeUtil.createDateFromTime(1, 20));
		market.addOffer(po780_1);
		
		PriceOffer po780_2 = new PriceOffer(1000000);
		po780_2.setPrice(P780);
		po780_2.setOfferTime(DateTimeUtil.createDateFromTime(1, 30));
		market.addOffer(po780_2);
		
		PriceOffer po775_1 = new PriceOffer(500000);
		po775_1.setPrice(P775);
		po775_1.setOfferTime(DateTimeUtil.createDateFromTime(0, 10));
		market.addOffer(po775_1);
		
		PriceRequest pr765_1 = new PriceRequest(250000);
		pr765_1.setPrice(P765);
		pr765_1.setRequestTime(DateTimeUtil.createDateFromTime(0, 40));
		pr765_1.setTier(1);
		market.addRequest(pr765_1);
		
		PriceRequest pr760_1 = new PriceRequest(600000);
		pr760_1.setPrice(P760);
		pr760_1.setRequestTime(DateTimeUtil.createDateFromTime(0, 30));
		pr760_1.setTier(1);
		market.addRequest(pr760_1);
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		market = null;
	}

	public void testGetRequestsFromPriceSingleExactMatch() {
		checkIOIList(market.getRequestsFromPrice(P765), new BigDecimal[] {P765}, new int[] {250000});
	}
	public void testGetRequestsFromPriceSingleOffMatch() {
		checkIOIList(market.getRequestsFromPrice(new BigDecimal(7.64)), new BigDecimal[] {P765}, new int[] {250000});
	}
	public void testGetRequestsFromPriceMultiPrice() {
		checkIOIList(market.getRequestsFromPrice(P760), new BigDecimal[] {P765, P760}, new int[] {250000, 600000});
	}

	public void testGetOffersFromPriceSingleExactMatch() {
		checkIOIList(market.getOffersFromPrice(P775), new BigDecimal[] {P775}, new int[] {500000});
	}
	public void testGetOffersFromPriceSingleOffMatch() {
		checkIOIList(market.getOffersFromPrice(new BigDecimal(7.76)), new BigDecimal[] {P775}, new int[] {500000});
	}
	public void testGetOffersFromPriceSingleMultiMatch() {
		checkIOIList(market.getOffersFromPrice(P780), new BigDecimal[] {P775, P780, P780}, new int[] {500000, 1000000, 1000000});
	}

	
	private void checkIOIList(List<? extends PriceIOI> ioiList, BigDecimal[] prices, int[] amounts) {
		assertNotNull("should not be null", ioiList);
		assertEquals("number of requests", prices.length, ioiList.size());
		for (int i = 0; i < prices.length; i++) {
			assertEquals("request ("+i+") price should be correct", prices[i], ioiList.get(i).getPrice());
		}
		for (int i = 0; i < amounts.length; i++) {
			assertEquals("request ("+i+") amount should be correct", amounts[i], ioiList.get(i).getAmount());
		}
	}
}
