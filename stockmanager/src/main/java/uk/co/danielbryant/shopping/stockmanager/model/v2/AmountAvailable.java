package uk.co.danielbryant.shopping.stockmanager.model.v2;

import com.github.quiram.utils.ArgumentChecks;
import com.github.quiram.utils.ReflectiveToStringCompareEquals;

public class AmountAvailable extends ReflectiveToStringCompareEquals<AmountAvailable> {
    private int total;
    private int perPurchase;

    public AmountAvailable(int total, int perPurchase) {
        ArgumentChecks.ensureNotNegative(total, "total");
        ArgumentChecks.ensureNotNegative(perPurchase, "perPurchase");
        ArgumentChecks.ensure(perPurchase, "perPurchase", p -> p > total, "not be higher than total");

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
