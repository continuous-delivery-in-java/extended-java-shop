package uk.co.danielbryant.djshopping.productcatalogue.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.math.BigDecimal;
import java.util.Random;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.Assert.*;

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
    public void idMustBePresent() {
        assertCannotCreate(id -> new Product(id, randomString(), randomString(), new BigDecimal(10)), "id");
    }

    @Test
    public void nameMustBePresent() {
        assertCannotCreate(name -> new Product(randomString(), name, randomString(), new BigDecimal(10)), "name");
    }

    @Test
    public void descriptionMustBePresent() {
        assertCannotCreate(description -> new Product(randomString(), randomString(), description, new BigDecimal(10)), "description");
    }

    private static void assertCannotCreate(Function<String, Product> constructor, String field) {
        asList(null, "", "    ").forEach(value -> expectException(constructor, field, value, format("Exception expected when testing value '%s' for field '%s;", value, field)));
    }

    private static void expectException(Function<String, Product> constructor, String expectedMessage, String value, String failureMessage) {
        try {
            constructor.apply(value);
            fail(failureMessage);
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), containsString(expectedMessage));
        }
    }

    private static String randomString() {
        return "" + new Random().nextLong();
    }

}