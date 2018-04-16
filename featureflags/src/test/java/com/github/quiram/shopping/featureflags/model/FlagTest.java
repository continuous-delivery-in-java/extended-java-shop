package com.github.quiram.shopping.featureflags.model;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.function.Function;

import static com.github.quiram.test_utils.ArgumentChecks.BLANK_VALUES;
import static com.github.quiram.test_utils.ArgumentChecks.assertIllegalArguments;
import static com.github.quiram.utils.Random.randomLong;
import static com.github.quiram.utils.Random.randomString;
import static java.util.Arrays.asList;

public class FlagTest {
    @Rule
    public ExpectedException onBadInput = ExpectedException.none();

    @Test
    public void portionMustBeValidPercentage() {
        assertIllegalArguments(portion -> new Flag(randomLong(), randomString(), portion), "portionIn", asList(-1, 101));
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

        assertIllegalArguments(constructor, field, BLANK_VALUES);
    }
}
