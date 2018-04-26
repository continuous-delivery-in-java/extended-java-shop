package uk.co.danielbryant.shopping.productcatalogue.model.v2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

public class UnitPriceTest {
    @Rule
    public ExpectedException onBadData = ExpectedException.none();

    @Test
    public void productCannotBeFree() {
        onBadData.expect(IllegalArgumentException.class);
        onBadData.expectMessage("price");
        new UnitPrice(new BigDecimal(0));
    }

    @Test
    public void priceCannotBeNegative() {
        onBadData.expect(IllegalArgumentException.class);
        onBadData.expectMessage("price");
        new UnitPrice(new BigDecimal(-1));
    }

    @Test
    public void priceCannotBeNull() {
        onBadData.expect(IllegalArgumentException.class);
        onBadData.expectMessage("price");
        new UnitPrice(null);
    }
}