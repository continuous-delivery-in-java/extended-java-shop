package uk.co.danielbryant.djshopping.productcatalogue.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import static org.apache.commons.lang3.StringUtils.isBlank;

public class Product {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;

    public Product() {
        // Needed for Jackson deserialization
    }

    public Product(String id, String name, String description, BigDecimal price) {
        ensureString(id, "id");
        ensureString(name, "name");
        ensureString(description, "description");
        ensureAmount(price);

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    private void ensureAmount(BigDecimal price) {
        if (price.compareTo(new BigDecimal(0)) <= 0) {
            throw new IllegalArgumentException("price must be greater than zero");
        }
    }

    private void ensureString(String field, final String fieldName) {
        if (isBlank(field)) {
            throw new IllegalArgumentException(fieldName + " must have an actual value");
        }
    }


    @JsonProperty
    public String getId() {
        return id;
    }

    @JsonProperty
    public String getName() {
        return name;
    }

    @JsonProperty
    public String getDescription() {
        return description;
    }

    @JsonProperty
    public BigDecimal getPrice() {
        return price;
    }
}
