package uk.co.danielbryant.shopping.stockmanager.model.v2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.Function;

import static com.github.quiram.test_utils.ArgumentChecks.BLANK_VALUES;
import static com.github.quiram.test_utils.ArgumentChecks.assertIllegalArguments;
import static com.github.quiram.utils.Random.randomInt;
import static com.github.quiram.utils.Random.randomString;
import static java.lang.Math.abs;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

public class StockTest {
    @Rule
    public ExpectedException onBadInput = ExpectedException.none();

    @Test
    public void mustHaveAmount() {
        onBadInput.expect(IllegalArgumentException.class);
        onBadInput.expectMessage("amount");
        onBadInput.expectMessage("null");
        new Stock(randomString(), randomString(), null);
    }

    @Test
    public void idMustHaveValue() {
        Function<String, Stock> constructor = id -> new Stock(id, randomString(), randomAmount());
        final String field = "productId";

        assertIllegalArguments(constructor, field, BLANK_VALUES);
    }

    @Test
    public void skuMustHaveValue() {
        Function<String, Stock> constructor = sku -> new Stock(randomString(), sku, randomAmount());
        final String field = "sku";

        assertIllegalArguments(constructor, field, BLANK_VALUES);
    }

    @Test
    public void canTransformToV1() {
        final String productId = randomString();
        final String sku = randomString();
        final AmountAvailable amountAvailable = randomAmount();
        final Stock v2Stock = new Stock(productId, sku, amountAvailable);
        final uk.co.danielbryant.shopping.stockmanager.model.v1.Stock v1Stock = v2Stock.asV1Stock();
        assertThat(v1Stock.getProductId(), is(productId));
        assertThat(v1Stock.getSku(), is(sku));
        assertThat(v1Stock.getAmountAvailable(), is(amountAvailable.getTotal()));
    }

    private static AmountAvailable randomAmount() {
        final int total = abs(randomInt());
        return new AmountAvailable(total, total - 1);
    }
}
