package uk.co.danielbryant.shopping.productcatalogue.model.v2;

import static com.github.quiram.utils.ArgumentChecks.ensure;
import static com.github.quiram.utils.ArgumentChecks.ensureNotNull;

public class BulkPrice {
    private UnitPrice unit;
    private int min;

    public BulkPrice(UnitPrice unit, int min) {
        ensureNotNull(unit, "unit price");
        ensure(min, "minimum amount", n -> n < 2, "be higher than one");
        this.unit = unit;
        this.min = min;
    }

    public UnitPrice getUnit() {
        return unit;
    }

    public void setUnit(UnitPrice unit) {
        this.unit = unit;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }
}
