package com.texcommunity.dao;

import java.math.BigDecimal;
import java.util.List;

import com.texcommunity.core.MarketPrice;
import com.texcommunity.core.PriceOffer;
import com.texcommunity.core.PriceRequest;
import com.texcommunity.core.RequestOfferMatch;
import static com.texcommunity.core.TestUtils.*;

import junit.framework.TestCase;


public class MarketDaoTest extends TestCase {

	MarketDao dao = new MarketDao();
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		/* This will create a default market with:
		 * REQUESTS		PRICE	OFFERS
		 * 				7.80	1m 01:20	1m 01:30
		 * 				7.75	500k 00:10
		 * 	250k 00:40	7.65
		 */ 
		dao.resetMarketPrices();
	}

	@Override
	protected void tearDown() throws Exception {
		super.tearDown();
	}
	
	
	public void testSaveRequestLowestPrice() {
		BigDecimal price = new BigDecimal(7.50);
		MarketPrice match = dao.saveRequest(buildRequest(300, price, 1));
		assertNull("match should be null", match);
		List<MarketPrice> priceList = dao.getDefaultMarketPrices();
		assertEquals("should be 4 market prices", 4, priceList.size());
		MarketPrice mPrice = priceList.get(3);
		assertEquals("last price should be 7.50", price, mPrice.getPrice());
		assertEquals("price should have one request", 1, mPrice.getRequestList().size());
		assertEquals("request amount should be correct", 300, mPrice.getRequestList().get(0).getAmount());
	}
	
	public void testSaveRequest78() {
		double pd = Double.parseDouble("7.8");
		BigDecimal price = new BigDecimal(pd);
		MarketPrice match = dao.saveOffer(buildOffer(300, price));
		assertNull("match should be null", match);
		List<MarketPrice> priceList = dao.getDefaultMarketPrices();
		assertEquals("should be 3 market prices", 3, priceList.size());
		MarketPrice mPrice = priceList.get(0);
		assertEquals("first price should be 7.8", price, mPrice.getPrice());
		assertEquals("price should have 3 offers", 3, mPrice.getOfferList().size());
		assertEquals("offer amount should be correct", 300, mPrice.getOfferList().get(2).getAmount());
	}
	

	public void testSaveRequestWithExactMatch() {
		BigDecimal price = P775;
		RequestOfferMatch match = dao.saveRequest(buildRequest(500000, price, 1));
		checkMatch(match, price, new int[] {500000}, new int[] {500000}, 500000);
		
		List<MarketPrice> priceList = dao.getDefaultMarketPrices();
		assertEquals("should be 2 market prices", 2, priceList.size());
		
		List<RequestOfferMatch> matchList = dao.getRecentMatches();
		assertEquals("should be 1 recent match", 1, matchList.size());
		checkMatch(matchList.get(0), price, new int[] {500000}, new int[] {500000}, 500000);
	}
	
	public void testSaveRequestWithExactMatchOffPrice() {
		BigDecimal price = new BigDecimal(7.76);
		RequestOfferMatch match = dao.saveRequest(buildRequest(500000, price, 1));
		checkMatch(match, P775, new int[] {500000}, new int[] {500000}, 500000);
		
		List<MarketPrice> priceList = dao.getDefaultMarketPrices();
		assertEquals("should be 2 market prices", 2, priceList.size());
		
		List<RequestOfferMatch> matchList = dao.getRecentMatches();
		assertEquals("should be 1 recent match", 1, matchList.size());
		checkMatch(matchList.get(0), P775, new int[] {500000}, new int[] {500000}, 500000);
	}
	
	public void testSaveRequestWithPartialMatchBiggerOffer() {
		BigDecimal price = P775;
		RequestOfferMatch match = dao.saveRequest(buildRequest(250000, price, 1));
		checkMatch(match, price, new int[] {250000}, new int[] {250000}, 250000);
		
		List<MarketPrice> priceList = dao.getDefaultMarketPrices();
		assertEquals("should be 3 market prices", 3, priceList.size());
		checkMarketPrice(priceList.get(1), price, new int[] {}, new int[] {250000});
		
		List<RequestOfferMatch> matchList = dao.getRecentMatches();
		assertEquals("should be 1 recent match", 1, matchList.size());
		checkMatch(matchList.get(0), price, new int[] {250000}, new int[] {250000}, 250000);
	}
	
	public void testSaveRequestWithPartialMatchBiggerRequest() {
		BigDecimal price = P775;
		RequestOfferMatch match = dao.saveRequest(buildRequest(750000, price, 1));
		checkMatch(match, price, new int[] {500000}, new int[] {500000}, 500000);
		
		List<MarketPrice> priceList = dao.getDefaultMarketPrices();
		assertEquals("should be 3 market prices", 3, priceList.size());
		checkMarketPrice(priceList.get(1), price, new int[] {250000}, new int[] {});
		
		List<RequestOfferMatch> matchList = dao.getRecentMatches();
		assertEquals("should be 1 recent match", 1, matchList.size());
		checkMatch(matchList.get(0), price, new int[] {500000}, new int[] {500000}, 500000);
	}
	
	public void testSaveRequestWithMatchMultipleOffer() {
		RequestOfferMatch match = dao.saveRequest(buildRequest(1750000, P780, 1));
		checkMatch(match, P775, new int[] {1750000}, new int[] {500000, 1000000, 250000}, 1750000);
		
		List<MarketPrice> priceList = dao.getDefaultMarketPrices();
		assertEquals("should be 2 market prices", 2, priceList.size());
		checkMarketPrice(priceList.get(0), P780, new int[] {}, new int[] {750000});
		
		List<RequestOfferMatch> matchList = dao.getRecentMatches();
		assertEquals("should be 1 recent match", 1, matchList.size());
		checkMatch(matchList.get(0), P775, new int[] {1750000}, new int[] {500000, 1000000, 250000}, 1750000);
	}
	
	public void testSaveOfferWithExactMatch() {
		BigDecimal price = P765;
		RequestOfferMatch match = dao.saveOffer(buildOffer(250000, price));
		checkMatch(match, price, new int[] {250000}, new int[] {250000}, 250000);
		
		List<MarketPrice> priceList = dao.getDefaultMarketPrices();
		assertEquals("should be 2 market prices", 2, priceList.size());
		
		List<RequestOfferMatch> matchList = dao.getRecentMatches();
		assertEquals("should be 1 recent match", 1, matchList.size());
		checkMatch(matchList.get(0), price, new int[] {250000}, new int[] {250000}, 250000);
	}
	
	public void testSaveOfferWithPartialMatchBiggerOffer() {
		BigDecimal price = P765;
		RequestOfferMatch match = dao.saveOffer(buildOffer(500000, price));
		checkMatch(match, price, new int[] {250000}, new int[] {250000}, 250000);
		
		List<MarketPrice> priceList = dao.getDefaultMarketPrices();
		assertEquals("should be 3 market prices", 3, priceList.size());
		checkMarketPrice(priceList.get(2), price, new int[] {}, new int[] {250000});
		
		List<RequestOfferMatch> matchList = dao.getRecentMatches();
		assertEquals("should be 1 recent match", 1, matchList.size());
		checkMatch(matchList.get(0), price, new int[] {250000}, new int[] {250000}, 250000);
	}
	
	public void testSaveOfferWithPartialMatchBiggerRequest() {
		BigDecimal price = P765;
		RequestOfferMatch match = dao.saveOffer(buildOffer(150000, price));
		checkMatch(match, price, new int[] {150000}, new int[] {150000}, 150000);
		
		List<MarketPrice> priceList = dao.getDefaultMarketPrices();
		assertEquals("should be 3 market prices", 3, priceList.size());
		checkMarketPrice(priceList.get(2), price, new int[] {100000}, new int[] {});
		
		List<RequestOfferMatch> matchList = dao.getRecentMatches();
		assertEquals("should be 1 recent match", 1, matchList.size());
		checkMatch(matchList.get(0), price, new int[] {150000}, new int[] {150000}, 150000);
	}
	
	public void testSaveOfferWithMatchMultipleOffer() {
		BigDecimal price = P765;
		dao.saveRequest(buildRequest(250000, price, 1));
		
		RequestOfferMatch match = dao.saveOffer(buildOffer(400000, price));
		checkMatch(match, price, new int[] {250000, 150000}, new int[] {400000}, 400000);
		
		List<MarketPrice> priceList = dao.getDefaultMarketPrices();
		assertEquals("should be 3 market prices", 3, priceList.size());
		checkMarketPrice(priceList.get(2), price, new int[] {100000}, new int[] {});
		
		List<RequestOfferMatch> matchList = dao.getRecentMatches();
		assertEquals("should be 1 recent match", 1, matchList.size());
		checkMatch(match, price, new int[] {250000, 150000}, new int[] {400000}, 400000);
	}
	
	public void testSaveOfferWithMatchMultipleOfferOffPrice() {
		BigDecimal price = new BigDecimal(7.50);
		dao.saveRequest(buildRequest(250000, price, 1));
		
		RequestOfferMatch match = dao.saveOffer(buildOffer(400000, price));
		checkMatch(match, P765, new int[] {250000, 150000}, new int[] {400000}, 400000);
		
		List<MarketPrice> priceList = dao.getDefaultMarketPrices();
		assertEquals("should be 3 market prices", 3, priceList.size());
		checkMarketPrice(priceList.get(2), price, new int[] {100000}, new int[] {});
		
		List<RequestOfferMatch> matchList = dao.getRecentMatches();
		assertEquals("should be 1 recent match", 1, matchList.size());
		checkMatch(match, P765, new int[] {250000, 150000}, new int[] {400000}, 400000);
	}
	

	
	private void checkMarketPrice(MarketPrice match, BigDecimal price, int[] requestAmounts, int[] offerAmounts) {
		assertNotNull("should not be null", match);
		assertEquals("price should be correct", price, match.getPrice());
		assertEquals("number of requests", requestAmounts.length, match.getRequestList().size());
		assertEquals("number of offers", offerAmounts.length, match.getOfferList().size());
		for (int i = 0; i < requestAmounts.length; i++) {
			assertEquals("request ("+i+") amount should be correct", requestAmounts[i], match.getRequestList().get(i).getAmount());
		}
		for (int i = 0; i < offerAmounts.length; i++) {
			assertEquals("offer ("+i+") amount should be correct", offerAmounts[i], match.getOfferList().get(i).getAmount());
		}
	}
	
	private void checkMatch(RequestOfferMatch match, BigDecimal price, int[] requestAmounts, int[] offerAmounts, int totalAmount) {
		checkMarketPrice(match, price, requestAmounts, offerAmounts);
		assertEquals("total amount should be correct", totalAmount, match.getMatchedAmount());
	}
	
	private PriceRequest buildRequest(int amount, BigDecimal price, int tier) {
		PriceRequest request = new PriceRequest();
		request.setAmount(amount);
		request.setPrice(price);
		request.setTier(tier);
		return request;
	}
	
	private PriceOffer buildOffer(int amount, BigDecimal price) {
		PriceOffer offer = new PriceOffer();
		offer.setAmount(amount);
		offer.setPrice(price);
		return offer;
	}
}
