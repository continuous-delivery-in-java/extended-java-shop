package uk.co.danielbryant.shopping.stockmanager.model.v1;

import com.github.quiram.utils.ReflectiveToStringCompareEquals;

import static com.github.quiram.utils.ArgumentChecks.ensureNotBlank;
import static com.github.quiram.utils.ArgumentChecks.ensureNotNegative;

public class Stock extends ReflectiveToStringCompareEquals<Stock> {

    private String productId;
    private String sku;
    private int amountAvailable;

    @SuppressWarnings("unused")
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
