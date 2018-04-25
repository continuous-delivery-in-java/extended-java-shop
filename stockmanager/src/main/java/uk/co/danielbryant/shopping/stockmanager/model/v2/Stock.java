package uk.co.danielbryant.shopping.stockmanager.model.v2;

import com.github.quiram.utils.ReflectiveToStringCompareEquals;

import javax.persistence.Entity;
import javax.persistence.Id;

import static com.github.quiram.utils.ArgumentChecks.ensureNotBlank;
import static com.github.quiram.utils.ArgumentChecks.ensureNotNull;

@Entity
public class Stock extends ReflectiveToStringCompareEquals<Stock> {

    @Id
    private String productId;
    private String sku;
    private AmountAvailable amountAvailable;

    private Stock() {
        // Needed by Spring
    }

    public Stock(String productId, String sku, AmountAvailable amountAvailable) {
        ensureNotNull(amountAvailable, "amountAvailable");
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

    public AmountAvailable getAmountAvailable() {
        return amountAvailable;
    }


    public uk.co.danielbryant.shopping.stockmanager.model.v1.Stock asV1Stock() {
        return new uk.co.danielbryant.shopping.stockmanager.model.v1.Stock(productId, sku, amountAvailable.getTotal());
    }
}
