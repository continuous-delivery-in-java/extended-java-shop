package uk.co.danielbryant.shopping.stockmanager.model;

import com.amarinperez.utils.ReflectiveToStringCompareEquals;

import javax.persistence.Entity;
import javax.persistence.Id;

import static com.amarinperez.utils.ArgumentChecks.ensureNotBlank;
import static com.amarinperez.utils.ArgumentChecks.ensureNotNegative;

@Entity
public class Stock extends ReflectiveToStringCompareEquals<Stock> {

    @Id
    private String productId;
    private String sku;
    private int amountAvailable;

    private Stock() {
        // Needed by Spring
    }

    public Stock(String productId, String sku, int amountAvailable) {
        ensureNotNegative(amountAvailable, "amountAvailable");
        ensureNotBlank(productId, "productId");
        ensureNotBlank(sku, "sku");

        this.productId = productId;
        this.sku = sku;
        this.amountAvailable = amountAvailable;
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
}
