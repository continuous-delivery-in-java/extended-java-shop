package uk.co.danielbryant.shopping.stockmanager.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.Function;

import static com.amarinperez.test_utils.ArgumentChecks.BLANK_VALUES;
import static com.amarinperez.test_utils.ArgumentChecks.assertIllegalArguments;
import static com.amarinperez.test_utils.Exceptions.expectException;
import static com.amarinperez.utils.Random.randomString;
import static java.lang.String.format;

public class StockTest {
    @Rule
    public ExpectedException onBadInput = ExpectedException.none();

    @Test
    public void cannotHaveNegativeAmountOfStock() {
        onBadInput.expect(IllegalArgumentException.class);
        onBadInput.expectMessage("amount");
        onBadInput.expectMessage("negative");
        new Stock(randomString(), randomString(), -1);
    }

    @Test
    public void availableAmountCanBeZero() {
        new Stock(randomString(), randomString(), 0);
    }

    @Test
    public void availableAmountCanBePositive() {
        new Stock(randomString(), randomString(), 10);
    }

    @Test
    public void idMustHaveValue() {
        Function<String, Stock> constructor = id -> new Stock(id, randomString(), 1);
        final String field = "productId";

        assertIllegalArguments(constructor, field, BLANK_VALUES);
    }

    @Test
    public void skuMustHaveValue() {
        Function<String, Stock> constructor = sku -> new Stock(randomString(), sku, 1);
        final String field = "sku";

        assertIllegalArguments(constructor, field, BLANK_VALUES);
    }
}
