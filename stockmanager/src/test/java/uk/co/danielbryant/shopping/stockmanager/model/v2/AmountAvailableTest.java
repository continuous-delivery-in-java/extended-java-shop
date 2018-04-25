package uk.co.danielbryant.shopping.stockmanager.model.v2;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static com.github.quiram.utils.Random.randomInt;

public class AmountAvailableTest {
    @Rule
    public ExpectedException onBadInput = ExpectedException.none();

    @Test
    public void cannotHaveNegativeTotalAmount() {
        onBadInput.expect(IllegalArgumentException.class);
        onBadInput.expectMessage("total");
        new AmountAvailable(-1, Math.abs(randomInt()));
    }

    @Test
    public void cannotHaveNegativePerPurchaseAmount() {
        onBadInput.expect(IllegalArgumentException.class);
        onBadInput.expectMessage("perPurchase");
        new AmountAvailable(Math.abs(randomInt()), -1);
    }

    @Test
    public void perPurchaseCannotBeHigherThanTotal() {
        onBadInput.expect(IllegalArgumentException.class);
        onBadInput.expectMessage("perPurchase");
        onBadInput.expectMessage("total");
        final int total = Math.abs(randomInt());
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
        new AmountAvailable(Math.abs(randomInt()), 0);
    }

    @Test
    public void perPurchaseAmountCanBePositive() {
        new AmountAvailable(Math.abs(randomInt()), 1);
    }

}