package uk.co.danielbryant.shopping.productcatalogue.model.v2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class PriceTest {
    @Rule
    public ExpectedException onBadData = ExpectedException.none();


    @Test
    public void priceCannotBeNull() {
        onBadData.expect(IllegalArgumentException.class);
        onBadData.expectMessage("single price");
        new Price(null, null);
    }

    @Test
    public void bulkPriceCanBeNull() {
        new Price(new UnitPrice(1), null);
    }

    @Test
    public void bulkPriceCannotBeSameAsSinglePrice() {
        onBadData.expect(IllegalArgumentException.class);
        onBadData.expectMessage("single price");
        onBadData.expectMessage("bulk price");
        new Price(new UnitPrice(1), new BulkPrice(new UnitPrice(1), 2));
    }

    @Test
    public void bulkPriceCannotBeHigherThanSinglePrice() {
        onBadData.expect(IllegalArgumentException.class);
        onBadData.expectMessage("single price");
        onBadData.expectMessage("bulk price");
        new Price(new UnitPrice(1), new BulkPrice(new UnitPrice(2), 2));
    }

}