package com.texcommunity.core;

import java.math.BigDecimal;
import java.math.MathContext;

public class PriceIOI implements Priceable {

	protected int amount;
	protected BigDecimal price;
	protected boolean best;

	public PriceIOI() {
		super();
	}
	public PriceIOI(int amount) {
		this.amount = amount;
	}

	public int getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public String getDisplayPrice() {
		return price.round(new MathContext(3)).toString();
	}

	public boolean isBest() {
		return best;
	}

	public void setBest(boolean best) {
		this.best = best;
	}

}