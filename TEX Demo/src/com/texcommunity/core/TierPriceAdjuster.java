package com.texcommunity.core;

import java.math.BigDecimal;


/**
 * 
 * @author Martyn Smith
 */
public class TierPriceAdjuster {
	
	private int tier;
	private BigDecimal spread;
	private boolean adjustPrice;

	public TierPriceAdjuster(UserCredentials creds) {
		if (creds.hasTier()) {
			tier = creds.getTier();
			switch (tier) {
				case 1:
					spread = new BigDecimal(1.5);
					break;
				case 2:
					spread = new BigDecimal(2.5);
					break;
				case 3:
					spread = new BigDecimal(3.5);
					break;
				case 4:
					spread = new BigDecimal(4.5);
					break;
				default:
					spread = new BigDecimal(0);
			}
			adjustPrice = true;
		} else {
			adjustPrice = false;
		}
	}

	public void adjustPriceForTier(Priceable price) {
		if (adjustPrice) {
			price.setPrice(price.getPrice().add(spread));
		}
	}
	
	public void adjustRequestForTier(PriceIOI req) {
		if (adjustPrice) {
			req.setPrice(req.getPrice().subtract(spread));
		}
	}
	
//	public void filterRequestsByTier(List<PriceRequest> reqList) {
//		ListIterator<PriceRequest> iter = reqList.listIterator();
//		while (iter.hasNext()) {
//			PriceRequest req = iter.next();
//			if (req.getTier() < tier) {
//				iter.remove();
//			}
//		}
//	}
}
