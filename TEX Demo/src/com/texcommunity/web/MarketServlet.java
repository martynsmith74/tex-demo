package com.texcommunity.web;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.texcommunity.core.MarketPrice;
import com.texcommunity.core.PriceOffer;
import com.texcommunity.core.PriceRequest;
import com.texcommunity.core.RequestOfferMatch;
import com.texcommunity.core.TierPriceAdjuster;
import com.texcommunity.core.UserCredentials;
import com.texcommunity.core.UserRole;
import com.texcommunity.dao.MarketDao;


public class MarketServlet extends HttpServlet {

	private static final long serialVersionUID = 7124021168632926938L;
	private static final Logger log = Logger.getLogger(MarketServlet.class.getName());

	/**
	 * A GET means we just want to see the market, or we are resetting the market
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.fine("get");
		UserCredentials creds = getUserCreds(req);
		String action = req.getParameter("action");
		if (action != null) {
			if ("reset".equals(action)) {
				resetMarket();
			}
		}
		doCommon(req, resp, creds);
	}

	/**
	 * A POST means we are adding an offer or request
	 */
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		log.fine("post");
		UserCredentials creds = getUserCreds(req);
		processSubmit(req, creds);
		doCommon(req, resp, creds);
	}
	
	protected void doCommon(HttpServletRequest req, HttpServletResponse resp, UserCredentials creds)
			throws ServletException, IOException {
		HttpSession session = req.getSession(true);
		ServletContext context = session.getServletContext();
		
		req.setAttribute("userCredentials", creds);
		
		MarketBean market = populateMarketBean();
		req.setAttribute("marketBean", market);
		
		if (!creds.isShowAllMarket() && creds.isShowUserMarket()) {
			InvViewBean invBean = populateInvViewBean(market, creds);
			req.setAttribute("invViewBean", invBean);
		}
		
		context.getRequestDispatcher("/jsps/mainpage.jsp").forward(req, resp);		
	}

	/**
	 * 
	 * @param session
	 * @return
	 */
	private UserCredentials getUserCreds(HttpServletRequest request) {
		String role = (String) request.getParameter("userRole");
		if (role == null) {
			return new UserCredentials(UserRole.NONE);
		} else {
			UserRole userRole = UserRole.valueOf(role);
			UserCredentials creds = new UserCredentials(userRole);
			if (creds.hasTier()) {
				creds.setTier(Integer.parseInt(request.getParameter("tier")));
			}
			return creds;
		}
	}
	
	/**
	 * 
	 * @param role
	 * @return
	 */
	private MarketBean populateMarketBean() {
		MarketDao dao = new MarketDao();
		MarketBean market = new MarketBean();

		List<MarketPrice> priceList = dao.getDefaultMarketPrices();
		for (MarketPrice price: priceList) {
			List<PriceRequest> requestList = price.getRequestList();
			padRequestList(requestList);
			price.setRequestList(requestList);

			List<PriceOffer> offerList = price.getOfferList();
			padOfferList(offerList);
			price.setOfferList(offerList);
		}		
		market.setAdminPrices(priceList);
		
		List<RequestOfferMatch> matchList = dao.getRecentMatches();
		padMatchList(matchList);
		market.setMatchList(matchList);
		
		return market;
	}
	
	private void padRequestList(List<PriceRequest> requestList) {
		if (requestList == null) {
			requestList = new ArrayList<PriceRequest>();
		}
		
		if (requestList.size() < 4) {
			for (int i = requestList.size(); i < 4; i++) {
				requestList.add(new PriceRequest());
			}
		}
		Collections.reverse(requestList);
	}
	
	private void padOfferList(List<PriceOffer> offerList) {
		if (offerList == null) {
			offerList = new ArrayList<PriceOffer>();
		}
		if (offerList.size() < 4) {
			for (int i = offerList.size(); i < 4; i++) {
				offerList.add(new PriceOffer());
			}
		}
	}
	
	private void padMatchList(List<RequestOfferMatch> matchList) {
		if (matchList == null) {
			matchList = new ArrayList<RequestOfferMatch>();
		}
		if (matchList.size() < 10) {
			for (int i = matchList.size(); i < 10; i++) {
				matchList.add(new RequestOfferMatch());
			}
		}
	}
	
	private InvViewBean populateInvViewBean(MarketBean market, UserCredentials creds) {
		MarketDao dao = new MarketDao();
		List<MarketPrice> priceList = dao.getDefaultMarketPrices();
		InvViewBean invBean = new InvViewBean();
		TierPriceAdjuster priceAdjuster = new TierPriceAdjuster(creds);
		
		List<PriceRequest> requestList = new ArrayList<PriceRequest>();
		List<PriceOffer> offerList = new ArrayList<PriceOffer>();
		Collections.reverse(priceList);
		for (MarketPrice price : priceList) {
			if (!price.getRequestList().isEmpty()) {
//				priceAdjuster.filterRequestsByTier(price.getRequestList());
				PriceRequest request = null;
				for (PriceRequest tmpRequest : price.getRequestList()) {
					if (request == null) {
						request = tmpRequest.clone();
					} else {
						request.setAmount(request.getAmount() + tmpRequest.getAmount());
					}
					
				}
				if (request != null) {
					priceAdjuster.adjustPriceForTier(request);
					requestList.add(request);
				}
			}
			if (!price.getOfferList().isEmpty()) {
				PriceOffer offer = null;
				for (PriceOffer tmpOffer : price.getOfferList()) {
					if (offer == null) {
						offer = tmpOffer.clone();
					} else {
						offer.setAmount(offer.getAmount() + tmpOffer.getAmount());
					}
				}
				priceAdjuster.adjustPriceForTier(offer);
				offerList.add(offer);
			}
		}
		Collections.reverse(requestList);
		padRequestList(requestList);
		padOfferList(offerList);
		requestList.get(requestList.size()-1).setBest(true);
		offerList.get(0).setBest(true);
		invBean.setRequestList(requestList.subList(0, 4));
		invBean.setOfferList(offerList.subList(0, 4));
		
		return invBean;
	}
	
	private void processSubmit(HttpServletRequest req, UserCredentials creds) {
		String action = req.getParameter("action");
		MarketDao dao = new MarketDao();
		TierPriceAdjuster priceAdjuster = new TierPriceAdjuster(creds);
		
		RequestOfferMatch match = null;
		if ("offer".equals(action)) {
			String amountString = req.getParameter("amount");
			String yieldString = req.getParameter("yield");

			PriceOffer offer = new PriceOffer();
			offer.setAmount(Integer.parseInt(amountString));
			offer.setPrice(new BigDecimal(Double.parseDouble(yieldString)));
			offer.setOfferTime(new Date());

			match = dao.saveOffer(offer);
		}
		else if ("request".equals(action)) {
			String amountString = req.getParameter("amount");
			String aprString = req.getParameter("apr");
			String tierString = req.getParameter("tier");
			
			PriceRequest request = new PriceRequest();
			request.setAmount(Integer.parseInt(amountString));
			request.setPrice(new BigDecimal(Double.parseDouble(aprString)));
			priceAdjuster.adjustRequestForTier(request);
			request.setRequestTime(new Date());
			if (tierString != null && !tierString.isEmpty()) {
				request.setTier(Integer.parseInt(tierString));
			} else {
				request.setTier(1);
			}
			
			match = dao.saveRequest(request);
		}
		
		if (match != null) {
			req.setAttribute("match", match);
		}
	}
	
	private void resetMarket() {
		MarketDao dao = new MarketDao();
		dao.resetMarketPrices();
	}
}
