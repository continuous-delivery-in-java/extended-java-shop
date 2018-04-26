package uk.co.danielbryant.shopping.productcatalogue.model.v2;

import com.github.quiram.utils.ReflectiveToStringCompareEquals;

import java.math.BigDecimal;

import static com.github.quiram.utils.ArgumentChecks.ensureGreaterThanZero;
import static com.github.quiram.utils.ArgumentChecks.ensureNotNull;

public class UnitPrice extends ReflectiveToStringCompareEquals<UnitPrice> {
    private BigDecimal value;

    public UnitPrice() {

    }

    public UnitPrice(BigDecimal value) {
        ensureNotNull(value, "price");
        ensureGreaterThanZero(value.intValue(), "price");
        this.value = value;
    }

    public UnitPrice(int value) {
        this(new BigDecimal(value));
    }

    public BigDecimal getValue() {
        return value;
    }

    public void setValue(BigDecimal value) {
        this.value = value;
    }
}
