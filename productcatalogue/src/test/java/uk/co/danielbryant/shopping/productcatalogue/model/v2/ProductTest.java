package uk.co.danielbryant.shopping.productcatalogue.model.v2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.amarinperez.test_utils.ArgumentChecks.BLANK_VALUES;
import static com.amarinperez.test_utils.ArgumentChecks.assertIllegalArguments;
import static com.github.quiram.utils.Random.randomDouble;
import static com.github.quiram.utils.Random.randomString;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static uk.co.danielbryant.shopping.productcatalogue.model.v2.Price.complexPrice;
import static uk.co.danielbryant.shopping.productcatalogue.model.v2.Price.singlePrice;

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
        assertIllegalArguments(id -> new Product(id, randomString(), randomString(), singlePrice(10)), "id", BLANK_VALUES);
    }

    @Test
    public void nameMustBePresent() {
        assertIllegalArguments(name -> new Product(randomString(), name, randomString(), singlePrice(15)), "name", BLANK_VALUES);
    }

    @Test
    public void descriptionMustBePresent() {
        assertIllegalArguments(description -> new Product(randomString(), randomString(), description, singlePrice(20)), "description", BLANK_VALUES);
    }

    @Test
    public void canConvertToV1Product() {
        final Price price = complexPrice(Double.toString(randomDouble(100, 2) + 100),
                Double.toString(randomDouble(100, 2)),
                5);
        final String id = randomString();
        final String name = randomString();
        final String description = randomString();
        final Product v2Product = new Product(id, name, description, price);
        final uk.co.danielbryant.shopping.productcatalogue.model.v1.Product v1Product = v2Product.asV1Product();
        assertThat(v1Product.getId(), is(id));
        assertThat(v1Product.getName(), is(name));
        assertThat(v1Product.getDescription(), is(description));
        assertThat(v1Product.getPrice(), is(price.getSingle().getValue()));
    }
}