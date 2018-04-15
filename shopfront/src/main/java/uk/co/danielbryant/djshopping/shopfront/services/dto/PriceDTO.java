package uk.co.danielbryant.djshopping.shopfront.services.dto;

import java.math.BigDecimal;

public class PriceDTO {
    private BigDecimal price;

    public PriceDTO() {
        // Needed by Spring
    }

    public PriceDTO(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
