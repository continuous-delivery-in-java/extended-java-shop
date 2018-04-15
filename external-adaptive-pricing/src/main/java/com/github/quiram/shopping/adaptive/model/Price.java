package com.github.quiram.shopping.adaptive.model;

import java.math.BigDecimal;

public class Price {
    private BigDecimal price;

    public Price() {
        // Needed by Spring
    }

    public Price(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
