package uk.co.danielbryant.shopping.productcatalogue.model.v1;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import static com.github.quiram.utils.ArgumentChecks.*;

public class Product {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;

    private Product() {
        // Needed for Jackson deserialization
    }

    public Product(String id, String name, String description, BigDecimal price) {
        ensureNotBlank(id, "id");
        ensureNotBlank(name, "name");
        ensureNotBlank(description, "description");
        ensurePrice(price);

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    private void ensurePrice(BigDecimal price) {
        ensureNotNull(price, "price");
        ensureGreaterThanZero(price.intValue(), "price");
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
