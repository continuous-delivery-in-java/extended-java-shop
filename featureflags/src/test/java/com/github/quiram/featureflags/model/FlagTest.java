package com.github.quiram.featureflags.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.List;
import java.util.Random;
import java.util.function.Function;

import static com.amarinperez.test_utils.Exceptions.expectException;
import static com.amarinperez.utils.Random.randomString;
import static java.lang.String.format;
import static java.util.Arrays.asList;

public class FlagTest {
    private static final List<String> blankValues = asList(null, "", "    ");
    @Rule
    public ExpectedException onBadInput = ExpectedException.none();

    @Test
    public void portionMustBeValidPercentage() {
        assertCannotCreate(portion -> new Flag(randomLong(), randomString(), portion), "portionIn", asList(-1, 101));
    }

    @Test
    public void portionInCanBeZero() {
        new Flag(randomLong(), randomString(), 0);
    }

    @Test
    public void portionInCanBePositive() {
        new Flag(randomLong(), randomString(), 10);
    }

    @Test
    public void portionInCanBeUpTo100() {
        new Flag(randomLong(), randomString(), 100);
    }

    @Test
    public void nameMustHaveValue() {
        Function<String, Flag> constructor = name -> new Flag(randomLong(), name, 1);
        final String field = "name";

        assertCannotCreate(constructor, field, blankValues);
    }

    private static <T> void assertCannotCreate(Function<T, Flag> constructor, String field, List<T> values) {
        values.forEach(value ->
                expectException(() -> constructor.apply(value), IllegalArgumentException.class,
                        field, format("Exception expected when testing value '%s' for field '%s;", value, field)
                ));
    }

    private long randomLong() {
        return new Random().nextLong();
    }
}
