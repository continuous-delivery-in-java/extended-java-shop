package uk.co.danielbryant.shopping.productcatalogue.model.v2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.amarinperez.test_utils.ArgumentChecks.BLANK_VALUES;
import static com.amarinperez.test_utils.ArgumentChecks.assertIllegalArguments;
import static com.github.quiram.utils.Random.randomString;

public class ProductTest {
    @Rule
    public ExpectedException onBadData = ExpectedException.none();

    @Test
    public void priceMustBePresent() {
        onBadData.expect(IllegalArgumentException.class);
        onBadData.expectMessage("price");
        new Product(randomString(), randomString(), randomString(), null);
    }

    @Test
    public void idMustBePresent() {
        assertIllegalArguments(id -> new Product(id, randomString(), randomString(), Price.singlePrice(10)), "id", BLANK_VALUES);
    }

    @Test
    public void nameMustBePresent() {
        assertIllegalArguments(name -> new Product(randomString(), name, randomString(), Price.singlePrice(15)), "name", BLANK_VALUES);
    }

    @Test
    public void descriptionMustBePresent() {
        assertIllegalArguments(description -> new Product(randomString(), randomString(), description, Price.singlePrice(20)), "description", BLANK_VALUES);
    }

}