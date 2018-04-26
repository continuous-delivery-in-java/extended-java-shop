package uk.co.danielbryant.shopping.productcatalogue.model.v2;

import java.math.BigDecimal;

import static com.github.quiram.utils.ArgumentChecks.ensure;
import static com.github.quiram.utils.ArgumentChecks.ensureNotNull;

public class Price {
    private UnitPrice single;
    private BulkPrice bulkPrice;

    public Price(UnitPrice single, BulkPrice bulkPrice) {
        ensureNotNull(single, "single price");
        ensure(bulkPrice, "bulk price", bulk -> bulk != null && bulkPriceNotLowerThanSinglePrice(bulk, single),
                "be lower than single price or not be there");

        this.single = single;
        this.bulkPrice = bulkPrice;
    }

    private static boolean bulkPriceNotLowerThanSinglePrice(BulkPrice bulkPrice, UnitPrice singlePrice) {
        return bulkPrice.getUnit().compareTo(singlePrice) >= 0;
    }

    public static Price singlePrice(int singlePrice) {
        return new Price(new UnitPrice(singlePrice), null);
    }

    public static Price complexPrice(String singlePrice, String bulkPrice, int bulkMinAmount) {
        return new Price(new UnitPrice(new BigDecimal(singlePrice)), new BulkPrice(new UnitPrice(new BigDecimal(bulkPrice)), bulkMinAmount));
    }

    public UnitPrice getSingle() {
        return single;
    }

    public void setSingle(UnitPrice single) {
        this.single = single;
    }

    public BulkPrice getBulkPrice() {
        return bulkPrice;
    }

    public void setBulkPrice(BulkPrice bulkPrice) {
        this.bulkPrice = bulkPrice;
    }
}
