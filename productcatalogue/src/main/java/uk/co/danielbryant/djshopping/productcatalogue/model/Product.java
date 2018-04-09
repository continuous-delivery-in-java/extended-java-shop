package uk.co.danielbryant.djshopping.productcatalogue.model;

import com.amarinperez.utils.ArgumentChecks;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;

import static com.amarinperez.utils.ArgumentChecks.ensureGreaterThanZero;
import static com.amarinperez.utils.ArgumentChecks.ensureNotBlank;
import static org.apache.commons.lang3.StringUtils.isBlank;

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
        ensureAmount(price);

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
    }

    private void ensureAmount(BigDecimal price) {
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
