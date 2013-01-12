package com.texcommunity.core;

import java.math.BigDecimal;

public interface Priceable {
	public BigDecimal getPrice();
	public void setPrice(BigDecimal price);
}
