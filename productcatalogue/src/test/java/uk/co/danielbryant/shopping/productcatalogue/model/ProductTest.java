package uk.co.danielbryant.shopping.productcatalogue.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;

import static com.amarinperez.test_utils.ArgumentChecks.BLANK_VALUES;
import static com.amarinperez.test_utils.ArgumentChecks.assertIllegalArguments;
import static com.github.quiram.utils.Random.randomString;

public class ProductTest {
    @Rule
    public ExpectedException onBadData = ExpectedException.none();

    @Test
    public void productCannotBeFree() {
        onBadData.expect(IllegalArgumentException.class);
        onBadData.expectMessage("price");
        new Product(randomString(), randomString(), randomString(), new BigDecimal(0));
    }

    @Test
    public void priceCannotBeNegative() {
        onBadData.expect(IllegalArgumentException.class);
        onBadData.expectMessage("price");
        new Product(randomString(), randomString(), randomString(), new BigDecimal(-1));
    }

    @Test
    public void priceCannotBeNull() {
        onBadData.expect(IllegalArgumentException.class);
        onBadData.expectMessage("price");
        new Product(randomString(), randomString(), randomString(), null);
    }

    @Test
    public void idMustBePresent() {
        assertIllegalArguments(id -> new Product(id, randomString(), randomString(), new BigDecimal(10)), "id", BLANK_VALUES);
    }

    @Test
    public void nameMustBePresent() {
        assertIllegalArguments(name -> new Product(randomString(), name, randomString(), new BigDecimal(10)), "name", BLANK_VALUES);
    }

    @Test
    public void descriptionMustBePresent() {
        assertIllegalArguments(description -> new Product(randomString(), randomString(), description, new BigDecimal(10)), "description", BLANK_VALUES);
    }
}