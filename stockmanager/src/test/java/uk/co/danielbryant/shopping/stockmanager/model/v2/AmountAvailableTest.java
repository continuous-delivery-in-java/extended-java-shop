package uk.co.danielbryant.shopping.stockmanager.model.v2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.quiram.utils.Random.randomPositiveInt;

public class AmountAvailableTest {
    @Rule
    public ExpectedException onBadInput = ExpectedException.none();

    @Test
    public void cannotHaveNegativeTotalAmount() {
        onBadInput.expect(IllegalArgumentException.class);
        onBadInput.expectMessage("total");
        new AmountAvailable(-1, randomPositiveInt());
    }

    @Test
    public void cannotHaveNegativePerPurchaseAmount() {
        onBadInput.expect(IllegalArgumentException.class);
        onBadInput.expectMessage("perPurchase");
        new AmountAvailable(randomPositiveInt(), -1);
    }

    @Test
    public void perPurchaseCannotBeHigherThanTotal() {
        onBadInput.expect(IllegalArgumentException.class);
        onBadInput.expectMessage("perPurchase");
        onBadInput.expectMessage("total");
        final int total = randomPositiveInt();
        new AmountAvailable(total, total + 1);
    }

    @Test
    public void totalAmountCanBeZero() {
        new AmountAvailable(0, 0);
    }

    @Test
    public void totalAmountCanBePositive() {
        new AmountAvailable(1, 0);
    }

    @Test
    public void perPurchaseAmountCanBeZero() {
        new AmountAvailable(randomPositiveInt(), 0);
    }

    @Test
    public void perPurchaseAmountCanBePositive() {
        new AmountAvailable(randomPositiveInt(), 1);
    }

}