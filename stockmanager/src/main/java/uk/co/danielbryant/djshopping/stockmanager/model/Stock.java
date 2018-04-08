package uk.co.danielbryant.djshopping.stockmanager.model;

import javax.persistence.Entity;
import javax.persistence.Id;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.builder.ToStringBuilder.reflectionToString;

@Entity
public class Stock {

    @Id
    private String productId;
    private String sku;
    private int amountAvailable;

    protected Stock() {
    }

    public Stock(String productId, String sku, int amountAvailable) {
        ensureAmount(amountAvailable);
        ensureString(productId, "productId");
        ensureString(sku, "sku");

        this.productId = productId;
        this.sku = sku;
        this.amountAvailable = amountAvailable;
    }

    private void ensureAmount(int amountAvailable) {
        if (amountAvailable < 0) {
            throw new IllegalArgumentException("amountAvailable cannot be negative");
        }
    }

    private void ensureString(String field, final String fieldName) {
        if (isBlank(field)) {
            throw new IllegalArgumentException(fieldName + " must have an actual value");
        }
    }

    public String getProductId() {
        return productId;
    }

    public String getSku() {
        return sku;
    }

    public int getAmountAvailable() {
        return amountAvailable;
    }

    @Override
    public String toString() {
        return reflectionToString(this);
    }
}
