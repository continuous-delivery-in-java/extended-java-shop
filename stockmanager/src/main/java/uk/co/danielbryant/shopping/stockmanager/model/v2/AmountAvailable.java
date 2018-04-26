package uk.co.danielbryant.shopping.stockmanager.model.v2;

import com.github.quiram.utils.ArgumentChecks;
import com.github.quiram.utils.ReflectiveToStringCompareEquals;

import javax.persistence.Embeddable;

import static com.github.quiram.utils.ArgumentChecks.ensure;
import static com.github.quiram.utils.ArgumentChecks.ensureNotNegative;

@Embeddable
public class AmountAvailable extends ReflectiveToStringCompareEquals<AmountAvailable> {
    private int total;
    private int perPurchase;

    @SuppressWarnings("unused")
    public AmountAvailable() {
        // Needed for Spring
    }

    public AmountAvailable(int total, int perPurchase) {
        ensureNotNegative(total, "total");
        ensureNotNegative(perPurchase, "perPurchase");
        ensure(() -> perPurchase > total, "perPurchase must not be higher than total");

        this.total = total;
        this.perPurchase = perPurchase;
    }

    public int getPerPurchase() {
        return perPurchase;
    }

    public void setPerPurchase(int perPurchase) {
        this.perPurchase = perPurchase;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }
}
