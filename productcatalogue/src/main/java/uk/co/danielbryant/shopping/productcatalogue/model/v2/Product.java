package uk.co.danielbryant.shopping.productcatalogue.model.v2;

import com.fasterxml.jackson.annotation.JsonProperty;

import static com.github.quiram.utils.ArgumentChecks.ensureNotBlank;
import static com.github.quiram.utils.ArgumentChecks.ensureNotNull;

public class Product {
    private String id;
    private String name;
    private String description;
    private Price price;

    private Product() {
        // Needed for Jackson deserialization
    }

    public Product(String id, String name, String description, Price price) {
        ensureNotBlank(id, "id");
        ensureNotBlank(name, "name");
        ensureNotBlank(description, "description");
        ensureNotNull(price, "price");

        this.id = id;
        this.name = name;
        this.description = description;
        this.price = price;
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
    public Price getPrice() {
        return price;
    }

    public uk.co.danielbryant.shopping.productcatalogue.model.v1.Product asV1Product() {
        return new uk.co.danielbryant.shopping.productcatalogue.model.v1.Product(id, name, description, price.getSingle().getValue());
    }
}
