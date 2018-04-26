package uk.co.danielbryant.shopping.productcatalogue.model.v2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class BulkPriceTest {
    @Rule
    public ExpectedException onBadData = ExpectedException.none();

    @Test
    public void priceMustBePresent() {
        onBadData.expect(IllegalArgumentException.class);
        onBadData.expectMessage("unit price");
        new BulkPrice(null, 2);
    }

    @Test
    public void minimumMustBeAtLeastTwo() {
        // no "bulk" if we can buy single items
        onBadData.expect(IllegalArgumentException.class);
        onBadData.expectMessage("minimum amount");
        new BulkPrice(new UnitPrice(1), 1);
    }
}
